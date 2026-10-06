package com.campus.lostfound.dto;

import com.campus.lostfound.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ItemRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull ItemType type,
        @NotBlank String location,
        @NotNull LocalDate itemDate,
        Long categoryId,
        String imageUrl
) {}
