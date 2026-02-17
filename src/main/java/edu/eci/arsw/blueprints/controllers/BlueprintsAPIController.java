package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * REST controller for managing blueprint resources.
 * Exposes HTTP endpoints for retrieving, creating, and updating blueprints.
 * Delegates business logic to the BlueprintsServices class.
 */
@RestController
@RequestMapping("/api/v1/blueprints")
public class BlueprintsAPIController {

    /**
     * Service layer for blueprint operations.
     */
    private final BlueprintsServices services;

    /**
     * Constructs the controller with the required service dependency.
     * @param services The BlueprintsServices instance
     */
    public BlueprintsAPIController(BlueprintsServices services) { this.services = services; }

    /**
     * Retrieves all blueprints in the system.
     * @return HTTP 200 with the set of all blueprints
     */
    @GetMapping
    public ResponseEntity<Set<Blueprint>> getAll() {
        return ResponseEntity.ok(services.getAllBlueprints()); // 200 OK
    }

    /**
     * Retrieves all blueprints created by a specific author.
     * @param author The author's name
     * @return HTTP 200 with the set of blueprints, or 404 if none found
     */
    @GetMapping("/{author}")
    public ResponseEntity<?> byAuthor(@PathVariable String author) {
        try {
            return ResponseEntity.ok(services.getBlueprintsByAuthor(author)); // 200 OK
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); // 404 Not Found
        }
    }

    /**
     * Retrieves a specific blueprint by author and blueprint name.
     * @param author The author's name
     * @param bpname The blueprint's name
     * @return HTTP 200 with the blueprint, or 404 if not found
     */
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            return ResponseEntity.ok(services.getBlueprint(author, bpname)); // 200 OK
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); // 404 Not Found
        }
    }

    /**
     * Creates a new blueprint with the provided data.
     * @param req The request body containing author, name, and points
     * @return HTTP 201 if created, or 403 if a blueprint with the same key already exists
     */
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())); // 400 Bad Request
        }
    }

    /**
     * Adds a new point to an existing blueprint.
     * @param author The author's name
     * @param bpname The blueprint's name
     * @param p The point to add
     * @return HTTP 202 if accepted, or 404 if the blueprint is not found
     */
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<?> addPoint(@PathVariable String author, @PathVariable String bpname,
                                      @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // 202 Accepted
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); // 404 Not Found
        }
    }

    /**
     * Request body model for creating a new blueprint.
     * Encapsulates and validates the required fields: author, name, and points.
     */
    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }
}
