package com.campus.lostfound.controller;

import com.campus.lostfound.entity.Claim;
import com.campus.lostfound.entity.Item;
import com.campus.lostfound.entity.User;
import com.campus.lostfound.service.ClaimService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    private final ClaimService service;
    public ClaimController(ClaimService service) { this.service = service; }

    private User user(Authentication auth) { return (User) auth.getPrincipal(); }

    @GetMapping("/my")
    public List<Claim> my(Authentication auth) { return service.my(user(auth)); }

    @PutMapping("/{id}/approve")
    public Claim approve(@PathVariable Long id, Authentication auth) {
        return service.approve(id, user(auth));
    }

    @PutMapping("/{id}/reject")
    public Claim reject(@PathVariable Long id, Authentication auth) {
        return service.reject(id, user(auth));
    }
}
