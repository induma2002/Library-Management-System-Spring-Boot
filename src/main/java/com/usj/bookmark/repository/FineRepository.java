package com.usj.bookmark.repository;

import com.usj.bookmark.domain.entity.Fine;
import com.usj.bookmark.domain.enums.FineStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {

	List<Fine> findByMemberIdAndStatus(Long memberId, FineStatus status);

	Page<Fine> findByStatus(FineStatus status, Pageable pageable);

	Page<Fine> findByMemberIdAndStatus(Long memberId, FineStatus status, Pageable pageable);

	Page<Fine> findByMemberId(Long memberId, Pageable pageable);

	long countByStatus(FineStatus status);
}
