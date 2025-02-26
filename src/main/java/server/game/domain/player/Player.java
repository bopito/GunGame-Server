package server.game.domain.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import server.game.base.entity.Entity;
import server.game.domain.player.handler.PlayerCollisionHandler;
import server.game.domain.player.handler.PlayerMovementHandler;
import server.game.domain.weapon.Weapon;
import server.game.domain.skill.Skill;
import server.game.domain.player.manager.*;

import java.util.List;
import java.util.Map;

/**
 * Represents a player in the game, managing only its state.
 */
@Getter
@Setter
public class Player extends Entity {
    private int health;
    private int score;
    private int speed;
    private Weapon currentWeapon;
    private Map<String, Boolean> keys;

    @JsonIgnore
    private final PlayerHealthManager healthManager;
    @JsonIgnore
    private final PlayerWeaponManager weaponManager;
    @JsonIgnore
    private final PlayerSkillManager skillManager;
    @JsonIgnore
    private final PlayerMovementHandler movementHandler;
    @JsonIgnore
    private final PlayerCollisionHandler collisionHandler;

    /**
     * Initializes a new player with a predefined set of skills.
     *
     * @param skills The three skills the player starts with.
     */
    public Player(List<Skill> skills) {
        if (skills == null || skills.size() != 3) {
            throw new IllegalArgumentException("A player must start with exactly 3 skills.");
        }
        this.health = 100;
        this.score = 0;
        this.speed = 1;
        this.keys = Map.of("w", false, "a", false, "s", false, "d", false);

        // ✅ 이제 SkillHandler는 Player가 직접 넘겨주지 않고 PlayerSkillManager에서 생성
        this.skillManager = new PlayerSkillManager(this, skills);

        // Initialize managers and handlers
        this.healthManager = new PlayerHealthManager(this);
        this.weaponManager = new PlayerWeaponManager(this);
        this.movementHandler = new PlayerMovementHandler(this);
        this.collisionHandler = new PlayerCollisionHandler(this);
    }

    @Override
    public void update() {
        movementHandler.updateMovement();
    }

    public void takeDamage(int damage) {
        healthManager.takeDamage(damage);
    }

    public void heal(int amount) {
        healthManager.heal(amount);
    }

    public void equipWeapon(Weapon weapon) {
        weaponManager.equipWeapon(weapon);
    }

    public void shootBullet() {
        weaponManager.shootBullet();
    }

    public void useSkill(int skillIndex) {
        skillManager.useSkill(skillIndex);
    }

    public void handleCollision(Entity otherEntity) {
        collisionHandler.handleCollision(otherEntity);
    }
}
