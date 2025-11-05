package com.usj.bookmark.service;

import com.usj.bookmark.domain.enums.LoanStatus;
import com.usj.bookmark.web.dto.CreateLoanRequest;
import com.usj.bookmark.web.dto.LoanResponse;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.dto.ReturnLoanRequest;

public interface LoanService {

	LoanResponse checkout(CreateLoanRequest request);

	LoanResponse returnLoan(Long loanId, ReturnLoanRequest request);

	LoanResponse getById(Long id);

	PageResponse<LoanResponse> listLoans(int page, int size, LoanStatus status);

	PageResponse<LoanResponse> listLoansForMember(Long memberId, int page, int size);

	void scanAndMarkOverdueLoans();
}
