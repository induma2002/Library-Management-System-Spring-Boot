package com.usj.bookmark.repository;

import com.usj.bookmark.domain.entity.Loan;
import com.usj.bookmark.domain.enums.LoanStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

	List<Loan> findByMemberIdAndStatusIn(Long memberId, List<LoanStatus> statuses);

	List<Loan> findByStatus(LoanStatus status);

	Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

	Page<Loan> findByMemberId(Long memberId, Pageable pageable);

	List<Loan> findByDueDateBeforeAndStatus(LocalDateTime date, LoanStatus status);

	long countByStatus(LoanStatus status);
}
