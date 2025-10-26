package org.example.algo;

import org.example.model.Edge;
import org.example.model.Graph;
import org.example.util.Metrics;

import java.util.*;


public class PrimSimple {

    /**
     * Compute MST using frontier scan approach.
     * Handles disconnected graphs (returns forest).
     */
    public static Result computeMST(Graph graph, Metrics metrics) {
        List<String> nodes = graph.getNodes();
        List<Edge> allEdges = graph.getEdges();

        if (nodes.isEmpty()) {
            return new Result(new ArrayList<>(), 0);
        }

        Set<String> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // Handle potentially disconnected graph by processing all components
        for (String startNode : nodes) {
            if (visited.contains(startNode)) {
                continue; // Already processed in previous component
            }

            // Start new component from this node
            visited.add(startNode);

            // Grow this component until no more reachable unvisited nodes
            while (true) {
                Edge minEdge = null;
                int minWeight = Integer.MAX_VALUE;

                // Scan all edges from visited to unvisited (frontier scan)
                for (Edge edge : allEdges) {
                    String u = edge.u;
                    String v = edge.v;

                    // Check if edge crosses the cut (visited → unvisited)
                    boolean uVisited = visited.contains(u);
                    boolean vVisited = visited.contains(v);

                    if (uVisited && vVisited) {
                        // Both visited: skip (edge within component)
                        continue;
                    }

                    if (!uVisited && !vVisited) {
                        // Both unvisited: skip (edge in different component)
                        continue;
                    }

                    // Edge crosses the cut: candidate for MST
                    metrics.inc(); // Count candidate edge check/comparison

                    if (edge.w < minWeight) {
                        minWeight = edge.w;
                        minEdge = edge;
                    }
                }

                // If no edge found, this component is complete
                if (minEdge == null) {
                    break;
                }

                // Add minimum edge to MST
                mstEdges.add(minEdge);
                totalCost += minWeight;
                metrics.inc(); // Count edge addition to MST

                // Mark the new vertex as visited
                String newVertex;
                if (visited.contains(minEdge.u)) {
                    newVertex = minEdge.v;
                } else {
                    newVertex = minEdge.u;
                }
                visited.add(newVertex);
            }
        }

        return new Result(mstEdges, totalCost);
    }
}

