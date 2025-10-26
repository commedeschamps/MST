package org.example.algo;

import org.example.util.Metrics;

/**
 * Disjoint Set Union with path compression and union by size.
 */
public class DSU {
    private final int[] parent;
    private final int[] size;

    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public int find(int x, Metrics metrics) {
        if (metrics != null) {
            metrics.inc();
        }
        return find(x);
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }

        return true;
    }

    public boolean union(int x, int y, Metrics metrics) {
        if (metrics != null) {
            metrics.inc();
        }
        return union(x, y);
    }
}

