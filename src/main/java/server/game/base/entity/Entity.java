package server.game.base.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

//
// Entity is an ABSTRACT class, do not intantiate this directly, use it as a model to extend
//

@Getter
@Setter
public abstract class Entity {
    protected String id;
    protected double x, y, z;
    protected double angle;

    public Entity() {
        this.id = UUID.randomUUID().toString();
        this.x = 0;
        this.y = 5;
        this.z = 0;
        this.angle = 0;
    }

    public abstract void update(); // subclasses define update() logic
}
