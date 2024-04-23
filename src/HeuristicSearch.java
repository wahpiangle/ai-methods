import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

public class HeuristicSearch {

    // private static final int MAX_ITERATIONS = 1000;
    private static final int MAX_DEPTH = 100;

    public static void main(String[] args) {

        Input input = new Input();
        input.getBinsFromTextFile();

        List<Problem> problems = input.problems;
        for (Problem problem : problems) {
            List<Bin> initialSolution = new ArrayList<>();
            for (int i = 0; i < problem.items.size(); i++) {
                // Create a bin for each item
                Bin bin = new Bin(problem.capacityOfEachBin);
                bin.addItem(problem.items.get(i).weight, 1);
                initialSolution.add(bin);
            }

            List<Bin> bestSolution = iterativeDeepeningSearch(initialSolution);
            // System.out.println("Best solution for problem " + problem.id + ":");
            // for (Bin bin : bestSolution) {
            // System.out.println("Bin with capacity " + bin.capacity + " contains:");
            // for (Item item : bin.items) {
            // System.out.println("Item " + item.weight);
            // }
            // }
            System.out.println("Total cost for problem " + problem.id + ": " + calculateTotalCost(bestSolution));
        }
    }

    // private static List<Bin> heuristicSearch(List<Bin> initialSolution) {
    // List<Bin> currentSolution = new ArrayList<>(initialSolution);
    // List<Bin> bestSolution = new ArrayList<>(currentSolution);

    // // Perform greedy search
    // for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
    // List<Bin> newSolution = getNeighborSolution(currentSolution);

    // // Update best solution if new solution is better
    // if (calculateTotalCost(newSolution) < calculateTotalCost(bestSolution)) {
    // bestSolution = newSolution;
    // }
    // }
    // return bestSolution;
    // }

    private static List<Bin> getNeighborSolution(List<Bin> solution) {
        // Find the most full bin and a suitable target bin
        Bin mostFullBin = null;
        Bin targetBin = null;
    
        for (Bin bin : solution) {
            if (mostFullBin == null || bin.getAvailableCapacity() < mostFullBin.getAvailableCapacity()) {
                mostFullBin = bin;
            }
            // Prioritize bins with more available space relative to their capacity
            if (targetBin == null || (double) bin.getAvailableCapacity() / bin.capacity > (double) targetBin.getAvailableCapacity() / targetBin.capacity) {
                targetBin = bin;
            }
        }
    
        // If found, try moving the largest item from the most full bin to the target bin
        if (mostFullBin != null && !mostFullBin.items.isEmpty()) {
            Item largestItem = mostFullBin.items.stream().max(Comparator.comparing(Item::getWeight)).orElse(null);
            if (largestItem != null && targetBin.canAddItem(largestItem.weight)) {
                mostFullBin.removeItem(largestItem.weight);
                targetBin.addItem(largestItem.weight, 1);
                return solution; // Exit after making one modification
            }
        }
    
        // No valid move with largest item found, try other items
        if (mostFullBin != null && !mostFullBin.items.isEmpty()) {
            for (Item item : mostFullBin.items) {
                if (targetBin.canAddItem(item.weight)) {
                    mostFullBin.removeItem(item.weight);
                    targetBin.addItem(item.weight, 1);
                    return solution; // Exit after making one modification
                }
            }
        }
    
        // No valid move found, return the original solution
        return solution;
    }
    
    
    
    private static List<Bin> iterativeDeepeningSearch(List<Bin> initialSolution) {
        List<Bin> bestSolution = null;
        // Perform depth-limited search iteratively
        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            List<Bin> currentSolution = new ArrayList<>(initialSolution);
            List<Bin> newSolution = depthLimitedSearch(currentSolution, depth);
            if (bestSolution == null || calculateTotalCost(newSolution) < calculateTotalCost(bestSolution)) {
                bestSolution = newSolution;
            }
        }
        return bestSolution;
    }

    private static List<Bin> depthLimitedSearch(List<Bin> solution, int depthLimit) {
        return depthLimitedSearchHelper(solution, depthLimit, 0);
    }

    private static List<Bin> depthLimitedSearchHelper(List<Bin> solution, int depthLimit, int currentDepth) {
        if (currentDepth >= depthLimit) {
            return solution;
        }

        // Generate successors
        List<Bin> successors = getNeighborSolution(solution);

        List<Bin> bestSolution = new ArrayList<>(solution);
        // Recursively search each successor
        for (Bin successor : successors) {
            List<Bin> newSolution = depthLimitedSearchHelper(Arrays.asList(successor), depthLimit, currentDepth + 1);
            if (calculateTotalCost(newSolution) < calculateTotalCost(bestSolution)) {
                bestSolution = newSolution;
            }
        }
        return bestSolution;
    }

    private static double calculateTotalCost(List<Bin> solution) {
        double totalCost = 0.0;
        for (Bin bin : solution) {
            totalCost += bin.getTotalWeight(); // Assuming you have a method to get total weight of items in a bin
        }
        return totalCost;
    }
}
