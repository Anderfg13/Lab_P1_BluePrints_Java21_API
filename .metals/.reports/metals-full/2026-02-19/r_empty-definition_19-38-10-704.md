error id: file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/persistence/PostgresBlueprintPersistence.java:java/lang/String#
file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/persistence/PostgresBlueprintPersistence.java
empty definition using pc, found symbol in pc: java/lang/String#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 2515
uri: file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/persistence/PostgresBlueprintPersistence.java
text:
```scala
package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * PostgreSQL implementation of BlueprintPersistence using Spring Data JPA.
 * Replaces the in-memory version and persists blueprints in the database.
 */
@Primary
@Repository
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        try {
            entityManager.persist(bp);
        } catch (Exception e) {
            throw new BlueprintPersistenceException("Error saving blueprint: " + e.getMessage());
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        TypedQuery<Blueprint> query = entityManager.createQuery(
            "SELECT b FROM Blueprint b WHERE b.author = :author AND b.name = :name", Blueprint.class);
        query.setParameter("author", author);
        query.setParameter("name", name);
        List<Blueprint> result = query.getResultList();
        if (result.isEmpty()) {
            throw new BlueprintNotFoundException("Blueprint not found: " + author + "/" + name);
        }
        return result.get(0);
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        TypedQuery<Blueprint> query = entityManager.createQuery(
            "SELECT b FROM Blueprint b WHERE b.author = :author", Blueprint.class);
        query.setParameter("author", author);
        List<Blueprint> result = query.getResultList();
        if (result.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints for author: " + author);
        }
        return new HashSet<>(result);
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        TypedQuery<Blueprint> query = entityManager.createQuery(
            "SELECT b FROM Blueprint b", Blueprint.class);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public void addPoint(@@String author, String name, int x, int y) throws BlueprintNotFoundException {
        Blueprint bp = getBlueprint(author, name);
        bp.addPoint(new edu.eci.arsw.blueprints.model.Point(x, y));
        entityManager.merge(bp);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/lang/String#