package com.usj.bookmark.service.impl;

import com.usj.bookmark.domain.entity.Fine;
import com.usj.bookmark.domain.entity.Loan;
import com.usj.bookmark.domain.enums.FineStatus;
import com.usj.bookmark.exception.ResourceNotFoundException;
import com.usj.bookmark.repository.FineRepository;
import com.usj.bookmark.service.FineService;
import com.usj.bookmark.web.dto.FineResponse;
import com.usj.bookmark.web.dto.PageResponse;
import com.usj.bookmark.web.mapper.FineMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FineServiceImpl implements FineService {

	private final FineRepository fineRepository;

	public FineServiceImpl(FineRepository fineRepository) {
		this.fineRepository = fineRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public FineResponse getById(Long id) {
		return FineMapper.toResponse(findFineOrThrow(id));
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<FineResponse> listAll(int page, int size, FineStatus status) {
		Page<Fine> result = status != null
			? fineRepository.findByStatus(status, PageRequest.of(page, size))
			: fineRepository.findAll(PageRequest.of(page, size));
		return new PageResponse<>(
			result.map(FineMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages());
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponse<FineResponse> listForMember(Long memberId, int page, int size, FineStatus status) {
		Page<Fine> result;
		if (status != null) {
			result = fineRepository.findByMemberIdAndStatus(memberId, status, PageRequest.of(page, size));
		} else {
			result = fineRepository.findByMemberId(memberId, PageRequest.of(page, size));
		}
		return new PageResponse<>(
			result.map(FineMapper::toResponse).getContent(),
			result.getNumber(),
			result.getSize(),
			result.getTotalElements(),
			result.getTotalPages());
	}

	@Override
	public FineResponse settleFine(Long fineId, boolean waive, LocalDateTime settledAt) {
		Fine fine = findFineOrThrow(fineId);
		fine.setStatus(waive ? FineStatus.WAIVED : FineStatus.PAID);
		fine.setSettledAt(settledAt != null ? settledAt : LocalDateTime.now());
		Loan loan = fine.getLoan();
		if (loan != null) {
			loan.setFineClosed(true);
		}
		return FineMapper.toResponse(fine);
	}

	@Override
	public void recordFine(Loan loan, BigDecimal amount, String description) {
		Fine fine = new Fine();
		fine.setLoan(loan);
		fine.setMember(loan.getMember());
		fine.setAmount(amount);
		fine.setStatus(FineStatus.PENDING);
		fine.setIssuedAt(LocalDateTime.now());
		fine.setDescription(description);
		fineRepository.save(fine);
	}

	private Fine findFineOrThrow(Long id) {
		return fineRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Fine not found with id " + id));
	}
}
