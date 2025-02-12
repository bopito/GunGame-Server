package com.websocket.test_connect_websocket.controller;/*
 * created by seokhyun on 2025-02-12.
 */

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MatchmakingController {

    private final Map<String, String> playerMatchAssignments = new ConcurrentHashMap<>();

    @PostMapping("/matchmaking")
    public Map<String, Object> assignMatch(@RequestBody Map<String, String> payload) {
        String playerId = payload.get("playerId");

        // Example logic to assign a match ID
        String matchId = "match-1"; // For simplicity, use a fixed match ID
        String team = Math.random() > 0.5 ? "red" : "blue";

        // Save the player's match assignment
        playerMatchAssignments.put(playerId, matchId);

        Map<String, Object> response = new HashMap<>();
        response.put("playerId", playerId);
        response.put("team", team);
        response.put("matchId", matchId);

        System.out.println("Player " + playerId + " assigned to match " + matchId + " on team " + team);
        return response;
    }
}
