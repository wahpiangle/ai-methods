import java.util.ArrayList;
import java.util.List;

public class FirstFitDecreasing {

    private ArrayList<Problem> problems;

    public FirstFitDecreasing() {
        this.problems = new ArrayList<>();
    }


    public void solve() {
        for (Problem problem : problems) {
            List<Item> items = problem.items;
            int binCapacity = problem.capacityOfEachBin;

            // Sort items in non-increasing order of weight
            items.sort((a, b) -> Integer.compare(b.weight, a.weight));

            System.out.println("Problem " + problem.id + ": Order of items after sorting (decreasing weight):");
            for (Item item : items) {
                System.out.println("Item weight: " + item.weight);
            }

            // Apply First Fit Decreasing algorithm
            List<Bin> bins = new ArrayList<>();
            for (Item item : items) {
                boolean placed = false;
                // Try to place item in existing bins
                for (Bin bin : bins) {
                    if (bin.addItemFFD(item.weight)) {
                        placed = true;
                        break;
                    }
                }
                // If item cannot fit in existing bins, create a new bin
                if (!placed) {
                    Bin newBin = new Bin(binCapacity);
                    newBin.addItemFFD(item.weight);
                    bins.add(newBin);
                }
            }
            System.out.println("Problem " + problem.id + ": Number of bins used = " + bins.size());
        }
    }


    public void setInput(Input input) {
        this.problems = input.problems;
    }

    public static void main(String[] args) {
        Input input = new Input();
        input.getBinsFromTextFile();

        FirstFitDecreasing solver = new FirstFitDecreasing();
        solver.setInput(input);
        solver.solve();
    }
}
