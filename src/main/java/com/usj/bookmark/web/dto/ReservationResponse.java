package com.usj.bookmark.web.dto;

import com.usj.bookmark.domain.enums.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponse(
		Long id,
		Long bookId,
		String bookTitle,
		Long memberId,
		String memberName,
		LocalDateTime reservedAt,
		LocalDateTime expiresAt,
		ReservationStatus status,
		String notes) {
}
