package server.game.domain.skill.handler;

import server.game.domain.skill.Skill;
import server.game.domain.skill.SkillManager;
import server.game.domain.player.Player;

/**
 * Handles actions related to skills.
 */
public class SkillHandler {
    private final SkillManager skillManager;

    public SkillHandler(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    public void useSkill(Player player, Skill skill) {
        if (skill.canUse()) {
            skill.use(player);
        } else {
            System.out.println("[Skill] Skill is on cooldown!");
        }
    }

    public void useSkillById(Player player, String skillId) {
        Skill skill = skillManager.getSkill(skillId);
        if (skill != null) {
            useSkill(player, skill);
        } else {
            System.out.println("[SkillHandler] Skill ID not found: " + skillId);
        }
    }
}
