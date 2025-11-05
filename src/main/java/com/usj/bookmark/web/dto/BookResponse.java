package com.usj.bookmark.web.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record BookResponse(
		Long id,
		String title,
		String author,
		String isbn,
		String publisher,
		String category,
		String language,
		Integer publicationYear,
		Integer totalCopies,
		Integer availableCopies,
		boolean referenceOnly,
		Set<String> tags,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
