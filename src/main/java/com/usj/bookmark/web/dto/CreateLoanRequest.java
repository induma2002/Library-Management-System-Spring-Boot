package com.usj.bookmark.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateLoanRequest {

	@NotNull
	private Long bookId;

	@NotNull
	private Long memberId;

	@Min(1)
	private Integer loanPeriodDays;

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

	public Integer getLoanPeriodDays() {
		return loanPeriodDays;
	}

	public void setLoanPeriodDays(Integer loanPeriodDays) {
		this.loanPeriodDays = loanPeriodDays;
	}
}
