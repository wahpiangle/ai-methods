import java.util.ArrayList;
import java.util.List;

public class BestFit {

    public static void main(String[] args) {
        Input input = new Input();
        input.getBinsFromTextFile();
        List<Problem> problems = input.problems;
        for (Problem problem : problems) {
            List<Bin> bestSolution = solveProblem(problem);
            System.out.println("Total cost for problem: " + problem.id + ":" + bestSolution.size());
        }
    }

    private static List<Bin> solveProblem(Problem problem) {
        List<Bin> currentSolution = new ArrayList<>();
        List<Item> remainingItems = new ArrayList<>(problem.items);

        while (!remainingItems.isEmpty()) {
            Item currentItem = remainingItems.get(0);

            Bin bestFitBin = null;
            int minRemainingCapacity = Integer.MAX_VALUE; // Initialize with max value as getRemainingCapacity is initialized as 0 so cannot use

            for (Bin bin : currentSolution) {
                int remainingCapacity = bin.getRemainingCapacity();
                if (remainingCapacity >= currentItem.weight && remainingCapacity < minRemainingCapacity) {
                    bestFitBin = bin;
                    minRemainingCapacity = remainingCapacity;
                }
            }

            if (bestFitBin != null) {
                bestFitBin.addItem(currentItem.weight, 1);
                remainingItems.remove(currentItem); // Remove placed item
            } else {
                // No bin can fit so create a new one
                Bin newBin = new Bin(problem.capacityOfEachBin);
                newBin.addItem(currentItem.weight, 1);
                currentSolution.add(newBin);
                remainingItems.remove(currentItem); // Remove placed item from remaining items array
            }
        }

        return currentSolution;
    }
}


