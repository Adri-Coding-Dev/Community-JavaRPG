package dev.hollowforge.domain.usecase;

import dev.hollowforge.domain.entity.PlayerStats;

public class ApplyDamage {

    public void execute(PlayerStats target, int damage) {
        if (target == null) throw new IllegalArgumentException("Target cannot be null");
        if (damage < 0) throw new IllegalArgumentException("Damage cannot be negative");
        target.takeDamage(damage);
    }

    public boolean isPlayerDead(PlayerStats target) {
        return target.getHealth() <= 0;
    }
}
