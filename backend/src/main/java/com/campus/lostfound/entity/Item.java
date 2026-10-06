package com.campus.lostfound.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "items")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 2000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ItemType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ItemStatus status = ItemStatus.PENDING;
    @Column(nullable = false) private String location;
    @Column(nullable = false) private LocalDate itemDate;
    private String imageUrl;

    @ManyToOne(optional = false) private User reporter;
    @ManyToOne(optional = false) private Category category;

    public Item() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public ItemType getType(){return type;} public void setType(ItemType v){type=v;}
    public ItemStatus getStatus(){return status;} public void setStatus(ItemStatus v){status=v;}
    public String getLocation(){return location;} public void setLocation(String v){location=v;}
    public LocalDate getItemDate(){return itemDate;} public void setItemDate(LocalDate v){itemDate=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public User getReporter(){return reporter;} public void setReporter(User v){reporter=v;}
    public Category getCategory(){return category;} public void setCategory(Category v){category=v;}
}
