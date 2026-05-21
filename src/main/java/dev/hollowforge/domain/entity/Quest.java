package dev.hollowforge.domain.entity;

public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private boolean completed;
    private boolean active;

    public Quest(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = false;
        this.active = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public boolean isActive() { return active; }

    public void activate() { this.active = true; }
    public void complete() { this.completed = true; this.active = false; }
}
