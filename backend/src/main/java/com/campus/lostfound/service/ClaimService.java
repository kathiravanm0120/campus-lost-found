package com.campus.lostfound.service;

import com.campus.lostfound.dto.ClaimRequest;
import com.campus.lostfound.entity.*;
import com.campus.lostfound.repository.ClaimRepository;
import com.campus.lostfound.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimService {
    private final ClaimRepository claims;
    private final ItemRepository items;

    public ClaimService(ClaimRepository claims, ItemRepository items) {
        this.claims = claims; this.items = items;
    }

    public Claim create(Long itemId, ClaimRequest req, User claimant) {
        Item item = items.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (item.getType() != ItemType.FOUND) throw new IllegalArgumentException("Only found items can be claimed");
        if (!item.getStatus().equals(ItemStatus.ACTIVE)) throw new IllegalArgumentException("Item is not available for claiming");
        if (item.getReporter().getId().equals(claimant.getId())) throw new IllegalArgumentException("You cannot claim your own item");
        if (claims.existsByItemIdAndClaimantIdAndStatus(itemId, claimant.getId(), ClaimStatus.PENDING)) {
            throw new IllegalArgumentException("You already have a pending claim");
        }

        Claim claim = new Claim();
        claim.setItem(item);
        claim.setClaimant(claimant);
        claim.setMessage(req.message());
        claim.setVerificationAnswer(req.verificationAnswer());
        claim.setStatus(ClaimStatus.PENDING);
        item.setStatus(ItemStatus.CLAIM_PENDING);
        items.save(item);
        return claims.save(claim);
    }

    public List<Claim> my(User user) {
        return claims.findByClaimantIdOrderByIdDesc(user.getId());
    }

    public List<Claim> forItem(Long itemId, User user) {
        Item item = items.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!item.getReporter().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Not allowed");
        }
        return claims.findByItemIdOrderByIdDesc(itemId);
    }

    public Claim approve(Long id, User user) {
        Claim claim = claims.findByIdAndItemReporterId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Claim not found or not allowed"));
        claim.setStatus(ClaimStatus.APPROVED);
        Item item = claim.getItem();
        item.setStatus(ItemStatus.CLAIM_APPROVED);
        items.save(item);
        return claims.save(claim);
    }

    public Claim reject(Long id, User user) {
        Claim claim = claims.findByIdAndItemReporterId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Claim not found or not allowed"));
        claim.setStatus(ClaimStatus.REJECTED);
        Item item = claim.getItem();
        item.setStatus(ItemStatus.ACTIVE);
        items.save(item);
        return claims.save(claim);
    }

    public Item returned(Long itemId, User user) {
        Item item = items.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!item.getReporter().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Not allowed");
        }
        item.setStatus(ItemStatus.RETURNED);
        return items.save(item);
    }
}
