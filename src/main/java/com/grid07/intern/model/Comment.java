package com.grid07.intern.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment 
{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
      private Long postId;
    private Long authorId;
        private String content;
    private Integer depthLevel = 0;
    
    @Column(name="created_at")
      private LocalDateTime createdAt;
    
      private String authorType;
    
    public Comment() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getPostId() { 
        return postId; 
    }
    public void setPostId(Long pid) { 
        postId = pid; 
    }
    
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long aid) { 
        authorId = aid; 
    }
    
    public String getContent() { return content; }
    
    public void setContent(String c) { 
        content = c; 
    }
    
    public Integer getDepthLevel() { return depthLevel; }
    public void setDepthLevel(Integer dl) { 
        depthLevel = dl; 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(LocalDateTime ct) { 
        createdAt = ct; 
    }
    
    public String getAuthorType() { return authorType; }
    
    public void setAuthorType(String at) { 
        authorType = at; 
    }
}
