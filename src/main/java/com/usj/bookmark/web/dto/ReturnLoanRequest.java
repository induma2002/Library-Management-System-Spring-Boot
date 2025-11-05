package com.usj.bookmark.web.dto;

import java.time.LocalDateTime;

public class ReturnLoanRequest {

	private LocalDateTime returnedAt;

	public LocalDateTime getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(LocalDateTime returnedAt) {
		this.returnedAt = returnedAt;
	}
}
