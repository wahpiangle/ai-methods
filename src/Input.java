import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Input {
    ArrayList<Problem> problems;

    public Input() {
        this.problems = new ArrayList<>();
    }

    public void addProblem(String id, int numberOfDistinctItems, int capacityOfEachBin) {
        problems.add(new Problem(id, numberOfDistinctItems, capacityOfEachBin));
    }

    public void addItemToProblem(String problemId, int weight, int count) {
        Problem problem = problems.stream().filter(p -> p.id.equals(problemId)).findFirst().orElse(null);
        if (problem != null) {
            for (int i = 0; i < count; i++) {
                problem.items.add(new Item(weight));
            }
        } else {
            throw new IllegalArgumentException("Problem with id " + problemId + " not found.");
        }
    }

    public void getBinsFromTextFile() {
        try {
            File file = new File("BPP.txt");
            Scanner scanner = new Scanner(file);

            String currentProblemId = null;
            int numberOfItems = 0;
            int capacity = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.contains("TEST")) {
                    currentProblemId = line;
                    numberOfItems = Integer.parseInt(scanner.nextLine().trim());
                    capacity = Integer.parseInt(scanner.nextLine().trim());
                    this.addProblem(currentProblemId, numberOfItems, capacity);
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
                    this.addItemToProblem(currentProblemId, weight, count);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
}