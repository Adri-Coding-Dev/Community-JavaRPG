package dev.hollowforge.ecs.core;

public class ComponentChunk<T> {
    private final Object[] components;
    private final int capacity;
    private int count;

    public ComponentChunk(int capacity) {
        this.capacity = capacity;
        this.components = new Object[capacity];
        this.count = 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= count) return null;
        return (T) components[index];
    }

    public void set(int index, T component) {
        if (index < 0 || index >= capacity) return;
        if (components[index] == null) count++;
        components[index] = component;
    }

    public int getCount() { return count; }

    public int getCapacity() { return capacity; }

    public void remove(int index) {
        if (index < 0 || index >= capacity) return;
        if (components[index] != null) {
            components[index] = null;
            count--;
        }
    }
}
