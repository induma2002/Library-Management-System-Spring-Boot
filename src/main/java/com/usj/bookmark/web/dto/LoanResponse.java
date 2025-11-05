package com.usj.bookmark.web.dto;

import com.usj.bookmark.domain.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanResponse(
		Long id,
		Long bookId,
		String bookTitle,
		Long memberId,
		String memberName,
		LocalDateTime checkoutDate,
		LocalDateTime dueDate,
		LocalDateTime returnedDate,
		LoanStatus status,
		BigDecimal fineAccrued) {
}
