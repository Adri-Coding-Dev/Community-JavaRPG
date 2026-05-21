package dev.hollowforge.ecs.component;

public class Velocity {
    private float vx;
    private float vy;

    public Velocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public float getVx() { return vx; }
    public float getVy() { return vy; }
    public void setVx(float vx) { this.vx = vx; }
    public void setVy(float vy) { this.vy = vy; }
}
