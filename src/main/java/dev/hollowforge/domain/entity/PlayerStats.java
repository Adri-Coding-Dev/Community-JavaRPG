package dev.hollowforge.domain.entity;

public class PlayerStats {
    private int health;
    private int maxHealth;
    private int level;
    private int experience;
    private int experienceToNextLevel;

    public PlayerStats(int maxHealth) {
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public void addExperience(int xp) {
        experience += xp;
        while (experience >= experienceToNextLevel) {
            experience -= experienceToNextLevel;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        maxHealth += 10;
        health = maxHealth;
        experienceToNextLevel = (int)(experienceToNextLevel * 1.5);
    }
}
