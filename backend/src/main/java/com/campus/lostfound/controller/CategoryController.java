package com.campus.lostfound.controller;

import com.campus.lostfound.entity.Category;
import com.campus.lostfound.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categories;
    public CategoryController(CategoryRepository categories) { this.categories = categories; }

    @GetMapping
    public List<Category> all() { return categories.findAll(); }
}
