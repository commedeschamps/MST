package org.example.model;

/**
 * Weighted undirected edge (professor's style).
 */
public class Edge {
    public final String u;
    public final String v;
    public final int w;

    public Edge(String u, String v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public String toString() {
        return String.format("(%s-%s:%d)", u, v, w);
    }
}

