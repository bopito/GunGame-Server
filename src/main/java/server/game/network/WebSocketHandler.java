package server.game.network;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import server.game.domain.player.Player;
import server.game.domain.player.PlayerManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import server.game.domain.skill.Skill;
import server.game.domain.skill.SkillEffect;
import server.game.domain.skill.effects.HealEffect;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final PlayerManager playerManager;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON serialization

    public WebSocketHandler(SessionManager sessionManager, PlayerManager playerManager) {
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket connection established: " + session.getId());

        //just for test skill
        Skill skill1 = new Skill("n1", 1, new HealEffect(1));
        Skill skill2 = new Skill("n2", 2, new HealEffect(2));
        Skill skill3 = new Skill("n3", 3, new HealEffect(3));

        List<Skill> testSkills = new ArrayList<>();
        testSkills.add(skill1);
        testSkills.add(skill2);
        testSkills.add(skill3);

        // Generate a new player and add it to the game
        Player newPlayer = new Player(testSkills);

        // Ensure the player is not already in the manager
        if (playerManager.getPlayer(newPlayer.getId()) == null) {
            playerManager.addPlayer(newPlayer);
        } else {
            System.err.println("[WebSocket] Duplicate player ID detected: " + newPlayer.getId());
        }

        // Store the player ID with the session
        sessionManager.addSession(newPlayer.getId(), session);

        // Send player ID to the client
        try {
            Map<String, Object> response = Map.of(
                    "type", "assign_id",
                    "playerId", newPlayer.getId()
            );
            String json = objectMapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(json));

            System.out.println("[WebSocket] Assigned player ID: " + newPlayer.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // Find the player associated with this session
            Player player = playerManager.getPlayerBySession(sessionManager, session);
            if (player != null) {
                playerManager.updatePlayerState(player, message);
            } else {
                System.err.println("[WebSocket] Received message from unknown session: " + session.getId());
            }
        } catch (Exception e) {
            System.err.println("[WebSocket] Error processing message from session: " + session.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket connection closed: " + session.getId() + " with status: " + status);

        // Find and remove the player associated with this session
        Player player = playerManager.getPlayerBySession(sessionManager, session);
        if (player != null) {
            System.out.println("[WebSocket] Removing player: " + player.getId());
            playerManager.removePlayer(player.getId());
            sessionManager.removeSession(player.getId()); // Also remove session mapping
        } else {
            System.err.println("[WebSocket] No player found for session: " + session.getId());
        }
    }
}
