package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Undirected graph with named vertices and weighted edges.
 */
public class Graph {
    private final List<String> nodes;
    private final List<Edge> edges;

    public Graph(List<String> nodes, List<Edge> edges) {
        this.nodes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>(edges);
    }

    public List<String> getNodes() {
        return new ArrayList<>(nodes);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public List<Edge> getIncidentEdges(String vertex) {
        List<Edge> incident = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.u.equals(vertex) || edge.v.equals(vertex)) {
                incident.add(edge);
            }
        }
        return incident;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public String toString() {
        return String.format("Graph{nodes=%d, edges=%d}", nodes.size(), edges.size());
    }
}

