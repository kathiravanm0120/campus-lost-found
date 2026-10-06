package com.campus.lostfound.controller;

import com.campus.lostfound.entity.Item;
import com.campus.lostfound.entity.ItemStatus;
import com.campus.lostfound.repository.ItemRepository;
import com.campus.lostfound.entity.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ItemRepository items;
    public AdminController(ItemRepository items) { this.items = items; }

    @GetMapping("/items/pending")
    public List<Item> pending() {
        return items.findByStatusOrderByIdDesc(ItemStatus.PENDING);
    }

    @PutMapping("/items/{id}/approve")
    public Item approve(@PathVariable Long id) {
        Item item = items.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setStatus(ItemStatus.ACTIVE);
        return items.save(item);
    }

    @PutMapping("/items/{id}/reject")
    public Item reject(@PathVariable Long id) {
        Item item = items.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setStatus(ItemStatus.REJECTED);
        return items.save(item);
    }

    @DeleteMapping("/items/{id}")
    public void delete(@PathVariable Long id) {
        items.deleteById(id);
    }
}
