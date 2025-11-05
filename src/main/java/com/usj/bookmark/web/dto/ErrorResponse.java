package com.usj.bookmark.web.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String message, String error, LocalDateTime timestamp) {

	public static ErrorResponse of(String message, String error) {
		return new ErrorResponse(message, error, LocalDateTime.now());
	}
}
