package com.campus.lostfound.service;

import com.campus.lostfound.dto.ItemRequest;
import com.campus.lostfound.entity.*;
import com.campus.lostfound.repository.CategoryRepository;
import com.campus.lostfound.repository.ItemRepository;
import com.campus.lostfound.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository items;
    private final CategoryRepository categories;
    private final UserRepository users;

    public ItemService(ItemRepository items, CategoryRepository categories, UserRepository users) {
        this.items = items; this.categories = categories; this.users = users;
    }

    public List<Item> browse(String type, String q) {
        List<Item> list = items.findByStatusOrderByIdDesc(ItemStatus.ACTIVE);
        return list.stream()
                .filter(i -> type == null || type.isBlank() || i.getType().name().equalsIgnoreCase(type))
                .filter(i -> q == null || q.isBlank() ||
                        (i.getTitle() + " " + i.getDescription() + " " + i.getLocation())
                                .toLowerCase().contains(q.toLowerCase()))
                .toList();
    }

    public Item get(Long id) {
        return items.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found"));
    }

    public Item create(ItemRequest req, User user) {
        Category category = req.categoryId() == null ? null :
                categories.findById(req.categoryId()).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (category == null) throw new IllegalArgumentException("Category is required");

        Item item = new Item();
        item.setTitle(req.title());
        item.setDescription(req.description());
        item.setType(req.type());
        item.setLocation(req.location());
        item.setItemDate(req.itemDate());
        item.setImageUrl(req.imageUrl());
        item.setCategory(category);
        item.setReporter(user);
        item.setStatus(ItemStatus.PENDING);
        return items.save(item);
    }

    public Item update(Long id, ItemRequest req, User user) {
        Item item = get(id);
        if (!item.getReporter().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only edit your own report");
        }
        if (item.getStatus() == ItemStatus.RETURNED) {
            throw new IllegalArgumentException("Returned items cannot be edited");
        }
        Category category = categories.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        item.setTitle(req.title());
        item.setDescription(req.description());
        item.setType(req.type());
        item.setLocation(req.location());
        item.setItemDate(req.itemDate());
        item.setImageUrl(req.imageUrl());
        item.setCategory(category);
        item.setStatus(ItemStatus.PENDING);
        return items.save(item);
    }

    public void delete(Long id, User user) {
        Item item = get(id);
        if (!item.getReporter().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Not allowed");
        }
        items.delete(item);
    }

    public List<Item> mine(User user) {
        return items.findByReporterIdOrderByIdDesc(user.getId());
    }
}
