package org.example.io;

import com.google.gson.*;
import org.example.model.Edge;
import org.example.model.Graph;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON I/O handler for reading input graphs and writing MST results.
 * Supports flexible input formats and ensures exact output schema compliance.
 */
public class JsonIO {

    /**
     * Read input graphs from JSON file.
     * Supports both array-of-strings and array-of-objects for vertices.
     * Supports both {u,v,w} and {from,to,weight} for edges.
     */
    public static List<InputGraph> readInputs(String path) throws IOException {
        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphsArray = root.getAsJsonArray("graphs");

            List<InputGraph> inputs = new ArrayList<>();
            for (JsonElement graphElem : graphsArray) {
                JsonObject graphObj = graphElem.getAsJsonObject();

                // Read graph ID
                String graphId = graphObj.get("id").getAsString();

                // Read nodes (liberal: accept ["A","B"] or [{"id":"A"}, ...])
                List<String> nodes = new ArrayList<>();
                JsonArray nodesArray = graphObj.getAsJsonArray("nodes");
                for (JsonElement nodeElem : nodesArray) {
                    if (nodeElem.isJsonPrimitive()) {
                        nodes.add(nodeElem.getAsString());
                    } else if (nodeElem.isJsonObject()) {
                        nodes.add(nodeElem.getAsJsonObject().get("id").getAsString());
                    }
                }

                // Read edges (liberal: accept {u,v,w} or {from,to,weight})
                List<Edge> edges = new ArrayList<>();
                JsonArray edgesArray = graphObj.getAsJsonArray("edges");
                for (JsonElement edgeElem : edgesArray) {
                    JsonObject edgeObj = edgeElem.getAsJsonObject();

                    String from, to;
                    int weight;

                    if (edgeObj.has("u")) {
                        from = edgeObj.get("u").getAsString();
                        to = edgeObj.get("v").getAsString();
                        weight = edgeObj.get("w").getAsInt();
                    } else {
                        from = edgeObj.get("from").getAsString();
                        to = edgeObj.get("to").getAsString();
                        weight = edgeObj.get("weight").getAsInt();
                    }

                    edges.add(new Edge(from, to, weight));
                }

                Graph graph = new Graph(nodes, edges);
                inputs.add(new InputGraph(graphId, graph));
            }

            return inputs;
        }
    }

    /**
     * Write MST results to JSON file with exact output schema.
     */
    public static void writeResults(String path, List<OutputRecord> records) throws IOException {
        JsonObject root = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        for (OutputRecord record : records) {
            JsonObject graphResult = new JsonObject();
            graphResult.addProperty("graph_id", record.graphId);

            // Input stats
            JsonObject inputStats = new JsonObject();
            inputStats.addProperty("vertices", record.vertexCount);
            inputStats.addProperty("edges", record.edgeCount);
            graphResult.add("input_stats", inputStats);

            // Prim result
            JsonObject primResult = new JsonObject();
            primResult.add("mst_edges", createEdgesArray(record.primEdges));
            primResult.addProperty("total_cost", record.primTotalCost);
            primResult.addProperty("operations_count", record.primOpsCount);
            primResult.addProperty("execution_time_ms", record.primTimeMs);
            graphResult.add("prim", primResult);

            // Kruskal result
            JsonObject kruskalResult = new JsonObject();
            kruskalResult.add("mst_edges", createEdgesArray(record.kruskalEdges));
            kruskalResult.addProperty("total_cost", record.kruskalTotalCost);
            kruskalResult.addProperty("operations_count", record.kruskalOpsCount);
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

    private static JsonArray createEdgesArray(List<Edge> edges) {
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

    /**
     * Container for input graph with ID.
     */
    public static class InputGraph {
        public final String id;
        public final Graph graph;

        public InputGraph(String id, Graph graph) {
            this.id = id;
            this.graph = graph;
        }
    }

    /**
     * Container for output record with all required fields.
     */
    public static class OutputRecord {
        public String graphId;
        public int vertexCount;
        public int edgeCount;

        public List<Edge> primEdges;
        public int primTotalCost;
        public long primOpsCount;
        public double primTimeMs;

        public List<Edge> kruskalEdges;
        public int kruskalTotalCost;
        public long kruskalOpsCount;
        public double kruskalTimeMs;

        public OutputRecord(String graphId, int vertexCount, int edgeCount) {
            this.graphId = graphId;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
        }
    }
}

