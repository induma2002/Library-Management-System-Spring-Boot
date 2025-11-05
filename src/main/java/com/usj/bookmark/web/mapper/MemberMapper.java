package com.usj.bookmark.web.mapper;

import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.web.dto.MemberRequest;
import com.usj.bookmark.web.dto.MemberResponse;

public final class MemberMapper {

	private MemberMapper() {
	}

	public static void updateEntity(Member member, MemberRequest request) {
		member.setMembershipId(request.getMembershipId());
		member.setFullName(request.getFullName());
		member.setEmail(request.getEmail());
		member.setPhoneNumber(request.getPhoneNumber());
		member.setAddress(request.getAddress());
	}

	public static MemberResponse toResponse(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getMembershipId(),
			member.getFullName(),
			member.getEmail(),
			member.getPhoneNumber(),
			member.getAddress(),
			member.getJoinedOn(),
			member.getStatus(),
			member.getCreatedAt(),
			member.getUpdatedAt());
	}
}
