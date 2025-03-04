

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ai extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Homepage Layout
        VBox homepage = new VBox(20);
        homepage.setPadding(new Insets(20));
        homepage.setStyle("-fx-background-color: #f4f4f4;");
        Label title = new Label("Budget Overview");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        Button transactButton = new Button("Go to Transact Page");
        Button previousTransactionsButton = new Button("View Previous Transactions");
        ProgressBar spendingPercentage = new ProgressBar(0.6);
        homepage.getChildren().addAll(title, spendingPercentage, transactButton, previousTransactionsButton);

        // Transact Page Layout
        VBox transactPage = new VBox(20);
        transactPage.setPadding(new Insets(20));
        transactPage.setStyle("-fx-background-color: #e8f0fe;");
        Label transactTitle = new Label("Make a Transaction");
        ChoiceBox<String> budgetItems = new ChoiceBox<>();
        budgetItems.getItems().addAll("Food", "Rent", "Entertainment");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");
        RadioButton addButton = new RadioButton("Addition");
        RadioButton deductButton = new RadioButton("Deduction");
        ToggleGroup transactionType = new ToggleGroup();
        addButton.setToggleGroup(transactionType);
        deductButton.setToggleGroup(transactionType);
        Button submitButton = new Button("Submit");
        transactPage.getChildren().addAll(transactTitle, budgetItems, amountField, addButton, deductButton, submitButton);

        // Previous Transactions Page Layout
        VBox previousTransactions = new VBox(20);
        previousTransactions.setPadding(new Insets(20));
        previousTransactions.setStyle("-fx-background-color: #f4f4f4;");
        Label previousTitle = new Label("Previous Transactions");
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search Transactions");
        ListView<String> transactionsList = new ListView<>();
        transactionsList.getItems().addAll("Rent - R5000", "Food - R1200", "Netflix - R200");
        previousTransactions.getChildren().addAll(previousTitle, searchBar, transactionsList);

        // Graphs Page Layout
        VBox graphsPage = new VBox(20);
        graphsPage.setPadding(new Insets(20));
        graphsPage.setStyle("-fx-background-color: #f4f4f4;");
        Label graphTitle = new Label("Spending Trends");
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 100));
        series.getData().add(new XYChart.Data<>(2, 150));
        series.getData().add(new XYChart.Data<>(3, 200));
        lineChart.getData().add(series);
        graphsPage.getChildren().addAll(graphTitle, lineChart);

        // Modifications Page Layout
        VBox modificationsPage = new VBox(20);
        modificationsPage.setPadding(new Insets(20));
        modificationsPage.setStyle("-fx-background-color: #e8f0fe;");
        Label modTitle = new Label("Modify Details");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name");
        Button saveButton = new Button("Save Changes");
        modificationsPage.getChildren().addAll(modTitle, nameField, saveButton);

        // Scene Switching
        Scene homepageScene = new Scene(homepage, 800, 600);
        Scene transactScene = new Scene(transactPage, 800, 600);
        Scene previousTransactionsScene = new Scene(previousTransactions, 800, 600);
        Scene graphsScene = new Scene(graphsPage, 800, 600);
        Scene modificationsScene = new Scene(modificationsPage, 800, 600);

        transactButton.setOnAction(e -> primaryStage.setScene(transactScene));
        previousTransactionsButton.setOnAction(e -> primaryStage.setScene(previousTransactionsScene));
        submitButton.setOnAction(e -> primaryStage.setScene(homepageScene));
        saveButton.setOnAction(e -> primaryStage.setScene(homepageScene));

        primaryStage.setTitle("Budget Affiliation App");
        primaryStage.setScene(homepageScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
