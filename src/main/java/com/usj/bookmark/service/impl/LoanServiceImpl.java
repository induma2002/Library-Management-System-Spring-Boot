package com.usj.bookmark.service.impl;

import com.usj.bookmark.domain.entity.Book;
import com.usj.bookmark.domain.entity.Loan;
import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.domain.entity.Reservation;
import com.usj.bookmark.domain.enums.LoanStatus;
import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.domain.enums.ReservationStatus;
import com.usj.bookmark.exception.BusinessRuleException;
import com.usj.bookmark.exception.ResourceNotFoundException;
import com.usj.bookmark.repository.BookRepository;
import com.usj.bookmark.repository.LoanRepository;
import com.usj.bookmark.repository.MemberRepository;
import com.usj.bookmark.repository.ReservationRepository;
import com.usj.bookmark.service.FineService;
import com.usj.bookmark.service.LoanService;
import com.usj.bookmark.web.dto.CreateLoanRequest;
import com.usj.bookmark.web.dto.LoanResponse;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.dto.ReturnLoanRequest;
import com.usj.bookmark.web.mapper.LoanMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

	private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;
	private static final int MAX_ACTIVE_LOANS = 5;
	private static final BigDecimal DAILY_FINE = new BigDecimal("1.50");

	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;
	private final FineService fineService;

	public LoanServiceImpl(LoanRepository loanRepository,
							BookRepository bookRepository,
							MemberRepository memberRepository,
							ReservationRepository reservationRepository,
							FineService fineService) {
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
		this.reservationRepository = reservationRepository;
		this.fineService = fineService;
	}

	@Override
	public LoanResponse checkout(CreateLoanRequest request) {
		Book book = bookRepository.findById(request.getBookId())
			.orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + request.getBookId()));
		Member member = memberRepository.findById(request.getMemberId())
			.orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + request.getMemberId()));

		if (member.getStatus() != MemberStatus.ACTIVE) {
			throw new BusinessRuleException("Member status does not allow new loans");
		}
		if (book.isReferenceOnly()) {
			throw new BusinessRuleException("Reference-only items cannot be loaned out");
		}
		List<Loan> activeLoans = loanRepository.findByMemberIdAndStatusIn(member.getId(), List.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE));
		if (activeLoans.size() >= MAX_ACTIVE_LOANS) {
			throw new BusinessRuleException("Member has reached the maximum number of active loans");
		}
		boolean alreadyBorrowed = activeLoans.stream().anyMatch(loan -> Objects.equals(loan.getBook().getId(), book.getId()));
		if (alreadyBorrowed) {
			throw new BusinessRuleException("Member already has an active loan for this book");
		}

		List<Reservation> reservationQueue = reservationRepository.findByBookIdAndStatusOrderByReservedAtAsc(book.getId(), ReservationStatus.ACTIVE);
		Reservation firstReservation = reservationQueue.isEmpty() ? null : reservationQueue.get(0);
		Reservation matchedReservation = null;
		if (book.getAvailableCopies() <= 0) {
			if (firstReservation == null || !Objects.equals(firstReservation.getMember().getId(), member.getId())) {
				throw new BusinessRuleException("No copies available right now");
			}
			matchedReservation = firstReservation;
		} else {
			if (firstReservation != null && !Objects.equals(firstReservation.getMember().getId(), member.getId())) {
				throw new BusinessRuleException("Book is on hold for another member");
			}
			book.decreaseAvailableCopies();
			if (firstReservation != null) {
				matchedReservation = firstReservation;
			}
		}

		LocalDateTime now = LocalDateTime.now();
		int period = request.getLoanPeriodDays() != null ? request.getLoanPeriodDays() : DEFAULT_LOAN_PERIOD_DAYS;
		LocalDateTime dueDate = now.plusDays(period);

		Loan loan = new Loan();
		loan.setBook(book);
		loan.setMember(member);
		loan.setCheckoutDate(now);
		loan.setDueDate(dueDate);
		loan.setStatus(LoanStatus.ACTIVE);
		loan.setFineAccrued(BigDecimal.ZERO);
		loan.setFineClosed(true);

		Loan saved = loanRepository.save(loan);

		if (matchedReservation != null) {
			matchedReservation.setStatus(ReservationStatus.FULFILLED);
			matchedReservation.setNotes("Fulfilled by loan " + saved.getId());
		}

		return LoanMapper.toResponse(saved);
	}

	@Override
	public LoanResponse returnLoan(Long loanId, ReturnLoanRequest request) {
		Loan loan = findLoanOrThrow(loanId);
		if (loan.getStatus() == LoanStatus.RETURNED) {
			return LoanMapper.toResponse(loan);
		}
		LocalDateTime returnedAt = request.getReturnedAt() != null ? request.getReturnedAt() : LocalDateTime.now();
		loan.setReturnedDate(returnedAt);
		loan.setStatus(LoanStatus.RETURNED);

		Book book = loan.getBook();
		book.increaseAvailableCopies();

		LocalDate dueDate = loan.getDueDate().toLocalDate();
		LocalDate returnDate = returnedAt.toLocalDate();
		long overdueDays = Math.max(0, ChronoUnit.DAYS.between(dueDate, returnDate));
		if (overdueDays > 0) {
			BigDecimal amount = DAILY_FINE.multiply(BigDecimal.valueOf(overdueDays));
			loan.setFineAccrued(loan.getFineAccrued().add(amount));
			loan.setFineClosed(false);
			fineService.recordFine(loan, amount, "Overdue by " + overdueDays + " days");
		}

		List<Reservation> reservations = reservationRepository.findByBookIdAndStatusOrderByReservedAtAsc(book.getId(), ReservationStatus.ACTIVE);
		Reservation nextReservation = reservations.isEmpty() ? null : reservations.get(0);
		if (nextReservation != null) {
			nextReservation.setStatus(ReservationStatus.FULFILLED);
			nextReservation.setNotes("Hold ready after loan " + loan.getId());
			book.decreaseAvailableCopies();
		}

		return LoanMapper.toResponse(loan);
	}

	@Override
	@Transactional(readOnly = true)
	public LoanResponse getById(Long id) {
		Loan loan = findLoanOrThrow(id);
		return LoanMapper.toResponse(loan);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<LoanResponse> listLoans(int page, int size, LoanStatus status) {
		Page<Loan> result = status != null
			? loanRepository.findByStatus(status, PageRequest.of(page, size))
			: loanRepository.findAll(PageRequest.of(page, size));
		return new PageResponse<>(
			result.map(LoanMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages());
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<LoanResponse> listLoansForMember(Long memberId, int page, int size) {
		Page<Loan> result = loanRepository.findByMemberId(memberId, PageRequest.of(page, size));
		return new PageResponse<>(
			result.map(LoanMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages());
	}

	@Override
	public void scanAndMarkOverdueLoans() {
		LocalDateTime now = LocalDateTime.now();
		List<Loan> overdueLoans = loanRepository.findByDueDateBeforeAndStatus(now, LoanStatus.ACTIVE);
		overdueLoans.forEach(loan -> loan.setStatus(LoanStatus.OVERDUE));
	}

	private Loan findLoanOrThrow(Long id) {
		return loanRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Loan not found with id " + id));
	}
}
