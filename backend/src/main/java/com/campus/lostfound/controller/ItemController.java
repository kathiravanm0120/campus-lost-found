package com.campus.lostfound.controller;

import com.campus.lostfound.dto.ClaimRequest;
import com.campus.lostfound.dto.ItemRequest;
import com.campus.lostfound.entity.Claim;
import com.campus.lostfound.entity.Item;
import com.campus.lostfound.entity.User;
import com.campus.lostfound.service.ClaimService;
import com.campus.lostfound.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;
    private final ClaimService claimService;

    public ItemController(ItemService itemService, ClaimService claimService) {
        this.itemService = itemService; this.claimService = claimService;
    }

    private User user(Authentication auth) { return (User) auth.getPrincipal(); }

    @GetMapping
    public List<Item> browse(@RequestParam(required = false) String type,
                             @RequestParam(required = false) String q) {
        return itemService.browse(type, q);
    }

    @GetMapping("/{id}")
    public Item get(@PathVariable Long id) { return itemService.get(id); }

    @PostMapping
    public Item create(@Valid @RequestBody ItemRequest req, Authentication auth) {
        return itemService.create(req, user(auth));
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @Valid @RequestBody ItemRequest req, Authentication auth) {
        return itemService.update(id, req, user(auth));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        itemService.delete(id, user(auth));
    }

    @GetMapping("/mine")
    public List<Item> mine(Authentication auth) { return itemService.mine(user(auth)); }

    @PostMapping("/{id}/claims")
    public Claim claim(@PathVariable Long id, @Valid @RequestBody ClaimRequest req, Authentication auth) {
        return claimService.create(id, req, user(auth));
    }

    @GetMapping("/{id}/claims")
    public List<Claim> claims(@PathVariable Long id, Authentication auth) {
        return claimService.forItem(id, user(auth));
    }

    @PutMapping("/{id}/returned")
    public Item returned(@PathVariable Long id, Authentication auth) {
        return claimService.returned(id, user(auth));
    }
}
