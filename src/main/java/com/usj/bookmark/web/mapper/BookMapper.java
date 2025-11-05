package com.usj.bookmark.web.mapper;

import com.usj.bookmark.domain.entity.Book;
import com.usj.bookmark.web.dto.BookRequest;
import com.usj.bookmark.web.dto.BookResponse;
import java.util.HashSet;
import java.util.Set;

public final class BookMapper {

	private BookMapper() {
	}

	public static void updateEntity(Book book, BookRequest request) {
		book.setTitle(request.getTitle());
		book.setAuthor(request.getAuthor());
		book.setIsbn(request.getIsbn());
		book.setPublisher(request.getPublisher());
		book.setCategory(request.getCategory());
		book.setLanguage(request.getLanguage());
		book.setPublicationYear(request.getPublicationYear());
		book.setReferenceOnly(request.isReferenceOnly());
		Set<String> tags = request.getTags() != null ? new HashSet<>(request.getTags()) : new HashSet<>();
		book.setTags(tags);
	}

	public static BookResponse toResponse(Book book) {
		return new BookResponse(
			book.getId(),
			book.getTitle(),
			book.getAuthor(),
			book.getIsbn(),
			book.getPublisher(),
			book.getCategory(),
			book.getLanguage(),
			book.getPublicationYear(),
			book.getTotalCopies(),
			book.getAvailableCopies(),
			book.isReferenceOnly(),
			book.getTags(),
			book.getCreatedAt(),
			book.getUpdatedAt());
	}
}
