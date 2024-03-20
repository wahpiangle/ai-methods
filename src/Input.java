import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Input {
    Map<String, Bin> bins;

    public Input() {
        this.bins = new HashMap<>();
    }

    public void addBin(String id, int numberOfItems, int capacity) {
        bins.put(id, new Bin(id, numberOfItems, capacity));
    }

    public void addItemToBin(String binId, int weight, int count) {
        Bin bin = bins.get(binId);
        if (bin != null) {
            bin.addItem(weight, count);
        }
    }

    public void getBinsFromTextFile() {
        try {
            File file = new File("BPP.txt");
            Scanner scanner = new Scanner(file);

            String currentBinId = null;
            int numberOfItems = 0;
            int capacity = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.contains("TEST")) {
                    currentBinId = line;
                    numberOfItems = Integer.parseInt(scanner.nextLine().trim());
                    capacity = Integer.parseInt(scanner.nextLine().trim());
                    this.addBin(currentBinId, numberOfItems, capacity);
                } else if (line.matches("\\d+\\s+\\d+")) {
                    Pattern intPattern = Pattern.compile("\\d+");
                    Matcher matcher = intPattern.matcher(line);
                    int[] weightAndCount = new int[2];
                    int i = 0;
                    while (matcher.find()) {
                        weightAndCount[i++] = Integer.parseInt(matcher.group());
                    }
                    int weight = weightAndCount[0];
                    int count = weightAndCount[1];
                    this.addItemToBin(currentBinId, weight, count);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
}