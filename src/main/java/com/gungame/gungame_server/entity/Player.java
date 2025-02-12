package com.gungame.gungame_server.entity;/*
 * created by seokhyun on 2025-02-12.
 */

import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerId;
    private String team;

    // Getters and setters
}


