import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class LineChartExample extends JFrame {

    public LineChartExample(String title) {
        super(title);

        // Create a dataset
        DefaultCategoryDataset dataset = createDataset();

        // Create a JFreeChart
        JFreeChart chart = ChartFactory.createLineChart(
                "Sample Line Chart", // chart title
                "X", // x-axis label
                "Y", // y-axis label
                dataset // data
        );

        // Create a chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add some sample data
        dataset.addValue(1.0, "Series 1", "Category 1");
        dataset.addValue(2.0, "Series 1", "Category 2");
        dataset.addValue(3.0, "Series 1", "Category 3");
        dataset.addValue(4.0, "Series 1", "Category 4");
        dataset.addValue(5.0, "Series 1", "Category 5");

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LineChartExample example = new LineChartExample("Line Chart Example");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
