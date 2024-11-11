import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FloydWarshall {

    static final int INF = 99999; // Define a large number as "infinity"
    static final int V = 128; // Number of cities, fixed to 128 based on your data

    // Method to implement the Floyd-Warshall algorithm
    public static void floydWarshall(int[][] graph) {
        int[][] dist = new int[V][V];

        // Initialize the solution matrix as a copy of the input graph matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        // Main algorithm loop
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF &&
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        // Write shortest distance matrix to output file
        writeSolutionToFile(dist, "output.txt");
    }

    // Utility method to write solution matrix to a file
    public static void writeSolutionToFile(int[][] dist, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("The following matrix shows the shortest distances between every pair of cities:\n");
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][j] == INF) {
                        writer.write("INF ");
                    } else {
                        writer.write(dist[i][j] + " ");
                    }
                }
                writer.write("\n");
            }
            System.out.println("Output written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read the graph from a CSV file
    public static int[][] readGraphFromCSV(String fileName) {
        int[][] graph = new int[V][V];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < V) {
                String[] values = line.trim().split("\\s+"); // Split by whitespace
                for (int col = 0; col < values.length && col < V; col++) {
                    try {
                        int distance = Integer.parseInt(values[col].trim());
                        graph[row][col] = (distance == 0 && row != col) ? INF : distance;
                    } catch (NumberFormatException e) {
                        // If parsing fails, assume no connection (INF)
                        graph[row][col] = INF;
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void main(String[] args) {
        String fileName = "D:\\AlgorithmProject\\Algorithm 1\\sgb128_dist.csv"; // Path to the uploaded CSV file
        int[][] graph = readGraphFromCSV(fileName);

        // Run Floyd-Warshall algorithm
        floydWarshall(graph);
    }
}
