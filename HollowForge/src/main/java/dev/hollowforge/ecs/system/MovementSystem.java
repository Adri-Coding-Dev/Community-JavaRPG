package dev.hollowforge.ecs.system;

import dev.hollowforge.ecs.component.Position;
import dev.hollowforge.ecs.component.Velocity;

public class MovementSystem {

    public void update(Position position, Velocity velocity, float delta) {
        if (position == null || velocity == null) return;
        position.translate(velocity.getVx() * delta, velocity.getVy() * delta);
    }
}
