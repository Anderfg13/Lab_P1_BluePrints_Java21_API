package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service layer for blueprint operations.
 * Acts as an intermediary between the controller and the persistence layer, and applies filters to blueprints when needed.
 */
@Service
public class BlueprintsServices {

    /**
     * Persistence layer for storing and retrieving blueprints.
     */
    private final BlueprintPersistence persistence;

    /**
     * Filter for processing blueprints before returning them to the client.
     */
    private final BlueprintsFilter filter;

    /**
     * Constructs the service with the required persistence and filter dependencies.
     * @param persistence The persistence implementation for blueprints
     * @param filter The filter to apply to blueprints
     */
    public BlueprintsServices(BlueprintPersistence persistence, BlueprintsFilter filter) {
        this.persistence = persistence;
        this.filter = filter;
    }

    /**
     * Adds a new blueprint to the system.
     * @param bp The blueprint to add
     * @throws BlueprintPersistenceException if a blueprint with the same key already exists
     */
    @Transactional
    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        persistence.saveBlueprint(bp);
    }

    /**
     * Retrieves all blueprints stored in the system.
     * @return A set of all blueprints
     */
    public Set<Blueprint> getAllBlueprints() {
        // Mantener retorno de entidades, el mapeo a DTO se hace en el controlador
        return persistence.getAllBlueprints();
    }

    /**
     * Retrieves all blueprints created by a specific author.
     * @param author The author's name
     * @return A set of blueprints by the author
     * @throws BlueprintNotFoundException if no blueprints are found for the author
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        // Mantener retorno de entidades, el mapeo a DTO se hace en el controlador
        return persistence.getBlueprintsByAuthor(author);
    }

    /**
     * Retrieves a specific blueprint by author and name, applying the configured filter before returning it.
     * @param author The author's name
     * @param name The blueprint's name
     * @return The filtered blueprint
     * @throws BlueprintNotFoundException if the blueprint is not found
     */
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        // Mantener retorno de entidad, el mapeo a DTO se hace en el controlador
        return filter.apply(persistence.getBlueprint(author, name));
    }

    /**
     * Adds a new point to an existing blueprint.
     * @param author The author's name
     * @param name The blueprint's name
     * @param x The x-coordinate of the new point
     * @param y The y-coordinate of the new point
     * @throws BlueprintNotFoundException if the blueprint is not found
     */
    @Transactional
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        persistence.addPoint(author, name, x, y);
    }
}
