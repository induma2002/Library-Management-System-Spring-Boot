package com.usj.bookmark.web.dto;

import java.time.LocalDateTime;

public record DashboardSummaryResponse(
		long totalBooks,
		long availableBooks,
		long activeMembers,
		long activeLoans,
		long overdueLoans,
		long pendingReservations,
		long pendingFines,
		LocalDateTime generatedAt) {
}
