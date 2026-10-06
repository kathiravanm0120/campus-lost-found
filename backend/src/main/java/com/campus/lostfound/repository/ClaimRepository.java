package com.campus.lostfound.repository;

import com.campus.lostfound.entity.Claim;
import com.campus.lostfound.entity.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByClaimantIdOrderByIdDesc(Long claimantId);
    List<Claim> findByItemIdOrderByIdDesc(Long itemId);
    Optional<Claim> findByIdAndItemReporterId(Long id, Long reporterId);
    boolean existsByItemIdAndClaimantIdAndStatus(Long itemId, Long claimantId, ClaimStatus status);
}
