package org.example;

import org.example.model.Edge;
import org.example.model.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for unified MST implementation.
 */
@DisplayName("MST Algorithm Tests")
public class MSTTest {

    @Test
    @DisplayName("Prim: Triangle graph")
    public void testPrimTriangle() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C"),
            Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 2),
                new Edge("A", "C", 3)
            )
        );

        MST.Result result = MST.primSimple(graph);

        assertEquals(3, result.totalCost, "MST cost should be 1+2=3");
        assertEquals(2, result.mstEdges.size(), "MST should have 2 edges");
        assertTrue(result.operationsCount > 0, "Operations should be counted");
    }

    @Test
    @DisplayName("Kruskal: Triangle graph")
    public void testKruskalTriangle() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C"),
            Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 2),
                new Edge("A", "C", 3)
            )
        );

        MST.Result result = MST.kruskal(graph);

        assertEquals(3, result.totalCost, "MST cost should be 1+2=3");
        assertEquals(2, result.mstEdges.size(), "MST should have 2 edges");
        assertTrue(result.operationsCount > 0, "Operations should be counted");
    }

    @Test
    @DisplayName("Both algorithms agree on MST cost")
    public void testBothAlgorithmsAgree() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C", "D", "E"),
            Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("A", "C", 3),
                new Edge("B", "C", 2),
                new Edge("B", "D", 5),
                new Edge("C", "D", 7),
                new Edge("C", "E", 8),
                new Edge("D", "E", 6)
            )
        );

        MST.Result primResult = MST.primSimple(graph);
        MST.Result kruskalResult = MST.kruskal(graph);

        assertEquals(16, primResult.totalCost);
        assertEquals(16, kruskalResult.totalCost);
        assertEquals(primResult.totalCost, kruskalResult.totalCost,
            "Both algorithms must produce same MST cost");
    }

    @Test
    @DisplayName("Disconnected graph (forest)")
    public void testDisconnectedGraph() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C", "D"),
            Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("C", "D", 2)
            )
        );

        MST.Result primResult = MST.primSimple(graph);
        MST.Result kruskalResult = MST.kruskal(graph);

        assertEquals(3, primResult.totalCost, "Prim handles forest");
        assertEquals(3, kruskalResult.totalCost, "Kruskal handles forest");
        assertEquals(2, primResult.mstEdges.size());
        assertEquals(2, kruskalResult.mstEdges.size());
    }

    @Test
    @DisplayName("Complete graph K4")
    public void testCompleteGraph() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C", "D"),
            Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 2),
                new Edge("A", "D", 3),
                new Edge("B", "C", 4),
                new Edge("B", "D", 5),
                new Edge("C", "D", 6)
            )
        );

        MST.Result primResult = MST.primSimple(graph);
        MST.Result kruskalResult = MST.kruskal(graph);

        assertEquals(6, primResult.totalCost, "MST should be 1+2+3=6");
        assertEquals(6, kruskalResult.totalCost, "MST should be 1+2+3=6");
        assertEquals(primResult.totalCost, kruskalResult.totalCost);
    }

    @Test
    @DisplayName("Empty graph")
    public void testEmptyGraph() {
        Graph graph = new Graph(Collections.emptyList(), Collections.emptyList());

        MST.Result primResult = MST.primSimple(graph);
        MST.Result kruskalResult = MST.kruskal(graph);

        assertEquals(0, primResult.totalCost);
        assertEquals(0, kruskalResult.totalCost);
        assertEquals(0, primResult.mstEdges.size());
        assertEquals(0, kruskalResult.mstEdges.size());
    }

    @Test
    @DisplayName("DSU operations")
    public void testDSU() {
        MST.DSU dsu = new MST.DSU(5);

        assertNotEquals(dsu.find(0), dsu.find(1));

        assertTrue(dsu.union(0, 1));
        assertEquals(dsu.find(0), dsu.find(1));

        assertFalse(dsu.union(0, 1));

        assertTrue(dsu.getOperations() > 0);
    }

    @Test
    @DisplayName("Equal weights - any valid MST acceptable")
    public void testEqualWeights() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C", "D"),
            Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("B", "C", 1),
                new Edge("C", "D", 1),
                new Edge("D", "A", 1)
            )
        );

        MST.Result primResult = MST.primSimple(graph);
        MST.Result kruskalResult = MST.kruskal(graph);

        assertEquals(3, primResult.totalCost);
        assertEquals(3, kruskalResult.totalCost);
        assertEquals(3, primResult.mstEdges.size());
        assertEquals(3, kruskalResult.mstEdges.size());
    }

    @Test
    @DisplayName("Star graph (single center)")
    public void testStarGraph() {
        Graph graph = new Graph(
            Arrays.asList("A", "B", "C", "D", "E"),
            Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 2),
                new Edge("A", "D", 3),
                new Edge("A", "E", 4)
            )
        );

        MST.Result primResult = MST.primSimple(graph);
        MST.Result kruskalResult = MST.kruskal(graph);

        assertEquals(10, primResult.totalCost, "All edges from center: 1+2+3+4=10");
        assertEquals(10, kruskalResult.totalCost, "All edges from center: 1+2+3+4=10");
        assertEquals(4, primResult.mstEdges.size());
        assertEquals(4, kruskalResult.mstEdges.size());
    }
}


