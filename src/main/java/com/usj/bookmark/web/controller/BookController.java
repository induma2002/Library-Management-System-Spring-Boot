package com.usj.bookmark.web.controller;

import com.usj.bookmark.service.BookService;
import com.usj.bookmark.web.dto.BookInventoryUpdateRequest;
import com.usj.bookmark.web.dto.BookRequest;
import com.usj.bookmark.web.dto.BookResponse;
import com.usj.bookmark.web.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	public ResponseEntity<BookResponse> create(@RequestBody @Valid BookRequest request) {
		BookResponse response = bookService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public BookResponse get(@PathVariable Long id) {
		return bookService.getById(id);
	}

	@GetMapping
	public PageResponse<BookResponse> search(@RequestParam(required = false) String query,
											@RequestParam(required = false) String category,
											@RequestParam(defaultValue = "0") int page,
											@RequestParam(defaultValue = "20") int size) {
		return bookService.search(query, category, page, size);
	}

	@PutMapping("/{id}")
	public BookResponse update(@PathVariable Long id, @RequestBody @Valid BookRequest request) {
		return bookService.update(id, request);
	}

	@PatchMapping("/{id}/inventory")
	public BookResponse adjustInventory(@PathVariable Long id,
									   @RequestBody @Valid BookInventoryUpdateRequest request) {
		return bookService.adjustInventory(id, request.getDelta());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
