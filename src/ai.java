import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ai extends Application {

    @Override
    public void start(Stage stage) {
        // Create the X and Y axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        // Create the bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Dynamically Updated Grouped Bar Chart");

        // Create the initial data series
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Category A");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Category B");

        series1.getData().add(new XYChart.Data<>("2025", 50));
        series2.getData().add(new XYChart.Data<>("2025", 30));

        series1.getData().add(new XYChart.Data<>("2026", 80));
        series2.getData().add(new XYChart.Data<>("2026", 40));

        // Add the series to the chart
        barChart.getData().addAll(series1, series2);

        // Create a button to dynamically update the chart
        Button updateButton = new Button("Update Chart");
        updateButton.setOnAction(event -> updateChart(barChart));

        // Add the chart and button to a layout
        StackPane root = new StackPane();
        root.getChildren().add(barChart);
        root.getChildren().add(updateButton);

        // Set the button position
        StackPane.setAlignment(updateButton, javafx.geometry.Pos.BOTTOM_CENTER);

        // Set up the stage and scene
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("JavaFX Grouped Bar Chart");
        stage.setScene(scene);
        stage.show();
    }

    // Method to update the chart dynamically
    private void updateChart(BarChart<String, Number> barChart) {
        // Clear current data
        barChart.getData().clear();

        // Create new data for the chart
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Category A");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Category B");

        series1.getData().add(new XYChart.Data<>("2025", 60));
        series2.getData().add(new XYChart.Data<>("2025", 20));

        series1.getData().add(new XYChart.Data<>("2026", 70));
        series2.getData().add(new XYChart.Data<>("2026", 50));

        // Add the updated data to the chart
        barChart.getData().addAll(series1, series2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
