package com.usj.bookmark.web.dto;

import com.usj.bookmark.domain.enums.MemberStatus;
import jakarta.validation.constraints.NotNull;

public class MemberStatusUpdateRequest {

	@NotNull
	private MemberStatus status;

	public MemberStatus getStatus() {
		return status;
	}

	public void setStatus(MemberStatus status) {
		this.status = status;
	}
}
