package com.usj.bookmark.web.dto;

import com.usj.bookmark.domain.enums.FineStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FineResponse(
		Long id,
		Long memberId,
		Long loanId,
		BigDecimal amount,
		FineStatus status,
		LocalDateTime issuedAt,
		LocalDateTime settledAt,
		String description) {
}
