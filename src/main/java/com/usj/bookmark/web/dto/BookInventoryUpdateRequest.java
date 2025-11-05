package com.usj.bookmark.web.dto;

import jakarta.validation.constraints.NotNull;

public class BookInventoryUpdateRequest {

	@NotNull
	private Integer delta;

	public Integer getDelta() {
		return delta;
	}

	public void setDelta(Integer delta) {
		this.delta = delta;
	}
}
