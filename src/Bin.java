import java.util.HashMap;
import java.util.Map;

class Bin {
    String id;
    int numberOfItems;
    int capacity;
    Map<Integer, Item> items;

    public Bin(String id, int numberOfItems, int capacity) {
        this.id = id;
        this.numberOfItems = numberOfItems;
        this.capacity = capacity;
        this.items = new HashMap<>();
    }

    public void addItem(int weight, int count) {
        items.put(weight, new Item(weight, count));
    }
}