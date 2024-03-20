
public class SimulatedAnnealing {
    public static void main(String[] args) {
        Input input = new Input();
        input.getBinsFromTextFile();

        // Accessing bins and their items
        for (Bin bin : input.bins.values()) {
            System.out.println("Bin ID: " + bin.id);
            System.out.println("Number of Items: " + bin.numberOfItems);
            System.out.println("Capacity: " + bin.capacity);
            System.out.println("ss: " + bin.items.size());
        }
    }
}
