package org.example.algo;

import org.example.model.Edge;
import java.util.ArrayList;
import java.util.List;

/**
 * MST algorithm result: edges and total cost.
 */
public class Result {
    private final List<Edge> mstEdges;
    private final int totalCost;

    public Result(List<Edge> mstEdges, int totalCost) {
        this.mstEdges = new ArrayList<>(mstEdges);
        this.totalCost = totalCost;
    }

    public List<Edge> getMstEdges() {
        return new ArrayList<>(mstEdges);
    }

    public int getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return String.format("Result{edges=%d, cost=%d}", mstEdges.size(), totalCost);
    }
}

