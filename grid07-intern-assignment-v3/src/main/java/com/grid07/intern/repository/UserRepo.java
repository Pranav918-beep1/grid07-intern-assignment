package com.grid07.intern.repository;

import com.grid07.intern.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
