package com.usj.bookmark.web.controller;

import com.usj.bookmark.domain.enums.ReservationStatus;
import com.usj.bookmark.service.ReservationService;
import com.usj.bookmark.web.dto.CreateReservationRequest;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.dto.ReservationResponse;
import com.usj.bookmark.web.dto.ReservationStatusUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	public ResponseEntity<ReservationResponse> create(@RequestBody @Valid CreateReservationRequest request) {
		ReservationResponse response = reservationService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/{id}/status")
	public ReservationResponse updateStatus(@PathVariable Long id,
									 @RequestBody @Valid ReservationStatusUpdateRequest request) {
		return reservationService.updateStatus(id, request.getStatus());
	}

	@GetMapping("/members/{memberId}")
	public PageResponse<ReservationResponse> listForMember(@PathVariable Long memberId,
												  @RequestParam(defaultValue = "0") int page,
												  @RequestParam(defaultValue = "20") int size) {
		return reservationService.listForMember(memberId, page, size);
	}

	@GetMapping("/books/{bookId}/next")
	public ReservationResponse getNextReservation(@PathVariable Long bookId) {
		return reservationService.getNextActiveReservation(bookId);
	}

	@PostMapping("/expire")
	public ResponseEntity<Void> expireReservations() {
		reservationService.expireReservations();
		return ResponseEntity.accepted().build();
	}
}
