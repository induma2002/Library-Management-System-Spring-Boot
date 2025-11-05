package com.usj.bookmark.web.dto;

import com.usj.bookmark.domain.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public class ReservationStatusUpdateRequest {

	@NotNull
	private ReservationStatus status;

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
}
