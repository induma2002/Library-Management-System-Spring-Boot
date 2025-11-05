package com.usj.bookmark.web.dto;

import java.time.LocalDateTime;

public class FinePaymentRequest {

	private LocalDateTime settledAt;

	private boolean waive;

	public LocalDateTime getSettledAt() {
		return settledAt;
	}

	public void setSettledAt(LocalDateTime settledAt) {
		this.settledAt = settledAt;
	}

	public boolean isWaive() {
		return waive;
	}

	public void setWaive(boolean waive) {
		this.waive = waive;
	}
}
