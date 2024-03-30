import java.util.ArrayList;

public class Problem {
    String id;
    ArrayList<Item> items;
    int numberOfDistinctItems;
    int capacityOfEachBin;

    public Problem(String id, int numberOfDistinctItems, int capacityOfEachBin) {
        this.id = id;
        this.numberOfDistinctItems = numberOfDistinctItems;
        this.capacityOfEachBin = capacityOfEachBin;
        this.items = new ArrayList<>();
    }
}
