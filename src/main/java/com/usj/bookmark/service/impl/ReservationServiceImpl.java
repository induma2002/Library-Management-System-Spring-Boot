package com.usj.bookmark.service.impl;

import com.usj.bookmark.domain.entity.Book;
import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.domain.entity.Reservation;
import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.domain.enums.ReservationStatus;
import com.usj.bookmark.exception.BusinessRuleException;
import com.usj.bookmark.exception.ResourceNotFoundException;
import com.usj.bookmark.repository.BookRepository;
import com.usj.bookmark.repository.MemberRepository;
import com.usj.bookmark.repository.ReservationRepository;
import com.usj.bookmark.service.ReservationService;
import com.usj.bookmark.web.dto.CreateReservationRequest;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.dto.ReservationResponse;
import com.usj.bookmark.web.mapper.ReservationMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

	private static final int DEFAULT_HOLD_DAYS = 3;

	private final ReservationRepository reservationRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	public ReservationServiceImpl(ReservationRepository reservationRepository,
									 BookRepository bookRepository,
									 MemberRepository memberRepository) {
		this.reservationRepository = reservationRepository;
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
	}

	@Override
	public ReservationResponse create(CreateReservationRequest request) {
		Book book = bookRepository.findById(request.getBookId())
			.orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + request.getBookId()));
		Member member = memberRepository.findById(request.getMemberId())
			.orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + request.getMemberId()));

		if (member.getStatus() != MemberStatus.ACTIVE) {
			throw new BusinessRuleException("Only active members can reserve books");
		}
		List<Reservation> activeReservations = reservationRepository.findByBookIdAndStatusOrderByReservedAtAsc(book.getId(), ReservationStatus.ACTIVE);
		boolean alreadyReserved = activeReservations.stream().anyMatch(reservation -> reservation.getMember().getId().equals(member.getId()));
		if (alreadyReserved) {
			throw new BusinessRuleException("Member already has an active reservation for this book");
		}

		LocalDateTime now = LocalDateTime.now();
		int holdDays = request.getHoldDays() != null ? request.getHoldDays() : DEFAULT_HOLD_DAYS;
		Reservation reservation = new Reservation();
		reservation.setBook(book);
		reservation.setMember(member);
		reservation.setReservedAt(now);
		reservation.setExpiresAt(now.plusDays(holdDays));
		reservation.setStatus(ReservationStatus.ACTIVE);

		Reservation saved = reservationRepository.save(reservation);
		return ReservationMapper.toResponse(saved);
	}

	@Override
	public ReservationResponse updateStatus(Long reservationId, ReservationStatus status) {
		Reservation reservation = findReservationOrThrow(reservationId);
		reservation.setStatus(status);
		return ReservationMapper.toResponse(reservation);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<ReservationResponse> listForMember(Long memberId, int page, int size) {
		Page<Reservation> result = reservationRepository.findByMemberId(memberId, PageRequest.of(page, size));
		return new PageResponse<>(
			result.map(ReservationMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages());
	}

	@Override
	@Transactional(readOnly = true)
	public ReservationResponse getNextActiveReservation(Long bookId) {
		List<Reservation> reservations = reservationRepository.findByBookIdAndStatusOrderByReservedAtAsc(bookId, ReservationStatus.ACTIVE);
		if (reservations.isEmpty()) {
			throw new ResourceNotFoundException("No active reservation for book " + bookId);
		}
		return ReservationMapper.toResponse(reservations.get(0));
	}

	@Override
	public void expireReservations() {
		LocalDateTime now = LocalDateTime.now();
		List<Reservation> expired = reservationRepository.findByExpiresAtBeforeAndStatus(now, ReservationStatus.ACTIVE);
		expired.forEach(reservation -> reservation.setStatus(ReservationStatus.EXPIRED));
	}

	private Reservation findReservationOrThrow(Long id) {
		return reservationRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));
	}
}
