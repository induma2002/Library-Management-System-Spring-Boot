package com.usj.bookmark.service;

import com.usj.bookmark.domain.enums.ReservationStatus;
import com.usj.bookmark.web.dto.CreateReservationRequest;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.dto.ReservationResponse;

public interface ReservationService {

	ReservationResponse create(CreateReservationRequest request);

	ReservationResponse updateStatus(Long reservationId, ReservationStatus status);

	PageResponse<ReservationResponse> listForMember(Long memberId, int page, int size);

	ReservationResponse getNextActiveReservation(Long bookId);

	void expireReservations();
}
