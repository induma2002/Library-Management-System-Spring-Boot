package com.usj.bookmark.web.dto;

import com.usj.bookmark.domain.enums.MemberStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponse(
		Long id,
		String membershipId,
		String fullName,
		String email,
		String phoneNumber,
		String address,
		LocalDate joinedOn,
		MemberStatus status,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
