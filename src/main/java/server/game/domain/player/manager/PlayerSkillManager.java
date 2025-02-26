package server.game.domain.player.manager;

import server.game.domain.player.Player;
import server.game.domain.skill.Skill;
import server.game.domain.skill.handler.SkillHandler;
import server.game.domain.skill.SkillManager;
import java.util.List;

/**
 * Manages the player's skills.
 * Players start with exactly 3 skills and cannot change them.
 */
public class PlayerSkillManager {
    private final Player player;
    private final List<Skill> skills;
    private final SkillHandler skillHandler;

    /**
     * Initializes the PlayerSkillManager with the player's predefined skills.
     *
     * @param player The player instance.
     * @param skills The three skills assigned to the player at the start of the game.
     */
    public PlayerSkillManager(Player player, List<Skill> skills) {
        if (skills == null || skills.size() != 3) {
            throw new IllegalArgumentException("A player must start with exactly 3 skills.");
        }
        this.player = player;
        this.skills = skills;
        this.skillHandler = new SkillHandler(new SkillManager());
    }

    /**
     * Returns the player's predefined skill list.
     */
    public List<Skill> getSkills() {
        return skills;
    }

    /**
     * Uses one of the player's skills.
     *
     * @param skillIndex The index of the skill to be used (0-2).
     */
    public void useSkill(int skillIndex) {
        if (skillIndex < 0 || skillIndex >= skills.size()) {
            System.out.println("[PlayerSkillManager] Invalid skill selection.");
            return;
        }
        skillHandler.useSkill(player, skills.get(skillIndex));
    }
}
