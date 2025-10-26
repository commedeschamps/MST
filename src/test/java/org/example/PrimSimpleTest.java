package org.example;

import org.example.algo.PrimSimple;
import org.example.algo.Result;
import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for PrimSimple algorithm (normalized frontier scan).
 * Tests correctness, edge cases, and operations counting.
 */
@DisplayName("PrimSimple Algorithm Tests")
public class PrimSimpleTest {

    private Metrics metrics;

    @BeforeEach
    public void setUp() {
        metrics = new Metrics();
    }

    @Test
    @DisplayName("Triangle with unique minimum weights")
    public void testTriangleWithUniqueMinimum() {
        List<String> nodes = Arrays.asList("A", "B", "C");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 2),
                new Edge("A", "C", 3)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(3, result.getTotalCost(), "MST cost should be 1+2=3");
        assertEquals(2, result.getMstEdges().size(), "MST should have 2 edges for 3 nodes");
        assertTrue(metrics.get() > 0, "Operations should be counted");

        System.out.println("Triangle: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Disconnected graph (forest with 2 components)")
    public void testDisconnectedGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("C", "D", 2)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(3, result.getTotalCost(), "Forest cost should be 1+2=3");
        assertEquals(2, result.getMstEdges().size(), "Forest should have 2 edges");
        assertTrue(metrics.get() > 0, "Operations should be counted");

        System.out.println("Disconnected (2 components): " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("All edges have equal weights")
    public void testEqualWeights() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 1),
                new Edge("C", "D", 1),
                new Edge("D", "A", 1)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(3, result.getTotalCost(), "Any valid MST should have cost 3");
        assertEquals(3, result.getMstEdges().size(), "MST should have 3 edges for 4 nodes");

        System.out.println("Equal weights: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Single vertex graph")
    public void testSingleNode() {
        List<String> nodes = Collections.singletonList("A");
        List<Edge> edges = Collections.emptyList();
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(0, result.getTotalCost());
        assertEquals(0, result.getMstEdges().size());
        assertEquals(0, metrics.get(), "No operations for single node");
    }

    @Test
    @DisplayName("Empty graph")
    public void testEmptyGraph() {
        List<String> nodes = Collections.emptyList();
        List<Edge> edges = Collections.emptyList();
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(0, result.getTotalCost());
        assertEquals(0, result.getMstEdges().size());
        assertEquals(0, metrics.get(), "No operations for empty graph");
    }

    @Test
    @DisplayName("Complete graph K4")
    public void testCompleteGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 2),
                new Edge("A", "D", 3),
                new Edge("B", "C", 4),
                new Edge("B", "D", 5),
                new Edge("C", "D", 6)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(6, result.getTotalCost(), "MST should be 1+2+3=6");
        assertEquals(3, result.getMstEdges().size());

        System.out.println("Complete K4: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Example Graph 1 (5 vertices, 7 edges)")
    public void testExampleGraph1() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("A", "C", 3),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 7),
                new Edge("C", "E", 8),
                new Edge("D", "E", 6)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(16, result.getTotalCost(), "MST cost: 2+3+5+6=16");
        assertEquals(4, result.getMstEdges().size());

        System.out.println("Example Graph 1: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Example Graph 2 (4 vertices, 5 edges)")
    public void testExampleGraph2() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 4),
                new Edge("B", "C", 2),
                new Edge("C", "D", 3),
                new Edge("B", "D", 5)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(6, result.getTotalCost(), "MST cost: 1+2+3=6");
        assertEquals(3, result.getMstEdges().size());

        System.out.println("Example Graph 2: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Three component disconnected graph")
    public void testThreeComponents() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("C", "D", 2),
                new Edge("E", "F", 3)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(6, result.getTotalCost(), "Forest cost: 1+2+3=6");
        assertEquals(3, result.getMstEdges().size(), "Forest should have 3 edges");

        System.out.println("Three components: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Dense graph with parallel edges")
    public void testParallelEdges() {
        List<String> nodes = Arrays.asList("A", "B", "C");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 5),
                new Edge("A", "B", 1),  // Parallel edge with different weight
                new Edge("B", "C", 2),
                new Edge("A", "C", 4)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(3, result.getTotalCost(), "Should pick minimum A-B edge: 1+2=3");
        assertEquals(2, result.getMstEdges().size());

        System.out.println("Parallel edges: " + metrics.get() + " operations");
    }

    @Test
    @DisplayName("Star graph (one center vertex)")
    public void testStarGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 2),
                new Edge("A", "D", 3),
                new Edge("A", "E", 4)
        );
        Graph graph = new Graph(nodes, edges);

        Result result = PrimSimple.computeMST(graph, metrics);

        assertEquals(10, result.getTotalCost(), "All edges from center: 1+2+3+4=10");
        assertEquals(4, result.getMstEdges().size());

        System.out.println("Star graph: " + metrics.get() + " operations");
    }
}