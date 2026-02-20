package edu.eci.arsw.blueprints.dto;

import java.util.List;

/**
 * Data Transfer Object for Blueprint entity.
 */
public class BlueprintDTO {
    private Long id;
    private String author;
    private String name;
    private List<PointDTO> points;

    public BlueprintDTO() {}

    public BlueprintDTO(Long id, String author, String name, List<PointDTO> points) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.points = points;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PointDTO> getPoints() { return points; }
    public void setPoints(List<PointDTO> points) { this.points = points; }
}
