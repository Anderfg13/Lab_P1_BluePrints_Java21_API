package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.filters.BlueprintsFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.*;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlueprintsServices Unit Tests")
class BlueprintsServicesTest {

    @Mock
    private BlueprintPersistence persistence;

    @Mock
    private BlueprintsFilter filter;

    @InjectMocks
    private BlueprintsServices services;

    private Blueprint sampleBlueprint;
    private Point samplePoint1;
    private Point samplePoint2;
    private List<Point> originalPoints;

    @BeforeEach
    void setUp() {
        samplePoint1 = new Point(10, 20);
        samplePoint2 = new Point(30, 40);
        originalPoints = Arrays.asList(samplePoint1, samplePoint2);
        sampleBlueprint = new Blueprint("author1", "blueprint1", originalPoints);
    }

    @Nested
    @DisplayName("Tests for getBlueprint method - CORREGIDO")
    class GetBlueprintTests {

        @Test
        @DisplayName("Should return filtered blueprint when it exists")
        void shouldReturnFilteredBlueprintWhenExists() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String name = "blueprint1";
            
            // Mock the persistence to return the original blueprint
            when(persistence.getBlueprint(author, name)).thenReturn(sampleBlueprint);
            
            // Create filtered points (only the first point)
            List<Point> filteredPoints = Arrays.asList(samplePoint1);
            Blueprint filteredBlueprint = new Blueprint(author, name, filteredPoints);
            
            // Mock the filter to return the filtered blueprint
            when(filter.apply(sampleBlueprint)).thenReturn(filteredBlueprint);

            // Act
            Blueprint actualBlueprint = services.getBlueprint(author, name);

            // Assert - CORREGIDO: Comparar puntos en lugar de blueprints completos
            assertThat(actualBlueprint).isNotNull();
            assertThat(actualBlueprint.getAuthor()).isEqualTo(author);
            assertThat(actualBlueprint.getName()).isEqualTo(name);
            assertThat(actualBlueprint.getPoints())
                .hasSize(1)
                .containsExactly(samplePoint1);
            
            // Verificar que el filtro se aplicó (los puntos son diferentes)
            assertThat(actualBlueprint.getPoints())
                .isNotEqualTo(originalPoints);

            verify(persistence, times(1)).getBlueprint(author, name);
            verify(filter, times(1)).apply(sampleBlueprint);
        }

        @Test
        @DisplayName("Should return original blueprint when filter returns same blueprint")
        void shouldReturnOriginalWhenFilterReturnsSame() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String name = "blueprint1";
            
            when(persistence.getBlueprint(author, name)).thenReturn(sampleBlueprint);
            // Filter returns the SAME blueprint (like IdentityFilter)
            when(filter.apply(sampleBlueprint)).thenReturn(sampleBlueprint);

            // Act
            Blueprint actualBlueprint = services.getBlueprint(author, name);

            // Assert
            assertThat(actualBlueprint).isNotNull();
            assertThat(actualBlueprint.getAuthor()).isEqualTo(author);
            assertThat(actualBlueprint.getName()).isEqualTo(name);
            assertThat(actualBlueprint.getPoints())
                .hasSize(2)
                .containsExactly(samplePoint1, samplePoint2);

            verify(persistence, times(1)).getBlueprint(author, name);
            verify(filter, times(1)).apply(sampleBlueprint);
        }

        @Test
        @DisplayName("Should throw exception when blueprint doesn't exist")
        void shouldThrowExceptionWhenBlueprintDoesNotExist() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String name = "nonexistent";
            
            when(persistence.getBlueprint(author, name))
                .thenThrow(new BlueprintNotFoundException("Blueprint not found"));

            // Act & Assert
            assertThatThrownBy(() -> services.getBlueprint(author, name))
                .isInstanceOf(BlueprintNotFoundException.class)
                .hasMessageContaining("not found");

            verify(persistence, times(1)).getBlueprint(author, name);
            verifyNoInteractions(filter);
        }
    }

    // El resto de las pruebas quedan igual...
    @Nested
    @DisplayName("Tests for addNewBlueprint method")
    class AddNewBlueprintTests {
        @Test
        @DisplayName("Should save blueprint successfully when it doesn't exist")
        void shouldSaveBlueprintSuccessfully() throws BlueprintPersistenceException {
            doNothing().when(persistence).saveBlueprint(sampleBlueprint);

            services.addNewBlueprint(sampleBlueprint);

            verify(persistence, times(1)).saveBlueprint(sampleBlueprint);
            verifyNoInteractions(filter);
        }
    }

    @Nested
    @DisplayName("Tests for getAllBlueprints method")
    class GetAllBlueprintsTests {
        @Test
        @DisplayName("Should return all blueprints when they exist")
        void shouldReturnAllBlueprints() {
            Set<Blueprint> expectedBlueprints = new HashSet<>(Arrays.asList(
                sampleBlueprint,
                new Blueprint("author2", "blueprint2", Arrays.asList(new Point(1, 1)))
            ));
            when(persistence.getAllBlueprints()).thenReturn(expectedBlueprints);

            Set<Blueprint> actualBlueprints = services.getAllBlueprints();

            assertThat(actualBlueprints).isNotNull().hasSize(2);
            verify(persistence, times(1)).getAllBlueprints();
        }
    }

    @Nested
    @DisplayName("Tests for getBlueprintsByAuthor method")
    class GetBlueprintsByAuthorTests {
        @Test
        @DisplayName("Should return blueprints when author exists")
        void shouldReturnBlueprintsWhenAuthorExists() throws BlueprintNotFoundException {
            String author = "author1";
            Set<Blueprint> expectedBlueprints = new HashSet<>(Arrays.asList(sampleBlueprint));
            when(persistence.getBlueprintsByAuthor(author)).thenReturn(expectedBlueprints);

            Set<Blueprint> actualBlueprints = services.getBlueprintsByAuthor(author);

            assertThat(actualBlueprints).isNotNull().hasSize(1);
            verify(persistence, times(1)).getBlueprintsByAuthor(author);
        }
    }

    @Nested
    @DisplayName("Tests for addPoint method")
    class AddPointTests {
        @Test
        @DisplayName("Should add point successfully when blueprint exists")
        void shouldAddPointSuccessfully() throws BlueprintNotFoundException {
            String author = "author1";
            String name = "blueprint1";
            int x = 50;
            int y = 60;
            
            doNothing().when(persistence).addPoint(author, name, x, y);

            services.addPoint(author, name, x, y);

            verify(persistence, times(1)).addPoint(author, name, x, y);
        }
    }
}