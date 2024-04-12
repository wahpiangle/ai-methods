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
        List<Bin> initialSolution = new ArrayList<>();
        for (int i = 0; i < problem.items.size(); i++) {
            Bin bin = new Bin(problem.capacityOfEachBin);
            bin.addItem(problem.items.get(i).weight, 1);
            initialSolution.add(bin);
        }

        List<Bin> currentSolution = new ArrayList<>(initialSolution);
        List<Bin> bestSolution = new ArrayList<>(currentSolution);

        for (Item item : problem.items){
            Bin bestBin = null;
            int minRemainingCapacity = Integer.MAX_VALUE;

            for (Bin bin : currentSolution) {
                if (bin.canFit(item.weight) && bin.getRemainingCapacity() < minRemainingCapacity) {
                    bestBin = bin;
                    minRemainingCapacity = bin.getRemainingCapacity();
                }
            }

            if (bestBin != null) {
                bestBin.addItem(item.weight, 1);
            } else {
                // If no suitable bin is found, create a new bin and place the item into it
                Bin newBin = new Bin(problem.capacityOfEachBin);
                newBin.addItem(item.weight, 1);
                currentSolution.add(newBin);
            }
        }

        if (currentSolution.size() < bestSolution.size()) {
            bestSolution = currentSolution;
        }

        return bestSolution;
    }
}
