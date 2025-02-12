package com.gungame.gungame_server.service;/*
 * created by seokhyun on 2025-02-13.
 */

import com.gungame.gungame_server.entity.Player;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlayerService {

    public void updatePlayerState(Player player, Map<String, Object> incomingMessage) {
        if (incomingMessage.containsKey("x")) {
            player.setX(((Number) incomingMessage.get("x")).intValue());
        }
        if (incomingMessage.containsKey("y")) {
            player.setY(((Number) incomingMessage.get("y")).intValue());
        }
        if (incomingMessage.containsKey("angle")) {
            player.setAngle(((Number) incomingMessage.get("angle")).doubleValue());
        }
        if (incomingMessage.containsKey("keys")) {
            player.setKeys((Map<String, Boolean>) incomingMessage.get("keys"));
        }
    }
}
