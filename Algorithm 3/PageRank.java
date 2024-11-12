import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class PageRank {
    private static final double DAMPING_FACTOR = 0.85;
    private static final int ITERATIONS = 10;
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        // Hardcoded input file path
        String inputFile = "D:\\AlgorithmProject\\Algorthm 3\\input.gz";

        Map<String, List<String>> graph = new HashMap<>();

        // Step 1: Read the graph from the .gz file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(inputFile))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by whitespace
                String[] parts = line.split("\\s+");

                // Check that the line contains at least two elements
                if (parts.length < 2) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;  // Skip to the next line if this one is invalid
                }

                String fromPage = parts[0];
                String toPage = parts[1];

                graph.computeIfAbsent(fromPage, k -> new ArrayList<>()).add(toPage);
                graph.putIfAbsent(toPage, new ArrayList<>());  // Ensure every page is in the graph
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        // Step 2: Initialize ranks
        Map<String, Double> ranks = new HashMap<>();
        double initialRank = 1.0 / graph.size();
        for (String page : graph.keySet()) {
            ranks.put(page, initialRank);
        }

        // Step 3: Apply PageRank algorithm
        for (int i = 0; i < ITERATIONS; i++) {
            Map<String, Double> newRanks = new HashMap<>();

            for (String page : graph.keySet()) {
                double rankSum = 0.0;

                for (String otherPage : graph.keySet()) {
                    if (graph.get(otherPage).contains(page)) {
                        rankSum += ranks.get(otherPage) / graph.get(otherPage).size();
                    }
                }

                double newRank = (1 - DAMPING_FACTOR) / graph.size() + DAMPING_FACTOR * rankSum;
                newRanks.put(page, newRank);
            }

            ranks = newRanks;
        }

        // Step 4: Write output to output.txt
        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            for (Map.Entry<String, Double> entry : ranks.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + e.getMessage());
        }
    }
}
