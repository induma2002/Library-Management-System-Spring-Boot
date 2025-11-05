package com.usj.bookmark.service.impl;

import com.usj.bookmark.domain.entity.Book;
import com.usj.bookmark.exception.BusinessRuleException;
import com.usj.bookmark.exception.ResourceNotFoundException;
import com.usj.bookmark.repository.BookRepository;
import com.usj.bookmark.service.BookService;
import com.usj.bookmark.web.dto.BookRequest;
import com.usj.bookmark.web.dto.BookResponse;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.mapper.BookMapper;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public BookResponse create(BookRequest request) {
		bookRepository.findByIsbn(request.getIsbn()).ifPresent(existing -> {
			throw new BusinessRuleException("Book with the same ISBN already exists");
		});
		Book book = new Book();
		BookMapper.updateEntity(book, request);
		book.setTotalCopies(request.getTotalCopies());
		book.setAvailableCopies(request.getTotalCopies());
		Book saved = bookRepository.save(book);
		return BookMapper.toResponse(saved);
	}

	@Override
	public BookResponse update(Long id, BookRequest request) {
		Book existing = findBookOrThrow(id);
		Optional<Book> other = bookRepository.findByIsbn(request.getIsbn());
		if (other.isPresent() && !other.get().getId().equals(id)) {
			throw new BusinessRuleException("Another book with the same ISBN exists");
		}
		int borrowed = existing.getTotalCopies() - existing.getAvailableCopies();
		if (request.getTotalCopies() < borrowed) {
			throw new BusinessRuleException("Cannot reduce total copies below number of borrowed copies");
		}
		BookMapper.updateEntity(existing, request);
		existing.setTotalCopies(request.getTotalCopies());
		existing.setAvailableCopies(request.getTotalCopies() - borrowed);
		return BookMapper.toResponse(existing);
	}

	@Override
	@Transactional(readOnly = true)
	public BookResponse getById(Long id) {
		Book book = findBookOrThrow(id);
		return BookMapper.toResponse(book);
	}

	@Override
	public void delete(Long id) {
		Book book = findBookOrThrow(id);
		int borrowed = book.getTotalCopies() - book.getAvailableCopies();
		if (borrowed > 0) {
			throw new BusinessRuleException("Cannot delete book with active loans");
		}
		bookRepository.delete(book);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<BookResponse> search(String query, String category, int page, int size) {
		Page<Book> result = bookRepository.search(normalize(query), normalize(category), PageRequest.of(page, size));
		return new PageResponse<>(
			result.map(BookMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages());
	}

	@Override
	public BookResponse adjustInventory(Long bookId, int delta) {
		Book book = findBookOrThrow(bookId);
		if (delta == 0) {
			return BookMapper.toResponse(book);
		}
		if (delta > 0) {
			book.setTotalCopies(book.getTotalCopies() + delta);
			book.setAvailableCopies(book.getAvailableCopies() + delta);
		} else {
			int absDelta = Math.abs(delta);
			if (absDelta > book.getAvailableCopies()) {
				throw new BusinessRuleException("Cannot remove more copies than available");
			}
			book.setTotalCopies(book.getTotalCopies() - absDelta);
			book.setAvailableCopies(book.getAvailableCopies() - absDelta);
		}
		return BookMapper.toResponse(book);
	}

	private Book findBookOrThrow(Long id) {
		return bookRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
	}

	private String normalize(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}
}
