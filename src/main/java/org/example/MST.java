package org.example;

import com.google.gson.*;
import org.example.model.Edge;
import org.example.model.Graph;

import java.io.*;
import java.util.*;

/**
 * MST (Minimum Spanning Tree) - Complete implementation.
 * Uses OOP model classes (Edge, Graph) with static algorithm methods.
 */
public class MST {

    // ==================== RESULT CLASS ====================

    public static class Result {
        final List<Edge> mstEdges;
        final int totalCost;
        final long operationsCount;

        public Result(List<Edge> mstEdges, int totalCost, long operationsCount) {
            this.mstEdges = new ArrayList<>(mstEdges);
            this.totalCost = totalCost;
            this.operationsCount = operationsCount;
        }
    }

    // ==================== DISJOINT SET UNION ====================

    public static class DSU {
        private final int[] parent;
        private final int[] size;
        private long operations;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
            operations = 0;
        }

        public int find(int x) {
            operations++;
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            operations++;
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

        public long getOperations() {
            return operations;
        }
    }

    // ==================== ALGORITHMS ====================

    public static Result primSimple(Graph graph) {
        int n = graph.getNodeCount();
        if (n == 0) {
            return new Result(new ArrayList<>(), 0, 0);
        }

        List<String> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();

        Map<String, Integer> nodeIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            nodeIndex.put(nodes.get(i), i);
        }

        boolean[] visited = new boolean[n];
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        long operations = 0;

        // Process each connected component
        for (int startNode = 0; startNode < n; startNode++) {
            if (visited[startNode]) continue;

            // Start new component with this node
            visited[startNode] = true;

            // Keep adding edges until no more edges can be found for this component
            while (true) {
                Edge minEdge = null;
                int minWeight = Integer.MAX_VALUE;

                // Find minimum edge connecting visited to unvisited vertices
                for (Edge e : edges) {
                    operations++;

                    int uIdx = nodeIndex.get(e.u);
                    int vIdx = nodeIndex.get(e.v);

                    boolean uVisited = visited[uIdx];
                    boolean vVisited = visited[vIdx];

                    // Skip if both visited or both unvisited
                    if (uVisited && vVisited) continue;
                    if (!uVisited && !vVisited) continue;

                    // Found a frontier edge
                    if (e.w < minWeight) {
                        minWeight = e.w;
                        minEdge = e;
                    }
                }

                // No more edges to this component
                if (minEdge == null) break;

                // Add edge to MST
                mstEdges.add(minEdge);
                totalCost += minWeight;
                operations++;

                // Mark new vertex as visited
                int uIdx = nodeIndex.get(minEdge.u);
                int vIdx = nodeIndex.get(minEdge.v);

                if (!visited[uIdx]) {
                    visited[uIdx] = true;
                } else {
                    visited[vIdx] = true;
                }
            }
        }

        return new Result(mstEdges, totalCost, operations);
    }

    public static Result kruskal(Graph graph) {
        int n = graph.getNodeCount();
        if (n == 0) {
            return new Result(new ArrayList<>(), 0, 0);
        }

        List<String> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();

        Map<String, Integer> nodeIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            nodeIndex.put(nodes.get(i), i);
        }

        List<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingInt(e -> e.w));
        long sortOps = edges.size();

        DSU dsu = new DSU(n);
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge e : sortedEdges) {
            int u = nodeIndex.get(e.u);
            int v = nodeIndex.get(e.v);

            int rootU = dsu.find(u);
            int rootV = dsu.find(v);

            if (rootU != rootV) {
                dsu.union(rootU, rootV);
                mstEdges.add(e);
                totalCost += e.w;
            }
        }

        return new Result(mstEdges, totalCost, sortOps + dsu.getOperations());
    }

    // ==================== JSON I/O ====================

    public static List<GraphInput> readJSON(String path) throws IOException {
        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphsArray = root.getAsJsonArray("graphs");

            List<GraphInput> inputs = new ArrayList<>();
            for (JsonElement graphElem : graphsArray) {
                JsonObject graphObj = graphElem.getAsJsonObject();

                String graphId = graphObj.get("id").getAsString();

                List<String> nodes = new ArrayList<>();
                JsonArray nodesArray = graphObj.getAsJsonArray("nodes");
                for (JsonElement nodeElem : nodesArray) {
                    nodes.add(nodeElem.getAsString());
                }

                List<Edge> edges = new ArrayList<>();
                JsonArray edgesArray = graphObj.getAsJsonArray("edges");
                for (JsonElement edgeElem : edgesArray) {
                    JsonObject edgeObj = edgeElem.getAsJsonObject();
                    String from = edgeObj.get("from").getAsString();
                    String to = edgeObj.get("to").getAsString();
                    int weight = edgeObj.get("weight").getAsInt();
                    edges.add(new Edge(from, to, weight));
                }

                inputs.add(new GraphInput(graphId, new Graph(nodes, edges)));
            }

            return inputs;
        }
    }

    public static void writeJSON(String path, List<OutputRecord> records) throws IOException {
        JsonObject root = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        for (OutputRecord record : records) {
            JsonObject graphResult = new JsonObject();
            graphResult.addProperty("graph_id", record.graphId);

            JsonObject inputStats = new JsonObject();
            inputStats.addProperty("vertices", record.vertexCount);
            inputStats.addProperty("edges", record.edgeCount);
            graphResult.add("input_stats", inputStats);

            JsonObject primResult = new JsonObject();
            primResult.add("mst_edges", edgesToJson(record.primResult.mstEdges));
            primResult.addProperty("total_cost", record.primResult.totalCost);
            primResult.addProperty("operations_count", record.primResult.operationsCount);
            primResult.addProperty("execution_time_ms", record.primTimeMs);
            graphResult.add("prim", primResult);

            JsonObject kruskalResult = new JsonObject();
            kruskalResult.add("mst_edges", edgesToJson(record.kruskalResult.mstEdges));
            kruskalResult.addProperty("total_cost", record.kruskalResult.totalCost);
            kruskalResult.addProperty("operations_count", record.kruskalResult.operationsCount);
            kruskalResult.addProperty("execution_time_ms", record.kruskalTimeMs);
            graphResult.add("kruskal", kruskalResult);

            resultsArray.add(graphResult);
        }

        root.add("results", resultsArray);

        try (FileWriter writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(root, writer);
        }
    }

    private static JsonArray edgesToJson(List<Edge> edges) {
        JsonArray array = new JsonArray();
        for (Edge edge : edges) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("from", edge.u);
            edgeObj.addProperty("to", edge.v);
            edgeObj.addProperty("weight", edge.w);
            array.add(edgeObj);
        }
        return array;
    }

    // ==================== HELPER CLASSES ====================

    public static class GraphInput {
        final String id;
        final Graph graph;

        public GraphInput(String id, Graph graph) {
            this.id = id;
            this.graph = graph;
        }
    }

    public static class OutputRecord {
        final String graphId;
        final int vertexCount;
        final int edgeCount;
        final Result primResult;
        final Result kruskalResult;
        final double primTimeMs;
        final double kruskalTimeMs;

        public OutputRecord(String graphId, int vertexCount, int edgeCount,
                          Result primResult, Result kruskalResult,
                          double primTimeMs, double kruskalTimeMs) {
            this.graphId = graphId;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            this.primResult = primResult;
            this.kruskalResult = kruskalResult;
            this.primTimeMs = primTimeMs;
            this.kruskalTimeMs = kruskalTimeMs;
        }
    }

    // ==================== TIMING ====================

    public static double measureMedian(Runnable task, int runs) {
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            times.add(end - start);
        }
        Collections.sort(times);
        return times.get(runs / 2) / 1_000_000.0;
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        String inputPath = "input_example.json";
        String outputPath = "output.json";
        int runs = 5;

        for (int i = 0; i < args.length; i++) {
            if ("--in".equals(args[i]) && i + 1 < args.length) inputPath = args[++i];
            else if ("--out".equals(args[i]) && i + 1 < args.length) outputPath = args[++i];
            else if ("--runs".equals(args[i]) && i + 1 < args.length) runs = Integer.parseInt(args[++i]);
        }

        try {
            List<GraphInput> inputs = readJSON(inputPath);
            List<OutputRecord> outputs = new ArrayList<>();

            System.out.println("Processing " + inputs.size() + " graph(s)...\n");
            System.out.printf("%-10s %-10s | %-12s | %-10s | %-10s%n",
                "Graph", "Algorithm", "Operations", "Time(ms)", "Total Cost");
            System.out.println("-".repeat(70));

            for (GraphInput input : inputs) {
                Graph graph = input.graph;

                double primTime = measureMedian(() -> primSimple(graph), runs);
                Result primResult = primSimple(graph);

                double kruskalTime = measureMedian(() -> kruskal(graph), runs);
                Result kruskalResult = kruskal(graph);

                OutputRecord record = new OutputRecord(
                    input.id, graph.getNodeCount(), graph.getEdgeCount(),
                    primResult, kruskalResult,
                    Math.round(primTime * 100.0) / 100.0,
                    Math.round(kruskalTime * 100.0) / 100.0
                );

                System.out.printf("%-10s %-10s | %-12d | %-10.2f | %-10d%n",
                    "G" + input.id, "prim", primResult.operationsCount, record.primTimeMs, primResult.totalCost);
                System.out.printf("%-10s %-10s | %-12d | %-10.2f | %-10d%n",
                    "", "kruskal", kruskalResult.operationsCount, record.kruskalTimeMs, kruskalResult.totalCost);

                if (primResult.totalCost == kruskalResult.totalCost) {
                    System.out.println("  ✓ Both algorithms agree on MST cost: " + primResult.totalCost);
                } else {
                    System.err.println("  ✗ ERROR: Different costs!");
                }
                System.out.println();

                outputs.add(record);
            }

            writeJSON(outputPath, outputs);
            System.out.println("Results written to: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Stack trace:");
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("  at " + element);
            }
        }
    }
}

