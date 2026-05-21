package dev.hollowforge.ecs.component;

public class Damage {
    private int amount;

    public Damage(int amount) {
        this.amount = amount;
    }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
