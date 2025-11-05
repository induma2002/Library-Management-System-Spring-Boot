package com.usj.bookmark.web.controller;

import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.service.MemberService;
import com.usj.bookmark.web.dto.MemberRequest;
import com.usj.bookmark.web.dto.MemberResponse;
import com.usj.bookmark.web.dto.MemberStatusUpdateRequest;
import com.usj.bookmark.web.dto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping
	public ResponseEntity<MemberResponse> create(@RequestBody @Valid MemberRequest request) {
		MemberResponse response = memberService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public MemberResponse update(@PathVariable Long id, @RequestBody @Valid MemberRequest request) {
		return memberService.update(id, request);
	}

	@PatchMapping("/{id}/status")
	public MemberResponse updateStatus(@PathVariable Long id,
								   @RequestBody @Valid MemberStatusUpdateRequest request) {
		return memberService.changeStatus(id, request.getStatus());
	}

	@GetMapping("/{id}")
	public MemberResponse get(@PathVariable Long id) {
		return memberService.getById(id);
	}

	@GetMapping
	public PageResponse<MemberResponse> list(@RequestParam(defaultValue = "0") int page,
								   @RequestParam(defaultValue = "20") int size,
								   @RequestParam(required = false) MemberStatus status) {
		return memberService.list(page, size, status);
	}
}
