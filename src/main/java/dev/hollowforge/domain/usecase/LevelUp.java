package dev.hollowforge.domain.usecase;

import dev.hollowforge.domain.entity.PlayerStats;

public class LevelUp {

    public void addExperience(PlayerStats player, int xp) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (xp < 0) throw new IllegalArgumentException("XP cannot be negative");
        player.addExperience(xp);
    }

    public int getCurrentLevel(PlayerStats player) {
        return player.getLevel();
    }
}
