package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.filters.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for BlueprintsFilter implementations.
 */
@DisplayName("BlueprintsFilter Unit Tests")
class BlueprintsFilterTest {

    private static final String AUTHOR = "testAuthor";
    private static final String NAME = "testBlueprint";

    @Nested
    @DisplayName("IdentityFilter Tests")
    class IdentityFilterTest {

        private IdentityFilter identityFilter;

        @BeforeEach
        void setUp() {
            identityFilter = new IdentityFilter();
        }

        @Test
        @DisplayName("Should return the same blueprint unchanged")
        void shouldReturnSameBlueprint() {
            List<Point> originalPoints = Arrays.asList(
                new Point(1, 1), new Point(2, 2), new Point(3, 3)
            );
            Blueprint original = new Blueprint(AUTHOR, NAME, originalPoints);

            Blueprint result = identityFilter.apply(original);

            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(original);
            
            assertThat(result.getPoints())
                .containsExactlyElementsOf(originalPoints);
        }
    }

    @Nested
    @DisplayName("RedundancyFilter Tests")
    class RedundancyFilterTest {

        private RedundancyFilter redundancyFilter;

        @BeforeEach
        void setUp() {
            redundancyFilter = new RedundancyFilter();
        }

        @Test
        @DisplayName("Should remove consecutive duplicate points")
        void shouldRemoveConsecutiveDuplicates() {
            List<Point> inputPoints = Arrays.asList(
                new Point(1, 1), new Point(1, 1),
                new Point(2, 2), new Point(2, 2), new Point(2, 2),
                new Point(3, 3), new Point(4, 4), new Point(4, 4)
            );
            
            List<Point> expectedPoints = Arrays.asList(
                new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4)
            );
            
            Blueprint blueprint = new Blueprint(AUTHOR, NAME, inputPoints);

            Blueprint result = redundancyFilter.apply(blueprint);

            assertThat(result.getPoints())
                .hasSize(4)
                .containsExactlyElementsOf(expectedPoints);
        }

        @ParameterizedTest
        @MethodSource("provideRedundancyTestCases")
        @DisplayName("Should handle various redundancy scenarios")
        void shouldHandleVariousScenarios(List<Point> input, List<Point> expected) {
            Blueprint blueprint = new Blueprint(AUTHOR, NAME, input);

            Blueprint result = redundancyFilter.apply(blueprint);

            assertThat(result.getPoints()).containsExactlyElementsOf(expected);
        }

        private static Stream<Arguments> provideRedundancyTestCases() {
            return Stream.of(
                Arguments.of(
                    Arrays.asList(new Point(1, 1), new Point(1, 1), new Point(1, 1)),
                    Collections.singletonList(new Point(1, 1))
                ),
                Arguments.of(
                    Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(2, 2), new Point(3, 3)),
                    Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3))
                ),
                Arguments.of(
                    Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3)),
                    Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3))
                )
            );
        }
    }

    @Nested
    @DisplayName("UndersamplingFilter Tests")
    class UndersamplingFilterTest {

        private UndersamplingFilter undersamplingFilter;

        @BeforeEach
        void setUp() {
            undersamplingFilter = new UndersamplingFilter();
        }

        @Test
        @DisplayName("Should keep every other point (even indices)")
        void shouldKeepEveryOtherPoint() {
            List<Point> inputPoints = Arrays.asList(
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3),
                new Point(4, 4)
            );
            
            List<Point> expectedPoints = Arrays.asList(
                new Point(0, 0),
                new Point(2, 2),
                new Point(4, 4)
            );
            
            Blueprint blueprint = new Blueprint(AUTHOR, NAME, inputPoints);

            Blueprint result = undersamplingFilter.apply(blueprint);

            assertThat(result.getPoints())
                .hasSize(3)
                .containsExactlyElementsOf(expectedPoints);
        }

        @ParameterizedTest
        @MethodSource("provideUndersamplingTestCases")
        @DisplayName("Should handle various undersampling scenarios")
        void shouldHandleVariousScenarios(int inputSize, int expectedSize) {
            List<Point> inputPoints = createPoints(inputSize);
            Blueprint blueprint = new Blueprint(AUTHOR, NAME, inputPoints);

            Blueprint result = undersamplingFilter.apply(blueprint);

            assertThat(result.getPoints()).hasSize(expectedSize);
        }

        private static Stream<Arguments> provideUndersamplingTestCases() {
            return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(1, 1),
                Arguments.of(2, 1),
                Arguments.of(3, 2),
                Arguments.of(4, 2),
                Arguments.of(5, 3),
                Arguments.of(6, 3)
            );
        }

        private List<Point> createPoints(int count) {
            return Stream.iterate(0, i -> i + 1)
                .limit(count)
                .map(i -> new Point(i, i))
                .toList();
        }
    }
}