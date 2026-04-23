package com.grid07.intern.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
      private Long id;
    
    private Long authorId;
      private String content;
    
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    private String authorType;
      private Integer likeCount = 0;
    
    public Post() {}
    
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getAuthorId() { return authorId; }
    
    public void setAuthorId(Long aid) { 
        authorId = aid; 
    }
    
    public String getContent() { return content; }
    public void setContent(String c) { 
        content = c; 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(LocalDateTime ct) { 
        createdAt = ct; 
    }
    
    public String getAuthorType() { return authorType; }
    public void setAuthorType(String at) { 
        authorType = at; 
    }
    
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer lc) { 
        likeCount = lc; 
    }
}
