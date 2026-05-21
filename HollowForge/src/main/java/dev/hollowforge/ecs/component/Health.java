package dev.hollowforge.ecs.component;

public class Health {
    private int current;
    private int maximum;

    public Health(int maximum) {
        this.current = maximum;
        this.maximum = maximum;
    }

    public int getCurrent() { return current; }
    public int getMaximum() { return maximum; }
    public void setCurrent(int current) { this.current = Math.max(0, Math.min(maximum, current)); }
    public boolean isAlive() { return current > 0; }
}
