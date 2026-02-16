package edu.eci.arsw.blueprints.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a blueprint composed of an author, a name, and a list of points.
 * Allows access and modification of blueprint data, as well as comparison by author and name.
 */
public class Blueprint {

    private String author;
    private String name;
    private final List<Point> points = new ArrayList<>();

    /**
     * Creates a new blueprint with the specified author, name, and list of points.
     * @param author Name of the author
     * @param name Name of the blueprint
     * @param pts List of points that make up the blueprint
     */
    public Blueprint(String author, String name, List<Point> pts) {
        this.author = author;
        this.name = name;
        if (pts != null) points.addAll(pts);
    }

    /**
     * Gets the name of the blueprint's author.
     * @return Author's name
     */
    public String getAuthor() { return author; }

    /**
     * Gets the name of the blueprint.
     * @return Blueprint name
     */
    public String getName() { return name; }

    /**
     * Gets the list of points that make up the blueprint.
     * @return List of points
     */
    public List<Point> getPoints() { return Collections.unmodifiableList(points); }

    /**
     * Adds a point to the blueprint's list of points.
     * @param p Point to add
     */
    public void addPoint(Point p) { points.add(p); }

    /**
     * Compares this blueprint with another object to determine if they are equal.
     * Two blueprints are equal if they have the same author and name.
     * @param o Object to compare
     * @return true if both blueprints have the same author and name, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blueprint bp)) return false;
        return Objects.equals(author, bp.author) && Objects.equals(name, bp.name);
    }

    /**
     * Generates the hash code for the blueprint based on the author and name.
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }
}
