package com.campus.lostfound.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    public Category() {}
    public Category(Long id, String name){this.id=id; this.name=name;}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
}
