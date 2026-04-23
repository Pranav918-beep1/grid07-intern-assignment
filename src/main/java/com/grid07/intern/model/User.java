package com.grid07.intern.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
      private String username;
    private Boolean isPremium = false;
    
  @Column(name="created_at")
    private LocalDateTime createdAt;
    
    public User() {}
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Boolean getIsPremium() { return isPremium; }
    
    public void setIsPremium(Boolean p) { 
        isPremium = p; 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(LocalDateTime ct) { 
        createdAt = ct; 
    }
}
