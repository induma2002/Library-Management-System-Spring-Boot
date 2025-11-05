package com.usj.bookmark.service;

import com.usj.bookmark.web.dto.BookRequest;
import com.usj.bookmark.web.dto.BookResponse;
import com.usj.bookmark.web.dto.PageResponse;

public interface BookService {

	BookResponse create(BookRequest request);

	BookResponse update(Long id, BookRequest request);

	BookResponse getById(Long id);

	void delete(Long id);

	PageResponse<BookResponse> search(String query, String category, int page, int size);

	BookResponse adjustInventory(Long bookId, int delta);
}
