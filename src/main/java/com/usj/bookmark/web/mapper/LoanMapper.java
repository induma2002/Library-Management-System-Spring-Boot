package com.usj.bookmark.web.mapper;

import com.usj.bookmark.domain.entity.Loan;
import com.usj.bookmark.web.dto.LoanResponse;

public final class LoanMapper {

	private LoanMapper() {
	}

	public static LoanResponse toResponse(Loan loan) {
		return new LoanResponse(
			loan.getId(),
			loan.getBook().getId(),
			loan.getBook().getTitle(),
			loan.getMember().getId(),
			loan.getMember().getFullName(),
			loan.getCheckoutDate(),
			loan.getDueDate(),
			loan.getReturnedDate(),
			loan.getStatus(),
			loan.getFineAccrued());
	}
}
