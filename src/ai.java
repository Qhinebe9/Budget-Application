import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class ai extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create dataset
        DefaultCategoryDataset dataset = createDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createStackedBarChart("Stacked Bar Chart Example","Category","Value",dataset);

        // Create a ChartPanel to embed the JFreeChart (Swing component)
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        // Create a SwingNode to wrap the ChartPanel
        SwingNode swingNode = new SwingNode();
        // Set the Swing component inside the SwingNode
        swingNode.setContent(chartPanel);

        // Create a StackPane to hold the SwingNode
        StackPane root = new StackPane();
        root.getChildren().add(swingNode);

        // Create a JavaFX Scene and set it on the Stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("JavaFX with JFreeChart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create a dataset for the chart
    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add data for series (you can modify this based on your data)
        dataset.addValue(1.0, "Series1", "Category1");
        dataset.addValue(2.0, "Series2", "Category1");
        dataset.addValue(3.0, "Series3", "Category1");

        dataset.addValue(1.5, "Series1", "Category2");
        dataset.addValue(2.5, "Series2", "Category2");
        dataset.addValue(1.0, "Series3", "Category2");

        dataset.addValue(2.0, "Series1", "Category3");
        dataset.addValue(1.5, "Series2", "Category3");
        dataset.addValue(2.5, "Series3", "Category3");

        return dataset;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
