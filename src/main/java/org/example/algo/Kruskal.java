package org.example.algo;

import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;

import java.util.*;


public class Kruskal {

    /**
     * Compute MST using Kruskal's algorithm with DSU.
     * Handles disconnected graphs (returns forest).
     */
    public static Result computeMST(Graph graph, Metrics metrics) {
        List<String> nodes = graph.getNodes();
        List<Edge> edges = new ArrayList<>(graph.getEdges());

        if (nodes.isEmpty()) {
            return new Result(new ArrayList<>(), 0);
        }

        // Map node names to indices [0..n-1]
        Map<String, Integer> nodeIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndex.put(nodes.get(i), i);
        }

        // Sort edges by weight (with metrics counting comparisons)
        edges.sort((e1, e2) -> {
            metrics.inc(); // Count comparison operation
            return Integer.compare(e1.w, e2.w);
        });

        // Initialize DSU
        DSU dsu = new DSU(nodes.size());
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // Process edges in sorted order
        for (Edge edge : edges) {
            int u = nodeIndex.get(edge.u);
            int v = nodeIndex.get(edge.v);

            // Check if edge creates a cycle using DSU
            int rootU = dsu.find(u, metrics);
            int rootV = dsu.find(v, metrics);

            if (rootU != rootV) {
                // No cycle: add edge to MST
                dsu.union(rootU, rootV, metrics);
                mstEdges.add(edge);
                totalCost += edge.w;
            }
        }

        return new Result(mstEdges, totalCost);
    }
}


