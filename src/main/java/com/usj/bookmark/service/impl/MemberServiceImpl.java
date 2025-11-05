package com.usj.bookmark.service.impl;

import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.exception.BusinessRuleException;
import com.usj.bookmark.exception.ResourceNotFoundException;
import com.usj.bookmark.repository.MemberRepository;
import com.usj.bookmark.service.MemberService;
import com.usj.bookmark.web.dto.MemberRequest;
import com.usj.bookmark.web.dto.MemberResponse;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.mapper.MemberMapper;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	public MemberServiceImpl(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public MemberResponse create(MemberRequest request) {
		ensureMembershipUnique(null, request.getMembershipId(), request.getEmail());
		Member member = new Member();
		MemberMapper.updateEntity(member, request);
		Member saved = memberRepository.save(member);
		return MemberMapper.toResponse(saved);
	}

	@Override
	public MemberResponse update(Long id, MemberRequest request) {
		Member member = findMemberOrThrow(id);
		ensureMembershipUnique(id, request.getMembershipId(), request.getEmail());
		MemberMapper.updateEntity(member, request);
		return MemberMapper.toResponse(member);
	}

	@Override
	public MemberResponse changeStatus(Long id, MemberStatus status) {
		Member member = findMemberOrThrow(id);
		member.setStatus(status);
		return MemberMapper.toResponse(member);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberResponse getById(Long id) {
		return MemberMapper.toResponse(findMemberOrThrow(id));
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<MemberResponse> list(int page, int size, MemberStatus status) {
		Page<Member> pageResult;
		if (status != null) {
			pageResult = memberRepository.findByStatus(status, PageRequest.of(page, size));
		} else {
			pageResult = memberRepository.findAll(PageRequest.of(page, size));
		}
		return new PageResponse<>(
			pageResult.map(MemberMapper::toResponse).getContent(),
			pageResult.getNumber(),
			pageResult.getSize(),
			pageResult.getTotalElements(),
			pageResult.getTotalPages());
	}

	private Member findMemberOrThrow(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + id));
	}

	private void ensureMembershipUnique(Long currentId, String membershipId, String email) {
		Optional<Member> existingByMembership = memberRepository.findByMembershipId(membershipId);
		if (existingByMembership.isPresent() && !existingByMembership.get().getId().equals(currentId)) {
			throw new BusinessRuleException("Membership id already in use");
		}
		Optional<Member> existingByEmail = memberRepository.findByEmail(email);
		if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(currentId)) {
			throw new BusinessRuleException("Email already registered");
		}
	}
}
