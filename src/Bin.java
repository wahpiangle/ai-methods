import java.util.ArrayList;

class Bin {
    int capacity;
    ArrayList<Item> items;

    public Bin(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public void addItem(int weight, int count) {
        for (int i = 0; i < count; i++) {
            items.add(new Item(weight));
        }
    }

    public int getRemainingCapacity() {
        int usedCapacity = 0;
        for (Item item : items) {
            usedCapacity += item.weight;
        }
        return capacity - usedCapacity;
    }

    public void removeItem(int weight) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).weight == weight) {
                items.remove(i);
                break;
            }
        }
    }
  
    public int getTotalWeight() {
        int totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.getWeight();
        }
        return totalWeight;
    }

    public int getAvailableCapacity() {
        int totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.weight;
        }
        return capacity - totalWeight;
    }

    public boolean canAddItem(int weight) {
        return getAvailableCapacity() >= weight;

    public boolean addItemFFD(int weight) {
        if (getRemainingCapacity() >= weight) {
            items.add(new Item(weight));
            return true;
        }
        return false;
    }
}