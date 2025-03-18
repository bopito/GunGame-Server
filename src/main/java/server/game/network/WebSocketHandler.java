package server.game.network;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import server.game.domain.box.Box;
import server.game.domain.box.BoxManager;
import server.game.domain.player.Player;
import server.game.domain.player.PlayerManager;
import server.game.domain.skill.Skill;
import server.game.domain.skill.effects.HealEffect;
import server.game.core.GameEngine;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private final PlayerManager playerManager;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON serialization
    private final GameEngine gameEngine;
    private final BoxManager boxManager;

    public WebSocketHandler(SessionManager sessionManager, PlayerManager playerManager, GameEngine gameEngine, BoxManager boxManager) {
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
        this.gameEngine = gameEngine;
        this.boxManager = boxManager;
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
            gameEngine.addPlayerToGame(newPlayer); // ✅ 새로운 플레이어 추가
        } else {
            System.err.println("[WebSocket] Duplicate player ID detected: " + newPlayer.getId());
        }

        // Store the player ID with the session
        sessionManager.addSession(newPlayer.getId(), session);
        gameEngine.onPlayerJoined(); // ✅ Notify GameEngine that a player has joined

        // Send player ID to the client
        try {
            Map<String, Object> response = Map.of(
                    "type", "assign_id",
                    "playerId", newPlayer.getId(),
                    "equippedWeapon", newPlayer.getCurrentWeapon().getName()
            );
            String json = objectMapper.writeValueAsString(response);

            synchronized (session){
                session.sendMessage(new TextMessage(json));
            }

            System.out.println("[WebSocket] Assigned player ID: " + newPlayer.getId());
            System.out.println("[WebSocket] Assigned default weapon: " + newPlayer.getCurrentWeapon().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<Box> existingBoxes = boxManager.getActiveBoxes();
            System.out.println("[WebSocket] Sending existing boxes. Count: " + existingBoxes.size());

            for (Box box : existingBoxes) {
                Map<String, Object> boxData = Map.of(
                        "type", "box_spawn",
                        "x", box.getX(),
                        "z", box.getZ(),
                        "weapon", box.getDroppedWeapon(),
                        "hp", box.getHp()
                );
                String jsonMessage = objectMapper.writeValueAsString(boxData);

                synchronized (session){
                    session.sendMessage(new TextMessage(jsonMessage));
                }

                System.out.println("[WebSocket] Sent box: " + jsonMessage);

            }
        } catch (Exception e) {
            System.err.println("[WebSocket] Error sending existing boxes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            Map<String, Object> data = objectMapper.readValue(payload, Map.class);

            // Retrieve the player associated with the session
            Player player = playerManager.getPlayerBySession(sessionManager, session);
            if (player != null) {
                playerManager.updatePlayerState(player, message);
            }else{
                System.err.println("[WebSocket] Received message from unknown session: " + session.getId());
                return;
            }

            String action = (String) data.get("action");

            switch (action) {
                //player weapon action
                case "equip_weapon":
                    String weaponId = (String) data.get("weaponId");
                    gameEngine.queueWeaponPickup(player, weaponId);
                    break;

                case "shoot":
                    gameEngine.queuePlayerShoot(player);
                    break;

                case "reload":
                    gameEngine.queuePlayerReload(player);
                    break;

                //player skill actoin
                case "skill":
                    System.out.println("player do skill logic");
                    break;

                //player game status action
                case "leave":
                    System.out.println("player leave logic");
                    break;

                //none action
                default:
                    break;
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

        gameEngine.onPlayerLeft(); // ✅ Notify GameEngine when a player leaves

    }
}
