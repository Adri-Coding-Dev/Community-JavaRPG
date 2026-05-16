package dev.hollowforge.model;

import java.util.Objects;

/**
 * Representa un contribuidor de GitHub con sus datos básicos.
 */
public class Contributor {

    private final String nombre;
    private final String avatarUrl;
    private final String htmlUrl;

    public Contributor(String nombre, String avatarUrl, String htmlUrl) {
        this.nombre = nombre;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contributor that = (Contributor) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}