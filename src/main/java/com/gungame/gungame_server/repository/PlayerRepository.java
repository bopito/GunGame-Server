package com.gungame.gungame_server.repository;/*
 * created by seokhyun on 2025-02-12.
 */

import com.gungame.gungame_server.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

