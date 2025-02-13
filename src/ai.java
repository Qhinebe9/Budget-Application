import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class ai extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the X and Y axes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X Axis");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y Axis");

        // Create the LineChart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Hover Line Chart Example");

        // Create a series to hold data points
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data Series");

        // Add data points to the series
        series.getData().add(new XYChart.Data<>(1, 10));
        series.getData().add(new XYChart.Data<>(2, 20));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 30));
        series.getData().add(new XYChart.Data<>(5, 25));

        // Add the series to the chart
        lineChart.getData().add(series);

        // Add hover functionality to each data point
        for (XYChart.Data<Number, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip("X: " + data.getXValue() + "\nY: " + data.getYValue());
            Tooltip.install(data.getNode(), tooltip);

            // Optional: Add visual feedback on hover
            data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-background-color: #FFA500;"));
            data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-background-color: #000000;"));
        }

        // Set up the scene
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Line Chart with Hover");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}