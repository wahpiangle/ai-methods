import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    private static final int INITIAL_TEMPERATURE = 1000;
    private static final double COOLING_RATE = 0.003;
    private static final int ITERATIONS_AT_TEMPERATURE = 100;

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

            List<Bin> bestSolution = simulatedAnnealing(initialSolution);
            System.out.println("Best solution for problem " + problem.id + ":");
            for (Bin bin : bestSolution) {
                System.out.println("Bin with capacity " + bin.capacity + " contains:");
                for (Item item : bin.items) {
                    System.out.println("Item " + item.weight);
                }
            }
            System.out.println("Total cost for problem " + problem.id + ": " + calculateTotalCost(bestSolution));
        }
    }

    private static List<Bin> simulatedAnnealing(List<Bin> initialSolution) {
        List<Bin> currentSolution = new ArrayList<>(initialSolution);
        List<Bin> bestSolution = new ArrayList<>(currentSolution);
        double temperature = INITIAL_TEMPERATURE;

        while (temperature > 1) {
            for (int i = 0; i < ITERATIONS_AT_TEMPERATURE; i++) {
                List<Bin> newSolution = getNeighborSolution(currentSolution);
                double currentEnergy = calculateTotalCost(currentSolution);
                double newEnergy = calculateTotalCost(newSolution);

                if (acceptanceProbability(currentEnergy, newEnergy, temperature) > Math.random()) {
                    currentSolution = newSolution;
                    if (newEnergy < calculateTotalCost(bestSolution)) {
                        bestSolution = new ArrayList<>(newSolution);
                    }
                }
            }
            temperature *= 1 - COOLING_RATE;
        }
        return bestSolution;
    }

    private static List<Bin> getNeighborSolution(List<Bin> solution) {
        List<Bin> neighborSolution = new ArrayList<>(solution);
        // Randomly select two bins
        Random random = new Random();

        // Randomly select an item from a random bin and move it to another random bin
        Bin bin1 = neighborSolution.get(random.nextInt(neighborSolution.size()));
        Bin bin2 = neighborSolution.get(random.nextInt(neighborSolution.size()));
        if (bin1.items.size() > 0) {
            Item item = bin1.items.get(random.nextInt(bin1.items.size()));
            if (bin2.getRemainingCapacity() >= item.weight) {
                bin1.removeItem(item.weight);
                bin2.addItem(item.weight, 1);
            }
        }
        neighborSolution.removeIf(bin -> bin.items.size() == 0);
        return neighborSolution;
    }

    private static double calculateTotalCost(List<Bin> solution) {
        return solution.size();
    }

    private static double acceptanceProbability(double currentEnergy, double newEnergy, double temperature) {
        if (newEnergy < currentEnergy) {
            return 1.0;
        }
        return Math.exp((currentEnergy - newEnergy) / temperature);
    }
}
