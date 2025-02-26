package server.game.domain.skill;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import server.game.domain.player.Player;

/**
 * Represents a skill in the game.
 */
@Getter
@Setter
public class Skill {
    private final String id;
    private final String name;
    private final int coolDown;
    private long lastUsedTime;
    private final SkillEffect effect;

    public Skill(String name, int coolDown, SkillEffect effect) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.coolDown = coolDown;
        this.effect = effect;
        this.lastUsedTime = 0;
    }

    public boolean canUse() {
        return System.currentTimeMillis() - lastUsedTime >= coolDown;
    }

    public void use(Player player) {
        if (canUse()) {
            lastUsedTime = System.currentTimeMillis();
            effect.applyEffect(player);
            System.out.println("[Skill] Used skill: " + name);
        } else {
            System.out.println("[Skill] Cooldown not finished!");
        }
    }
}
