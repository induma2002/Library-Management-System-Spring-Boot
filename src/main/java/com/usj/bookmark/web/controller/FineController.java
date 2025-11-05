package com.usj.bookmark.web.controller;

import com.usj.bookmark.domain.enums.FineStatus;
import com.usj.bookmark.service.FineService;
import com.usj.bookmark.web.dto.FinePaymentRequest;
import com.usj.bookmark.web.dto.FineResponse;
import com.usj.bookmark.web.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fines")
public class FineController {

	private final FineService fineService;

	public FineController(FineService fineService) {
		this.fineService = fineService;
	}

	@GetMapping
	public PageResponse<FineResponse> list(@RequestParam(defaultValue = "0") int page,
								 @RequestParam(defaultValue = "20") int size,
								 @RequestParam(required = false) FineStatus status) {
		return fineService.listAll(page, size, status);
	}

	@GetMapping("/{id}")
	public FineResponse get(@PathVariable Long id) {
		return fineService.getById(id);
	}

	@GetMapping("/members/{memberId}")
	public PageResponse<FineResponse> listForMember(@PathVariable Long memberId,
											    @RequestParam(defaultValue = "0") int page,
											    @RequestParam(defaultValue = "20") int size,
											    @RequestParam(required = false) FineStatus status) {
		return fineService.listForMember(memberId, page, size, status);
	}

	@PostMapping("/{id}/settle")
	public ResponseEntity<FineResponse> settle(@PathVariable Long id,
									 @RequestBody @Valid FinePaymentRequest request) {
		FineResponse response = fineService.settleFine(id, request.isWaive(), request.getSettledAt());
		return ResponseEntity.ok(response);
	}
}
