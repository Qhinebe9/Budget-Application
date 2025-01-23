import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ai extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set the title of the window
        primaryStage.setTitle("Complex Budget Application");

        // Create layout containers
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;"); // Background color

        // Title Label
        Label appTitle = new Label("Welcome to Your Budget Tracker");
        appTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        appTitle.setAlignment(Pos.CENTER);

        // Budget input section
        VBox budgetSection = new VBox(10);
        Label budgetLabel = new Label("Enter Total Budget:");
        TextField budgetField = new TextField();
        budgetField.setPromptText("e.g., 1000");

        // Expense input section (multiple categories)
        VBox expenseSection = new VBox(10);
        Label expensesLabel = new Label("Enter Expenses by Category:");
        TextField foodExpense = new TextField();
        foodExpense.setPromptText("Food (e.g., 200)");
        TextField rentExpense = new TextField();
        rentExpense.setPromptText("Rent (e.g., 500)");
        TextField transportExpense = new TextField();
        transportExpense.setPromptText("Transportation (e.g., 150)");

        // Submit Button and Result
        Button calculateButton = new Button("Calculate Remaining Budget");
        Label remainingBudgetLabel = new Label("Remaining Budget: $0");
        remainingBudgetLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout for showing summary of expenses
        VBox expenseSummary = new VBox(10);
        expenseSummary.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; -fx-border-color: #cccccc;");
        Label totalExpensesLabel = new Label("Total Expenses: $0");
        Label foodSummaryLabel = new Label("Food: $0");
        Label rentSummaryLabel = new Label("Rent: $0");
        Label transportSummaryLabel = new Label("Transportation: $0");

        // Action for calculating remaining budget
        calculateButton.setOnAction(e -> {
            try {
                // Parse the total budget
                double totalBudget = Double.parseDouble(budgetField.getText());

                // Parse expenses by category
                double food = Double.parseDouble(foodExpense.getText());
                double rent = Double.parseDouble(rentExpense.getText());
                double transport = Double.parseDouble(transportExpense.getText());

                // Calculate total expenses and remaining budget
                double totalExpenses = food + rent + transport;
                double remainingBudget = totalBudget - totalExpenses;

                // Update UI with calculations
                totalExpensesLabel.setText("Total Expenses: $" + totalExpenses);
                foodSummaryLabel.setText("Food: $" + food);
                rentSummaryLabel.setText("Rent: $" + rent);
                transportSummaryLabel.setText("Transportation: $" + transport);
                remainingBudgetLabel.setText("Remaining Budget: $" + remainingBudget);
            } catch (NumberFormatException ex) {
                remainingBudgetLabel.setText("Please enter valid numbers!");
            }
        });

        // Add components to the layout
        budgetSection.getChildren().addAll(budgetLabel, budgetField);
        expenseSection.getChildren().addAll(expensesLabel, foodExpense, rentExpense, transportExpense);
        expenseSummary.getChildren().addAll(totalExpensesLabel, foodSummaryLabel, rentSummaryLabel, transportSummaryLabel);

        // Arrange sections in the main layout
        mainLayout.getChildren().addAll(appTitle, budgetSection, expenseSection, calculateButton, remainingBudgetLabel, expenseSummary);

        // Create the scene and set it on the stage
        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

