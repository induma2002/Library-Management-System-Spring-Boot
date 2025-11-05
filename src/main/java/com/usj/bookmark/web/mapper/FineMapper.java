package com.usj.bookmark.web.mapper;

import com.usj.bookmark.domain.entity.Fine;
import com.usj.bookmark.web.dto.FineResponse;

public final class FineMapper {

	private FineMapper() {
	}

	public static FineResponse toResponse(Fine fine) {
		return new FineResponse(
			fine.getId(),
			fine.getMember().getId(),
			fine.getLoan() != null ? fine.getLoan().getId() : null,
			fine.getAmount(),
			fine.getStatus(),
			fine.getIssuedAt(),
			fine.getSettledAt(),
			fine.getDescription());
	}
}
