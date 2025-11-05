package com.usj.bookmark.repository;

import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.domain.enums.MemberStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByMembershipId(String membershipId);

	Optional<Member> findByEmail(String email);

	List<Member> findByStatus(MemberStatus status);

	Page<Member> findByStatus(MemberStatus status, Pageable pageable);

	long countByStatus(MemberStatus status);
}
