package edu.eci.arsw.blueprints;


import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.controllers.*;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for BlueprintsAPIController.
 * Tests HTTP endpoint responses and error handling.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BlueprintsAPIController Unit Tests")
class BlueprintsAPIControllerTest {

    @Mock
    private BlueprintsServices services;

    @InjectMocks
    private BlueprintsAPIController controller;

    private Blueprint sampleBlueprint;
    private Point samplePoint1;
    private Point samplePoint2;
    private Set<Blueprint> sampleBlueprintSet;

    @BeforeEach
    void setUp() {
        samplePoint1 = new Point(10, 20);
        samplePoint2 = new Point(30, 40);
        sampleBlueprint = new Blueprint("author1", "blueprint1", 
                                      Arrays.asList(samplePoint1, samplePoint2));
        sampleBlueprintSet = new HashSet<>(Arrays.asList(sampleBlueprint));
    }

    @Nested
    @DisplayName("GET /api/v1/blueprints")
    class GetAllBlueprintsTests {

        @Test
        @DisplayName("Should return 200 OK with all blueprints")
        void shouldReturnAllBlueprints() {
            // Arrange
            when(services.getAllBlueprints()).thenReturn(sampleBlueprintSet);

            // Act
            ResponseEntity<ApiResponse<Set<Blueprint>>> response = controller.getAll();

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(200);
            assertThat(response.getBody().message()).isEqualTo("Success");
            assertThat(response.getBody().data()).hasSize(1);

            verify(services, times(1)).getAllBlueprints();
        }

        @Test
        @DisplayName("Should return 200 OK with empty list when no blueprints")
        void shouldReturnEmptyListWhenNoBlueprints() {
            // Arrange
            when(services.getAllBlueprints()).thenReturn(new HashSet<>());

            // Act
            ResponseEntity<ApiResponse<Set<Blueprint>>> response = controller.getAll();

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEmpty();

            verify(services, times(1)).getAllBlueprints();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/blueprints/{author}")
    class GetBlueprintsByAuthorTests {

        @Test
        @DisplayName("Should return 200 OK when author exists")
        void shouldReturnBlueprintsWhenAuthorExists() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            when(services.getBlueprintsByAuthor(author)).thenReturn(sampleBlueprintSet);

            // Act
            ResponseEntity<ApiResponse<Set<Blueprint>>> response = controller.byAuthor(author);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(200);
            assertThat(response.getBody().data()).hasSize(1);

            verify(services, times(1)).getBlueprintsByAuthor(author);
        }

        @Test
        @DisplayName("Should return 404 Not Found when author doesn't exist")
        void shouldReturn404WhenAuthorNotFound() throws BlueprintNotFoundException {
            // Arrange
            String author = "nonexistent";
            when(services.getBlueprintsByAuthor(author))
                .thenThrow(new BlueprintNotFoundException("No se encontraron blueprints para el autor"));

            // Act
            ResponseEntity<ApiResponse<Set<Blueprint>>> response = controller.byAuthor(author);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(404);
            assertThat(response.getBody().message()).contains("No se encontraron");

            verify(services, times(1)).getBlueprintsByAuthor(author);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/blueprints/{author}/{bpname}")
    class GetBlueprintByAuthorAndNameTests {

        @Test
        @DisplayName("Should return 200 OK when blueprint exists")
        void shouldReturnBlueprintWhenExists() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String bpname = "blueprint1";
            when(services.getBlueprint(author, bpname)).thenReturn(sampleBlueprint);

            // Act
            ResponseEntity<ApiResponse<Blueprint>> response = controller.byAuthorAndName(author, bpname);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(200);
            assertThat(response.getBody().data()).isEqualTo(sampleBlueprint);
            assertThat(response.getBody().data().getPoints()).hasSize(2);

            verify(services, times(1)).getBlueprint(author, bpname);
        }

        @Test
        @DisplayName("Should return 404 Not Found when blueprint doesn't exist")
        void shouldReturn404WhenBlueprintNotFound() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String bpname = "nonexistent";
            when(services.getBlueprint(author, bpname))
                .thenThrow(new BlueprintNotFoundException("Blueprint no encontrado"));

            // Act
            ResponseEntity<ApiResponse<Blueprint>> response = controller.byAuthorAndName(author, bpname);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(404);
            assertThat(response.getBody().message()).contains("no encontrado");

            verify(services, times(1)).getBlueprint(author, bpname);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/blueprints")
    class AddBlueprintTests {

        @Test
        @DisplayName("Should return 201 Created when blueprint is valid")
        void shouldCreateBlueprintSuccessfully() throws BlueprintPersistenceException {
            // Arrange
            var request = new BlueprintsAPIController.NewBlueprintRequest(
                "author1", 
                "newBlueprint", 
                Arrays.asList(new Point(1, 1), new Point(2, 2))
            );
            
            doNothing().when(services).addNewBlueprint(any(Blueprint.class));

            // Act
            ResponseEntity<ApiResponse<Void>> response = controller.add(request);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(201);
            assertThat(response.getBody().message()).isEqualTo("Created");

            verify(services, times(1)).addNewBlueprint(any(Blueprint.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when blueprint already exists")
        void shouldReturn400WhenBlueprintExists() throws BlueprintPersistenceException {
            // Arrange
            var request = new BlueprintsAPIController.NewBlueprintRequest(
                "author1", 
                "existingBlueprint", 
                Arrays.asList(new Point(1, 1))
            );
            
            doThrow(new BlueprintPersistenceException("El blueprint ya existe"))
                .when(services).addNewBlueprint(any(Blueprint.class));

            // Act
            ResponseEntity<ApiResponse<Void>> response = controller.add(request);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(400);
            assertThat(response.getBody().message()).contains("ya existe");

            verify(services, times(1)).addNewBlueprint(any(Blueprint.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/blueprints/{author}/{bpname}/points")
    class AddPointTests {

        @Test
        @DisplayName("Should return 201 Created when point is added successfully")
        void shouldAddPointSuccessfully() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String bpname = "blueprint1";
            Point point = new Point(50, 60);
            
            doNothing().when(services).addPoint(author, bpname, point.x(), point.y());

            // Act
            ResponseEntity<ApiResponse<Void>> response = controller.addPoint(author, bpname, point);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(201);
            assertThat(response.getBody().message()).isEqualTo("Created");

            verify(services, times(1)).addPoint(author, bpname, point.x(), point.y());
        }

        @Test
        @DisplayName("Should return 404 Not Found when blueprint doesn't exist")
        void shouldReturn404WhenBlueprintNotFound() throws BlueprintNotFoundException {
            // Arrange
            String author = "author1";
            String bpname = "nonexistent";
            Point point = new Point(50, 60);
            
            doThrow(new BlueprintNotFoundException("Blueprint no encontrado"))
                .when(services).addPoint(author, bpname, point.x(), point.y());

            // Act
            ResponseEntity<ApiResponse<Void>> response = controller.addPoint(author, bpname, point);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().code()).isEqualTo(404);
            assertThat(response.getBody().message()).contains("no encontrado");

            verify(services, times(1)).addPoint(author, bpname, point.x(), point.y());
        }
    }
}