import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;

public class ai extends Application {

    @Override
    public void start(Stage stage) {
        // Create a StackPane to hold the line chart
        StackPane root = new StackPane();
        
        // Example data points (x, y)
        double[] xPoints = {1, 2, 3, 4, 5};
        double[] yPoints = {5, 7, 6, 8, 9};
        
        // Manually scale factors to fit the data points into the window size
        double xScale = 100;  // Scale factor for the x-axis
        double yScale = 50;   // Scale factor for the y-axis

        // Create a Path to draw the curve
        Path path = new Path();
        path.setStroke(Color.BLUE); // Set the line color
        path.setStrokeWidth(2); // Set the line width

        // Move to the first point (Start point)
        MoveTo moveTo = new MoveTo();
        moveTo.setX(xPoints[0] * xScale);  // Scale the x-coordinate
        moveTo.setY(yPoints[0] * yScale);  // Scale the y-coordinate
        path.getElements().add(moveTo); // Add MoveTo to the path

        // Create curves between points using QuadCurveTo
        for (int i = 1; i < xPoints.length - 1; i++) {
            double ctrlX = (xPoints[i] + xPoints[i + 1]) / 2;  // Control point X (midpoint)
            double ctrlY = (yPoints[i] + yPoints[i + 1]) / 2;  // Control point Y (midpoint)

            // Create a quadratic curve between two points
            QuadCurveTo quadCurve = new QuadCurveTo();
            quadCurve.setControlX(ctrlX * xScale);  // Scale control point X
            quadCurve.setControlY(ctrlY * yScale);  // Scale control point Y
            quadCurve.setX(xPoints[i + 1] * xScale);  // Scale the x-coordinate
            quadCurve.setY(yPoints[i + 1] * yScale);  // Scale the y-coordinate
            
            path.getElements().add(quadCurve); // Add the curve to the path
        }

        // Add the curve path to the root pane
        root.getChildren().add(path);

        // Set the scene with the graph and axes
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Curved Line Graph in JavaFX");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
