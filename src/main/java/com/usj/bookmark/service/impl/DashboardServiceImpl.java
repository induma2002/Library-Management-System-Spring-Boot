package com.usj.bookmark.service.impl;

import com.usj.bookmark.domain.enums.FineStatus;
import com.usj.bookmark.domain.enums.LoanStatus;
import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.domain.enums.ReservationStatus;
import com.usj.bookmark.repository.BookRepository;
import com.usj.bookmark.repository.FineRepository;
import com.usj.bookmark.repository.LoanRepository;
import com.usj.bookmark.repository.MemberRepository;
import com.usj.bookmark.repository.ReservationRepository;
import com.usj.bookmark.service.DashboardService;
import com.usj.bookmark.web.dto.DashboardSummaryResponse;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final LoanRepository loanRepository;
	private final ReservationRepository reservationRepository;
	private final FineRepository fineRepository;

	public DashboardServiceImpl(BookRepository bookRepository,
								 MemberRepository memberRepository,
								 LoanRepository loanRepository,
								 ReservationRepository reservationRepository,
								 FineRepository fineRepository) {
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
		this.loanRepository = loanRepository;
		this.reservationRepository = reservationRepository;
		this.fineRepository = fineRepository;
	}

	@Override
	public DashboardSummaryResponse getSummary() {
		long totalBooks = bookRepository.count();
		long availableCopies = bookRepository.sumAvailableCopies();
		long activeMembers = memberRepository.countByStatus(MemberStatus.ACTIVE);
		long activeLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);
		long overdueLoans = loanRepository.countByStatus(LoanStatus.OVERDUE);
		long pendingReservations = reservationRepository.countByStatus(ReservationStatus.ACTIVE);
		long pendingFines = fineRepository.countByStatus(FineStatus.PENDING);

		return new DashboardSummaryResponse(
			totalBooks,
			availableCopies,
			activeMembers,
			activeLoans,
			overdueLoans,
			pendingReservations,
			pendingFines,
			LocalDateTime.now());
	}
}
