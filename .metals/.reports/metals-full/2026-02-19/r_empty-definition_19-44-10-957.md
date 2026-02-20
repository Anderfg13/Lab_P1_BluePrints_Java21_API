error id: file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/model/Point.java:jakarta/persistence/Embeddable#
file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/model/Point.java
empty definition using pc, found symbol in pc: jakarta/persistence/Embeddable#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 67
uri: file:///D:/ander/Documents/SEMESTRE%207/ARSW/LAB04%20-%20P1%20-%20BLUEPRINTS/Lab_P1_BluePrints_Java21_API/src/main/java/edu/eci/arsw/blueprints/model/Point.java
text:
```scala
package edu.eci.arsw.blueprints.model;
import jakarta.persistence.@@Embeddable;

/**
 * Represents a point in a 2D coordinate system.
 * Each point has an x (horizontal) and y (vertical) value.
 * This class is immutable and uses Java's record feature for simplicity.
 *
 * @param x The x-coordinate of the point
 * @param y The y-coordinate of the point
 */
@Embeddable
public record Point(int x, int y) { }

```


#### Short summary: 

empty definition using pc, found symbol in pc: jakarta/persistence/Embeddable#