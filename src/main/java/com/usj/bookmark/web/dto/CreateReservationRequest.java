package com.usj.bookmark.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateReservationRequest {

	@NotNull
	private Long bookId;

	@NotNull
	private Long memberId;

	@Min(1)
	private Integer holdDays;

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Integer getHoldDays() {
		return holdDays;
	}

	public void setHoldDays(Integer holdDays) {
		this.holdDays = holdDays;
	}
}
