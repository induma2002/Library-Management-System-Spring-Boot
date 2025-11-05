package com.usj.bookmark.web.mapper;

import com.usj.bookmark.domain.entity.Reservation;
import com.usj.bookmark.web.dto.ReservationResponse;

public final class ReservationMapper {

	private ReservationMapper() {
	}

	public static ReservationResponse toResponse(Reservation reservation) {
		return new ReservationResponse(
			reservation.getId(),
			reservation.getBook().getId(),
			reservation.getBook().getTitle(),
			reservation.getMember().getId(),
			reservation.getMember().getFullName(),
			reservation.getReservedAt(),
			reservation.getExpiresAt(),
			reservation.getStatus(),
			reservation.getNotes());
	}
}
