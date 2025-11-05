package com.usj.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.usj.bookmark.domain.entity.Book;
import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.domain.enums.FineStatus;
import com.usj.bookmark.domain.enums.LoanStatus;
import com.usj.bookmark.repository.BookRepository;
import com.usj.bookmark.repository.FineRepository;
import com.usj.bookmark.repository.MemberRepository;
import com.usj.bookmark.web.dto.CreateLoanRequest;
import com.usj.bookmark.web.dto.LoanResponse;
import com.usj.bookmark.web.dto.ReturnLoanRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoanServiceTest {

	@Autowired
	private LoanService loanService;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FineRepository fineRepository;

	private Book book;

	private Member member;

	@BeforeEach
	void setup() {
		book = bookRepository.findByIsbn("9780132350884").orElseGet(() -> {
			Book fresh = new Book();
			fresh.setTitle("Clean Architecture");
			fresh.setAuthor("Robert C. Martin");
			fresh.setIsbn("9780134494166");
			fresh.setTotalCopies(4);
			fresh.setAvailableCopies(4);
			return bookRepository.save(fresh);
		});
		member = memberRepository.findByMembershipId("MEM-001").orElseThrow();
	}

	@Test
	void checkoutDecrementsAvailableCopies() {
		int original = book.getAvailableCopies();
		CreateLoanRequest request = new CreateLoanRequest();
		request.setBookId(book.getId());
		request.setMemberId(member.getId());

		LoanResponse response = loanService.checkout(request);

		Book reloaded = bookRepository.findById(book.getId()).orElseThrow();
		assertThat(reloaded.getAvailableCopies()).isEqualTo(Math.max(0, original - 1));
		assertThat(response.status()).isEqualTo(LoanStatus.ACTIVE);
	}

	@Test
	void returnLoanGeneratesFineWhenOverdue() {
		CreateLoanRequest request = new CreateLoanRequest();
		request.setBookId(book.getId());
		request.setMemberId(member.getId());
		request.setLoanPeriodDays(7);

		LoanResponse loan = loanService.checkout(request);

		ReturnLoanRequest returnRequest = new ReturnLoanRequest();
		returnRequest.setReturnedAt(LocalDateTime.now().plusDays(12));

		LoanResponse returned = loanService.returnLoan(loan.id(), returnRequest);

		assertThat(returned.status()).isEqualTo(LoanStatus.RETURNED);
		assertThat(returned.fineAccrued()).isGreaterThan(BigDecimal.ZERO);
		long pendingFines = fineRepository.findByMemberIdAndStatus(member.getId(), FineStatus.PENDING).size();
		assertThat(pendingFines).isGreaterThanOrEqualTo(1);
	}
}
