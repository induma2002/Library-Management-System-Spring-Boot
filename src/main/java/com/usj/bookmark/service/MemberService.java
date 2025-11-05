package com.usj.bookmark.service;

import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.web.dto.MemberRequest;
import com.usj.bookmark.web.dto.MemberResponse;
import com.usj.bookmark.web.dto.PageResponse;

public interface MemberService {

	MemberResponse create(MemberRequest request);

	MemberResponse update(Long id, MemberRequest request);

	MemberResponse changeStatus(Long id, MemberStatus status);

	MemberResponse getById(Long id);

	PageResponse<MemberResponse> list(int page, int size, MemberStatus status);
}
