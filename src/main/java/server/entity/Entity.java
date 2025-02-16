package server.entity;

import java.util.UUID;

//
// Entity is an ABSTRACT class, do not intantiate this directly, use it as a model to extend
//

public abstract class Entity {
    protected String id;
    protected int x, y, z;
    protected double angle;

    public Entity() {
        this.id = UUID.randomUUID().toString();
        this.x = 0;
        this.y = 5;
        this.z = 0;
        this.angle = 0;
    }

    public String getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public double getAngle() { return angle; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setZ(int z) { this.z = z; }
    public void setAngle(double angle) { this.angle = angle; }

    public abstract void update(); // subclasses define update() logic
}
