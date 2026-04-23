package com.grid07.intern.repository;

import com.grid07.intern.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> 
{
}
