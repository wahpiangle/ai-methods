import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class SimulatedAnnealing {
    static final int DEFAULT_TEMPERATURE = 5000;
    static final double DEFAULT_COOLING_RATE = 0.001;
    static final int DEFAULT_ITERATIONS = 10;

    public static void main(String[] args) {
        Input input = new Input();
        input.getBinsFromTextFile();
        int[] initialTemperatures = {10, 100, 500, 1000, 5000, 10000, 20000, 50000, 100000, 200000};
        double[] coolingRates = {1.0, 0.5, 0.1, 0.05, 0.01, 0.005, 0.001, 0.0005, 0.0001, 0.00005};
        List<Problem> problems = input.problems;
        plotCoolingRateAgainstCost(coolingRates, problems);
//        plotTemperatureAgainstCost(initialTemperatures, problems);
//        plotIterationsAgainstCost(problems, initialTemperatures, coolingRates);
    }

//    private static void plotIterationsAgainstCost(List<Problem> problems, int[] initialTemperatures, double[] coolingRates) {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//            for (double temp : initialTemperatures) {
//                double iterations = 0;
//                for (int i = 0; i < DEFAULT_ITERATIONS; i++) {
//                    for (Problem problem : problems) {
//                        iterations += solveProblem(problem, temp, DEFAULT_COOLING_RATE);
//                    }
//                dataset.addValue(iterations, "Cost", String.valueOf(temp) + " " + String.valueOf(DEFAULT_COOLING_RATE*100) + "%");
//            }
//        }
//        generateBarChart(dataset, "Iterations");
//    }
//
//    private static int solveProblem(Problem problem, double temperature, double coolingRate) {
//        List<Bin> initialSolution = new ArrayList<>();
//        for (int i = 0; i < problem.items.size(); i++) {
//            Bin bin = new Bin(problem.capacityOfEachBin);
//            bin.addItem(problem.items.get(i).weight, 1);
//            initialSolution.add(bin);
//        }
//
//        List<Bin> currentSolution = new ArrayList<>(initialSolution);
//        List<Bin> bestSolution = new ArrayList<>(currentSolution);
//        int iterations = 0;
//        while (temperature > 1) {
//            List<Bin> newSolution = getNeighborSolution(currentSolution);
//            double currentEnergy = calculateTotalCost(currentSolution);
//            double newEnergy = calculateTotalCost(newSolution);
//
//            if (acceptanceProbability(currentEnergy, newEnergy, temperature) > Math.random()) {
//                currentSolution = newSolution;
//                if (newEnergy < calculateTotalCost(bestSolution)) {
//                    bestSolution = new ArrayList<>(newSolution);
//                }
//            }
//            temperature *= 1 - coolingRate;
//            iterations++;
//        }
//        return iterations;
//    }

    private static List<Bin> solveProblem(Problem problem, double temperature, double coolingRate) {
        List<Bin> initialSolution = new ArrayList<>();
        for (int i = 0; i < problem.items.size(); i++) {
            Bin bin = new Bin(problem.capacityOfEachBin);
            bin.addItem(problem.items.get(i).weight, 1);
            initialSolution.add(bin);
        }

        List<Bin> currentSolution = new ArrayList<>(initialSolution);
        List<Bin> bestSolution = new ArrayList<>(currentSolution);
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Current solution");
        while (temperature > 1) {
            List<Bin> newSolution = getNeighborSolution(currentSolution);
            double currentEnergy = calculateTotalCost(currentSolution);
            double newEnergy = calculateTotalCost(newSolution);

            if (acceptanceProbability(currentEnergy, newEnergy, temperature) > Math.random()) {
                currentSolution = newSolution;
                if (newEnergy < calculateTotalCost(bestSolution)) {
                    bestSolution = new ArrayList<>(newSolution);
                }
            }
            series.add(temperature, calculateTotalCost(currentSolution));
            temperature *= 1 - coolingRate;
        }
        dataset.addSeries(series);
//        plotChart(dataset, problem);
        return bestSolution;
    }


    private static List<Bin> getNeighborSolution(List<Bin> solution) {
        List<Bin> neighborSolution = new ArrayList<>(solution);
        Random random = new Random();

        Bin bin1 = neighborSolution.get(random.nextInt(neighborSolution.size()));
        Bin bin2 = neighborSolution.get(random.nextInt(neighborSolution.size()));
        if (!bin1.items.isEmpty()) {
            Item item = bin1.items.get(random.nextInt(bin1.items.size()));
            if (bin2.getRemainingCapacity() >= item.weight) {
                bin1.removeItem(item.weight);
                bin2.addItem(item.weight, 1);
            }
        }
        neighborSolution.removeIf(bin -> bin.items.isEmpty());
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

    private static void plotChart(XYSeriesCollection dataset, Problem problem) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Simulated Annealing for problem " + problem.id,
                "Temperature",
                "Cost",
                dataset
        );
        chart.getXYPlot().getDomainAxis().setInverted(true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Cost function");
        frame.setContentPane(chartPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private static void plotCoolingRateAgainstCost(double[] coolingRates, List<Problem> problems) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (double rate : coolingRates) {
            addAverageCostDatasetValue(problems, DEFAULT_TEMPERATURE, rate, dataset, (rate * 100 + "%" ));
        }

        generateBarChart(dataset, "Cooling rate");
    }

    private static void plotTemperatureAgainstCost(int[] initialTemperatures, List<Problem> problems) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int temp : initialTemperatures) {
            addAverageCostDatasetValue(problems, temp, DEFAULT_COOLING_RATE, dataset, String.valueOf(temp));
        }

        generateBarChart(dataset, "Temperature");
    }

    private static void addAverageCostDatasetValue(List<Problem> problems, int temp, double coolingRate, DefaultCategoryDataset dataset, String s) {
        double averageCost = 0;
        for (int i = 0; i < DEFAULT_ITERATIONS; i++) {
            for (Problem problem : problems) {
                averageCost += calculateTotalCost(solveProblem(problem, temp, coolingRate));
            }
        }
        averageCost /= DEFAULT_ITERATIONS;
        dataset.addValue(averageCost, "Cost", s);
    }

    private static void generateBarChart(DefaultCategoryDataset dataset, String categoryAxisLabel) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Simulated Annealing",
                categoryAxisLabel,
                "Cost",
                dataset
        );
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Effects of parameter values on cost function");
        frame.setContentPane(chartPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
