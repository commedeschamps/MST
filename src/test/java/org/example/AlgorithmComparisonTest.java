package org.example;

import org.example.algo.Kruskal;
import org.example.algo.PrimSimple;
import org.example.algo.Result;
import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comparison tests between PrimSimple and Kruskal algorithms.
 * Validates that both algorithms produce identical MST costs.
 */
@DisplayName("Algorithm Comparison Tests")
public class AlgorithmComparisonTest {

    @Test
    @DisplayName("Both algorithms: same cost on connected graph")
    public void testBothAlgorithmsProduceSameCost() {
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

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(),
            "Both algorithms must produce same MST cost");
        assertEquals(primResult.getMstEdges().size(), kruskalResult.getMstEdges().size(),
            "Both algorithms must produce same number of edges");

        System.out.println("Comparison - Connected graph:");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
    }

    @Test
    @DisplayName("Both algorithms: same cost on disconnected graph (2 components)")
    public void testBothAlgorithmsTwoComponents() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("C", "D", 2)
        );
        Graph graph = new Graph(nodes, edges);

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        assertEquals(3, primResult.getTotalCost());
        assertEquals(3, kruskalResult.getTotalCost());
        assertEquals(2, primResult.getMstEdges().size());
        assertEquals(2, kruskalResult.getMstEdges().size());

        System.out.println("Comparison - 2 components:");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
    }

    @Test
    @DisplayName("Both algorithms: same cost on disconnected graph (3 components)")
    public void testBothAlgorithmsThreeComponents() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("C", "D", 2),
            new Edge("E", "F", 3)
        );
        Graph graph = new Graph(nodes, edges);

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        assertEquals(6, primResult.getTotalCost());
        assertEquals(6, kruskalResult.getTotalCost());
        assertEquals(3, primResult.getMstEdges().size());
        assertEquals(3, kruskalResult.getMstEdges().size());

        System.out.println("Comparison - 3 components:");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
    }

    @Test
    @DisplayName("Both algorithms: same cost on complete graph K5")
    public void testBothAlgorithmsCompleteGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("A", "C", 2),
            new Edge("A", "D", 3),
            new Edge("A", "E", 4),
            new Edge("B", "C", 5),
            new Edge("B", "D", 6),
            new Edge("B", "E", 7),
            new Edge("C", "D", 8),
            new Edge("C", "E", 9),
            new Edge("D", "E", 10)
        );
        Graph graph = new Graph(nodes, edges);

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        assertEquals(10, primResult.getTotalCost(), "MST: 1+2+3+4=10");
        assertEquals(10, kruskalResult.getTotalCost(), "MST: 1+2+3+4=10");
        assertEquals(4, primResult.getMstEdges().size());
        assertEquals(4, kruskalResult.getMstEdges().size());

        System.out.println("Comparison - Complete K5:");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
    }

    @Test
    @DisplayName("Both algorithms: same cost with equal weights")
    public void testBothAlgorithmsEqualWeights() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("B", "C", 1),
            new Edge("C", "D", 1),
            new Edge("D", "E", 1),
            new Edge("E", "A", 1)
        );
        Graph graph = new Graph(nodes, edges);

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        assertEquals(4, primResult.getTotalCost());
        assertEquals(4, kruskalResult.getTotalCost());
        assertEquals(4, primResult.getMstEdges().size());
        assertEquals(4, kruskalResult.getMstEdges().size());

        System.out.println("Comparison - Equal weights:");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
    }

    @Test
    @DisplayName("Both algorithms: same cost on sparse graph")
    public void testBothAlgorithmsSparseGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E", "F");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("B", "C", 2),
            new Edge("C", "D", 3),
            new Edge("D", "E", 4),
            new Edge("E", "F", 5)
        );
        Graph graph = new Graph(nodes, edges);

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        assertEquals(15, primResult.getTotalCost(), "Linear path: 1+2+3+4+5=15");
        assertEquals(15, kruskalResult.getTotalCost(), "Linear path: 1+2+3+4+5=15");
        assertEquals(5, primResult.getMstEdges().size());
        assertEquals(5, kruskalResult.getMstEdges().size());

        System.out.println("Comparison - Sparse graph (linear):");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
        System.out.println("  Operations ratio: " +
            String.format("%.2f", (double) primMetrics.get() / kruskalMetrics.get()));
    }

    @Test
    @DisplayName("Operations count comparison on sparse linear graph")
    public void testOperationsComparisonOnSparseGraph() {
        // Linear graph - very sparse
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
        List<Edge> edges = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("B", "C", 2),
            new Edge("C", "D", 3),
            new Edge("D", "E", 4),
            new Edge("E", "F", 5),
            new Edge("F", "G", 6),
            new Edge("G", "H", 7)
        );
        Graph graph = new Graph(nodes, edges);

        Metrics primMetrics = new Metrics();
        Metrics kruskalMetrics = new Metrics();

        Result primResult = PrimSimple.computeMST(graph, primMetrics);
        Result kruskalResult = Kruskal.computeMST(graph, kruskalMetrics);

        // Verify correctness
        assertEquals(28, primResult.getTotalCost(), "Linear path total: 1+2+3+4+5+6+7=28");
        assertEquals(28, kruskalResult.getTotalCost(), "Both should produce same cost");
        assertEquals(7, primResult.getMstEdges().size());
        assertEquals(7, kruskalResult.getMstEdges().size());

        // Report operations for analysis (no assertion on which is better)
        System.out.println("Operations comparison (sparse linear graph, 8 nodes, 7 edges):");
        System.out.println("  Prim operations: " + primMetrics.get());
        System.out.println("  Kruskal operations: " + kruskalMetrics.get());
        System.out.println("  Ratio (Prim/Kruskal): " +
            String.format("%.2f", (double) primMetrics.get() / kruskalMetrics.get()));

        // Both algorithms should produce valid results
        assertNotNull(primResult.getMstEdges());
        assertNotNull(kruskalResult.getMstEdges());
    }
}


