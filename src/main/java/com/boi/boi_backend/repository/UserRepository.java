package com.boi.boi_backend.repository;

import com.boi.boi_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
      User findByUsername(String username);
      User findById(long id);
}