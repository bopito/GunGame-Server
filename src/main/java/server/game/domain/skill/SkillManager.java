package server.game.domain.skill;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages skills in the game.
 */
public class SkillManager {
    private final Map<String, Skill> skills = new HashMap<>();

    public void addSkill(Skill skill) {
        skills.put(skill.getId(), skill);
    }

    public void removeSkill(String id) {
        skills.remove(id);
    }

    public Skill getSkill(String id) {
        return skills.get(id);
    }
}
