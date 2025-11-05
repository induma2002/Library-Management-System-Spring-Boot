package com.usj.bookmark.service;

import com.usj.bookmark.domain.entity.Loan;
import com.usj.bookmark.domain.enums.FineStatus;
import com.usj.bookmark.web.dto.FineResponse;
import com.usj.bookmark.web.dto.PageResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FineService {

	FineResponse getById(Long id);

	PageResponse<FineResponse> listAll(int page, int size, FineStatus status);

	PageResponse<FineResponse> listForMember(Long memberId, int page, int size, FineStatus status);

	FineResponse settleFine(Long fineId, boolean waive, LocalDateTime settledAt);

	void recordFine(Loan loan, BigDecimal amount, String description);
}
