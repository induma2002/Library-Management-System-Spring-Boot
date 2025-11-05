package com.usj.bookmark.domain.entity;

import com.usj.bookmark.domain.enums.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "members", uniqueConstraints = {
		@UniqueConstraint(name = "uk_members_code", columnNames = "membership_id"),
		@UniqueConstraint(name = "uk_members_email", columnNames = "email")
})
public class Member extends BaseEntity {

	@Column(name = "membership_id", nullable = false, length = 40)
	private String membershipId;

	@Column(nullable = false, length = 120)
	private String fullName;

	@Column(nullable = false, length = 120)
	private String email;

	@Column(length = 25)
	private String phoneNumber;

	@Column(length = 200)
	private String address;

	@Column(nullable = false)
	private LocalDate joinedOn = LocalDate.now();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private MemberStatus status = MemberStatus.ACTIVE;

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

	public LocalDate getJoinedOn() {
		return joinedOn;
	}

	public void setJoinedOn(LocalDate joinedOn) {
		this.joinedOn = joinedOn;
	}

	public MemberStatus getStatus() {
		return status;
	}

	public void setStatus(MemberStatus status) {
		this.status = status;
	}
}
