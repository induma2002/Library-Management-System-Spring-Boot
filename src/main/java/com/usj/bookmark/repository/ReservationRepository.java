package com.usj.bookmark.repository;

import com.usj.bookmark.domain.entity.Reservation;
import com.usj.bookmark.domain.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByMemberIdAndStatusIn(Long memberId, List<ReservationStatus> statuses);

	List<Reservation> findByBookIdAndStatusOrderByReservedAtAsc(Long bookId, ReservationStatus status);

	List<Reservation> findByExpiresAtBeforeAndStatus(LocalDateTime timestamp, ReservationStatus status);

	Page<Reservation> findByMemberId(Long memberId, Pageable pageable);

	long countByStatus(ReservationStatus status);
}
