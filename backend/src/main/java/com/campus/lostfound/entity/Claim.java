package com.campus.lostfound.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) private Item item;
    @ManyToOne(optional = false) private User claimant;
    @Column(nullable = false, length = 2000) private String message;
    @Column(length = 2000) private String verificationAnswer;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ClaimStatus status = ClaimStatus.PENDING;
    @Column(nullable = false) private LocalDateTime createdAt = LocalDateTime.now();

    public Claim() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Item getItem(){return item;} public void setItem(Item v){item=v;}
    public User getClaimant(){return claimant;} public void setClaimant(User v){claimant=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public String getVerificationAnswer(){return verificationAnswer;} public void setVerificationAnswer(String v){verificationAnswer=v;}
    public ClaimStatus getStatus(){return status;} public void setStatus(ClaimStatus v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
