package server.game.domain.skill;/*
 * created by seokhyun on 2025-02-26.
 */

import server.game.domain.player.Player;

public interface SkillEffect {
    void applyEffect(Player player);
}
