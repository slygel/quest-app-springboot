package com.example.questapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questapp.entity.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

     @Query("SELECT u FROM User u WHERE u.username = ?1")
     User findByUsername(String username);

}
