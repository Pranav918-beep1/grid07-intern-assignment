package com.grid07.intern.repository;

import com.grid07.intern.model.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepo extends JpaRepository<Bot, Long> 
{
}
