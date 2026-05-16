// Fichero: Contributor.java
package dev.hollowforge.model;

import java.util.Objects;

/**
 * Representa un contribuidor de GitHub con sus datos básicos.
 * Es un modelo de datos inmutable (sus campos son finales).
 */
public class Contributor {

    private final String nombre;
    private final String avatarUrl;
    private final String htmlUrl;
    private final int contribuciones;

    /**
     * Constructor.
     *
     * @param nombre         nombre de usuario en GitHub (login)
     * @param avatarUrl      URL de la imagen de avatar
     * @param htmlUrl        URL del perfil en GitHub
     * @param contribuciones número de contribuciones al repositorio
     */
    public Contributor(String nombre, String avatarUrl, String htmlUrl, int contribuciones) {
        this.nombre = nombre;
        this.avatarUrl = avatarUrl;
        this.htmlUrl = htmlUrl;
        this.contribuciones = contribuciones;
    }

    // Getters (no hay setters porque el objeto es inmutable)
    public String getNombre() {
        return nombre;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public int getContribuciones(){
        return contribuciones;
    }

    /**
     * Dos contribuidores se consideran iguales si tienen el mismo nombre (login).
     * Esto es útil para evitar duplicados en colecciones.
     *
     * @param o objeto a comparar
     * @return true si son iguales según el nombre
     */
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
        return nombre + "(" + contribuciones + " contribuciones al proyecto)";
    }
}