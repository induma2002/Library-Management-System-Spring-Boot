package com.usj.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.usj.bookmark.exception.BusinessRuleException;
import com.usj.bookmark.web.dto.BookInventoryUpdateRequest;
import com.usj.bookmark.web.dto.BookRequest;
import com.usj.bookmark.web.dto.BookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BookServiceTest {

	@Autowired
	private BookService bookService;

	@Test
	void createBookInitialisesInventory() {
		BookRequest request = new BookRequest();
		request.setTitle("The Pragmatic Programmer");
		request.setAuthor("Andy Hunt");
		request.setIsbn("9780201616224");
		request.setPublisher("Addison-Wesley");
		request.setCategory("Software");
		request.setLanguage("EN");
		request.setPublicationYear(1999);
		request.setTotalCopies(4);
		request.setReferenceOnly(false);

		BookResponse response = bookService.create(request);

		assertThat(response.totalCopies()).isEqualTo(4);
		assertThat(response.availableCopies()).isEqualTo(4);
		assertThat(response.id()).isNotNull();
	}

	@Test
	void adjustInventoryPreventsRemovingBorrowedCopies() {
		BookRequest request = new BookRequest();
		request.setTitle("Domain-Driven Design");
		request.setAuthor("Eric Evans");
		request.setIsbn("9780321125217");
		request.setPublisher("Addison-Wesley");
		request.setCategory("Software");
		request.setLanguage("EN");
		request.setPublicationYear(2003);
		request.setTotalCopies(2);
		request.setReferenceOnly(false);

		BookResponse response = bookService.create(request);

		BookInventoryUpdateRequest updateRequest = new BookInventoryUpdateRequest();
		updateRequest.setDelta(-3);

		assertThatThrownBy(() -> bookService.adjustInventory(response.id(), updateRequest.getDelta()))
			.isInstanceOf(BusinessRuleException.class);
	}
}
