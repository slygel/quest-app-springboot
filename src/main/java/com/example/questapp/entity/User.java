package com.example.questapp.entity;

import javax.persistence.*;

import lombok.Data;

@Table(name = "user")
@Entity
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "username")
    String username;
    String password;
    int avatar;
}
