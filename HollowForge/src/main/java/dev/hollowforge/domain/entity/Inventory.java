    package dev.hollowforge.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<String> items;
    private final int maxSlots;

    public Inventory(int maxSlots) {
        this.items = new ArrayList<>();
        this.maxSlots = maxSlots;
    }

    public boolean addItem(String item) {
        if (items.size() >= maxSlots) return false;
        return items.add(item);
    }

    public boolean removeItem(String item) {
        return items.remove(item);
    }

    public boolean hasItem(String item) {
        return items.contains(item);
    }

    public List<String> getItems() {
        return new ArrayList<>(items);
    }

    public int getSlotCount() { return items.size(); }
    public int getMaxSlots() { return maxSlots; }
}
