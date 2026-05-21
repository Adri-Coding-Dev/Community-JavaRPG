package dev.hollowforge.ecs.system;

import dev.hollowforge.ecs.component.Position;
import dev.hollowforge.ecs.component.Velocity;

public class AISystem {

    public void chaseTarget(Position position, Velocity velocity, float targetX, float targetY, float speed) {
        if (position == null || velocity == null) return;
        float dx = targetX - position.getX();
        float dy = targetY - position.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            velocity.setVx((dx / distance) * speed);
            velocity.setVy((dy / distance) * speed);
        }
    }

    public void patrol(Position position, Velocity velocity, float originX, float range, float speed) {
        if (position == null || velocity == null) return;
        float dx = position.getX() - originX;
        if (Math.abs(dx) > range) {
            velocity.setVx(-velocity.getVx());
        }
    }
}
