package com.usj.bookmark.web.controller;

import com.usj.bookmark.domain.enums.LoanStatus;
import com.usj.bookmark.service.LoanService;
import com.usj.bookmark.web.dto.CreateLoanRequest;
import com.usj.bookmark.web.dto.LoanResponse;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.dto.ReturnLoanRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@PostMapping
	public ResponseEntity<LoanResponse> checkout(@RequestBody @Valid CreateLoanRequest request) {
		LoanResponse response = loanService.checkout(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/{id}/return")
	public LoanResponse returnLoan(@PathVariable Long id, @RequestBody(required = false) ReturnLoanRequest request) {
		ReturnLoanRequest payload = request != null ? request : new ReturnLoanRequest();
		return loanService.returnLoan(id, payload);
	}

	@GetMapping("/{id}")
	public LoanResponse get(@PathVariable Long id) {
		return loanService.getById(id);
	}

	@GetMapping
	public PageResponse<LoanResponse> list(@RequestParam(defaultValue = "0") int page,
								 @RequestParam(defaultValue = "20") int size,
								 @RequestParam(required = false) LoanStatus status) {
		return loanService.listLoans(page, size, status);
	}

	@GetMapping("/members/{memberId}")
	public PageResponse<LoanResponse> listForMember(@PathVariable Long memberId,
												 @RequestParam(defaultValue = "0") int page,
												 @RequestParam(defaultValue = "20") int size) {
		return loanService.listLoansForMember(memberId, page, size);
	}

	@PostMapping("/overdue/scan")
	public ResponseEntity<Void> scanOverdue() {
		loanService.scanAndMarkOverdueLoans();
		return ResponseEntity.accepted().build();
	}
}
