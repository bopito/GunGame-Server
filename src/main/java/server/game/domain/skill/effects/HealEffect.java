package server.game.domain.skill.effects;/*
 * created by seokhyun on 2025-02-26.
 */


import server.game.domain.skill.SkillEffect;
import server.game.domain.player.Player;

public class HealEffect implements SkillEffect {
    private final int healAmount;

    public HealEffect(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void applyEffect(Player player) {
        player.heal(healAmount);
        System.out.println("[Skill] Heal used! Player HP: " + player.getHealth());
    }
}
