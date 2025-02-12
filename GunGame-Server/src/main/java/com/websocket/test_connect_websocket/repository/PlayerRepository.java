package com.websocket.test_connect_websocket.repository;/*
 * created by seokhyun on 2025-02-12.
 */

import com.websocket.test_connect_websocket.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

