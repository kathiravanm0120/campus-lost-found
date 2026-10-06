package com.campus.lostfound.repository;

import com.campus.lostfound.entity.Item;
import com.campus.lostfound.entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatusOrderByIdDesc(ItemStatus status);
    List<Item> findAllByOrderByIdDesc();
    List<Item> findByReporterIdOrderByIdDesc(Long reporterId);
}
