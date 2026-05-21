package dev.hollowforge.ecs.core;

import java.util.Arrays;

public class EntityArray {
    private int[] entities;
    private int size;

    public EntityArray(int initialCapacity) {
        this.entities = new int[initialCapacity];
        this.size = 0;
    }

    public void add(int entityId) {
        if (size >= entities.length) {
            entities = Arrays.copyOf(entities, entities.length * 2);
        }
        entities[size++] = entityId;
    }

    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return entities[index];
    }

    public int size() { return size; }

    public void clear() { size = 0; }

    public int[] getRawData() { return entities; }
}
