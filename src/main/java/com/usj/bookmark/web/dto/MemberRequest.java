package com.usj.bookmark.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberRequest {

	@NotBlank
	@Size(max = 40)
	private String membershipId;

	@NotBlank
	@Size(max = 120)
	private String fullName;

	@NotBlank
	@Email
	private String email;

	@Pattern(regexp = "^[-0-9+() ]{7,25}$", message = "invalid phone number")
	private String phoneNumber;

	@Size(max = 200)
	private String address;

	public String getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
