package main.java;
import java.io.File;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;

public class Graphs extends Canvas implements Navigation, Design {

	Scene scene;
	Button btnback;
	String name, path, strnetpay;
	double dblnetpay;
	public Button btntransact;
	public Button btnprevtransact;
	public Button btnmore;
	Label lblname;
	ImageView imgprofileview;
	ResultSet rs;
	Statement stm;
	String type = "";
	Image imgprofile;
	LocalDate date = LocalDate.now();
	int intdate = date.getMonthValue();
	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	public File txtplan = new File("docs/Plan.txt");

	public Graphs() {
		try {
			stm = Main.con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.createPage();

	}

	@SuppressWarnings("resource")
	void createPage() {
		// rootnode
		Group roothome = new Group();

		// Label for recent transactions

		// back button
		btnback = new Button("Back");
		btnback.setPrefSize(100, 30);
		Design.Layout(btnback, 20, 20, roothome);
		btnback.setFont(Design.ButtonFont());
		btnback.setStyle(Design.ButtonStyle());

		// current month

		// Section for Budget VS Actual amount
		try {
			Statement stm = Main.con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT \r\n" + "    SUM(subquery.amount) AS totalAmount, \r\n"
					+ "    SUM(subquery.totalSetAmount) AS totalSetAmount, \r\n" + "    subquery.category\r\n"
					+ "FROM \r\n" + "    (SELECT \r\n" + "        transaction.itemid, \r\n"
					+ "        SUM(transaction.amount) AS amount,  -- Sum amount for each itemid\r\n"
					+ "        MAX(items.setAmount) AS totalSetAmount,  -- Ensure setAmount is added once per itemid\r\n"
					+ "        items.category\r\n" + "     FROM transaction\r\n"
					+ "     JOIN items ON transaction.itemid = items.iditems\r\n"
					+ "     WHERE items.itemtype = 'expense'\r\n" + "     GROUP BY \r\n"
					+ "        transaction.itemid, \r\n" + "        items.category) AS subquery\r\n" + "GROUP BY \r\n"
					+ "    subquery.category;\r\n" + "");
			// Bar chart
			CategoryAxis x_axis = new CategoryAxis();
			x_axis.setLabel("Categories");
			NumberAxis y_axis = new NumberAxis();
			BarChart<String, Number> stackedbarchart = new BarChart<>(x_axis, y_axis);
			stackedbarchart.setTitle("Budget vs. Actual Spending");
			List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
			XYChart.Series<String, Number> series1 = new XYChart.Series<>();
			series1.setName("Budgeted");
			XYChart.Series<String, Number> series2 = new XYChart.Series<>();
			series2.setName("Actual");
			while (rs.next()) {
				double amount = rs.getDouble("totalAmount");
				amount = Math.abs(amount);
				String category = rs.getString("category");
				double setamount = rs.getDouble("totalSetAmount");
				double actualspent=setamount-amount;
				series1.getData().add(new XYChart.Data<>(category, setamount));
				series2.getData().add(new XYChart.Data<>(category, amount));

			}
			seriesList.add(series1);
			seriesList.add(series2);
			stackedbarchart.getData().addAll(seriesList);
			Design.Layout(stackedbarchart, Design.GetX(10), Design.GetY(0), roothome);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		// Monthly Spending graph
		try {

			Statement stm = Main.con.createStatement();
			rs = stm.executeQuery("SELECT \r\n" + "    SUM(transaction.amount) AS amount,\r\n"
					+ "DAY(transaction.date) as day FROM \r\n" + "    transaction\r\n" + "JOIN \r\n"
					+ "    items ON transaction.itemid = items.iditems\r\n" + "WHERE MONTH(transaction.date)=" + intdate
					+ " GROUP BY \r\n" + "    DAY(transaction.date) ;\r\n" + "");
			NumberAxis xAxisday = new NumberAxis();
			xAxisday.setLabel("Day of the Month");
			NumberAxis yAxisday = new NumberAxis();
			yAxisday.setLabel("Amount");
			LineChart<Number, Number> daylineChart = new LineChart<>(xAxisday, yAxisday);
			daylineChart.setTitle("Daily Spending");
			XYChart.Series<Number, Number> dayseries = new XYChart.Series<>();
			dayseries.setName("Hover/click data point to view more info.");
			while (rs.next()) {
				double amount = rs.getDouble("amount");
				int day = rs.getInt("day");
				if (amount < 0)
					type = "deduction";
				else
					type = "addition";
				amount = Math.abs(amount);
				dayseries.getData().add(new XYChart.Data<>(day, amount, type));
			}
			daylineChart.getData().add(dayseries);
			Design.Layout(daylineChart, Design.GetX(50), Design.GetY(0), roothome);
			System.out.println(roothome.getChildren().indexOf(daylineChart));
			for (XYChart.Data<Number, Number> data : dayseries.getData()) {
				// Tooltip for hover
				Tooltip tooltip = new Tooltip("Day: " + data.getXValue() + "\nType: " + data.getExtraValue()
						+ "\nAmount:R " + data.getYValue().doubleValue());
				Tooltip.install(data.getNode(), tooltip);

				// Visual feedback on hover
				data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-background-color: #FFA500;"));
				data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-background-color: #000000;"));

				// Click functionality
				data.getNode().setOnMouseClicked(event -> {
					// Display additional information in an alert dialog
					alert.setTitle("Data Point Clicked");
					alert.setHeaderText("Details for Data Point");
					if (data.getYValue().doubleValue() < 0)
						type = "deduction";
					else
						type = "addition";
					alert.setContentText("Day: " + data.getXValue() + "\nType: " + data.getExtraValue() + "\nAmount:R "
							+ data.getYValue());
					alert.showAndWait();
				});
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		// Combobox processing
		ObservableList<String> strMonths = FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
		ComboBox<String> cmbMonths = new ComboBox<String>(strMonths);
		cmbMonths.setStyle(
				"-fx-background-color: #FFFFFF; -fx-background-radius: 15px; -fx-font: 18px \"Comic Sans Ms\";");
		Design.Layout(cmbMonths, Design.GetX(85), Design.GetY(3), roothome);
		cmbMonths.setPrefSize(100, 30);

		cmbMonths.setPromptText(strMonths.get(intdate - 1));
		cmbMonths.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Event triggered when a new selection is made
				int month = cmbMonths.getSelectionModel().getSelectedIndex();
				month++;
				if (month == 0) {
					month = date.getMonthValue();
				}
				PopulateSpending(roothome, month);
			}
		});
		cmbMonths.valueProperty().addListener((observable, oldValue, newValue) -> {
		});

		// Yearly Balance graph
		try {
			rs = stm.executeQuery("SELECT \r\n" + "    SUM(transaction.amount) AS amount,\r\n"
					+ "MONTH(transaction.date) as month FROM \r\n" + "    transaction\r\n" + "JOIN \r\n"
					+ "    items ON transaction.itemid = items.iditems\r\n" + "GROUP BY \r\n"
					+ "    MONTH(transaction.date) ;\r\n" + "");
			strMonths = FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
					"Oct", "Nov", "Dec");
			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Months");
			NumberAxis yAxis = new NumberAxis();
			yAxis.setLabel("Balance");
			LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
			lineChart.setTitle("Monthly Balance");
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName("Hover/click data point to view more info.");
			int index = 0;
			while (rs.next()) {
				double amount = rs.getDouble("amount");
				amount = Math.abs(amount);
				series.getData().add(new XYChart.Data<>(strMonths.get(index), amount));
				index++;
			}
			lineChart.getData().add(series);
			Design.Layout(lineChart, Design.GetX(50), Design.GetY(48), roothome);
			for (XYChart.Data<String, Number> data : series.getData()) {
				// Tooltip for hover
				Tooltip tooltip = new Tooltip("Month: " + data.getXValue() + "\nAmount:R " + data.getYValue());
				Tooltip.install(data.getNode(), tooltip);

				// Visual feedback on hover
				data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-background-color: #FFA500;"));
				data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-background-color: #000000;"));

				// Click functionality
				data.getNode().setOnMouseClicked(event -> {
					// Display additional information in an alert dialog
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Data Point Clicked");
					alert.setHeaderText("Details for Data Point");
					alert.setContentText("Month: " + data.getXValue() + "\nAmount:R " + data.getYValue());
					alert.showAndWait();
				});
			}

			// btnback processing
			btnback.setOnAction(e -> {
				Navigation.toHomepage();
			});

			// Adding components to root node
			roothome.getStyleClass().add("color-palette");
			scene = new Scene(roothome);

			// colour of the scene
			scene.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#DCE8E0")),
					new Stop(1, Color.web("#D2D8D6"))));

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		
		// Yearly Income graph
				try {
					rs = stm.executeQuery("SELECT SUM(transaction.amount) AS amount,\r\n"
							+ " MONTH(transaction.date) as month FROM \r\n"
							+ " transaction JOIN\r\n"
							+ " items ON transaction.itemid = items.iditems where transaction.amount>0\r\n"
							+ " GROUP BY MONTH(transaction.date) ;\r\n" + "");
					strMonths = FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
							"Oct", "Nov", "Dec");
					CategoryAxis xAxis = new CategoryAxis();
					xAxis.setLabel("Months");
					NumberAxis yAxis = new NumberAxis();
					yAxis.setLabel("Balance");
					LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
					lineChart.setTitle("Monthly Income");
					XYChart.Series<String, Number> series = new XYChart.Series<>();
					series.setName("Hover/click data point to view more info.");
					int index = 0;
					while (rs.next()) {
						double amount = rs.getDouble("amount");
						amount = Math.abs(amount);
						series.getData().add(new XYChart.Data<>(strMonths.get(index), amount));
						index++;
					}
					lineChart.getData().add(series);
					Design.Layout(lineChart, Design.GetX(10), Design.GetY(48), roothome);
					for (XYChart.Data<String, Number> data : series.getData()) {
						// Tooltip for hover
						Tooltip tooltip = new Tooltip("Month: " + data.getXValue() + "\nAmount:R " + data.getYValue());
						Tooltip.install(data.getNode(), tooltip);

						// Visual feedback on hover
						data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-background-color: #FFA500;"));
						data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-background-color: #000000;"));

						// Click functionality
						data.getNode().setOnMouseClicked(event -> {
							// Display additional information in an alert dialog
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("Data Point Clicked");
							alert.setHeaderText("Details for Data Point");
							alert.setContentText("Month: " + data.getXValue() + "\nAmount:R " + data.getYValue());
							alert.showAndWait();
						});
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

	}

	public Scene getscene() {
		return scene;
	}

	private void PopulateSpending(Group roothome, int month) {
		try {

			Statement stm = Main.con.createStatement();
			rs = stm.executeQuery("SELECT \r\n" + "    SUM(transaction.amount) AS amount,\r\n"
					+ "DAY(transaction.date) as day FROM \r\n" + "    transaction\r\n" + "JOIN \r\n"
					+ "    items ON transaction.itemid = items.iditems\r\n" + "WHERE MONTH(transaction.date)=" + month
					+ " GROUP BY \r\n" + "    DAY(transaction.date) ;\r\n" + "");
			NumberAxis xAxisday = new NumberAxis();
			xAxisday.setLabel("Day of the Month");
			NumberAxis yAxisday = new NumberAxis();
			yAxisday.setLabel("Amount");
			LineChart<Number, Number> daylineChart = new LineChart<>(xAxisday, yAxisday);
			daylineChart.setTitle("Daily Spending");
			XYChart.Series<Number, Number> dayseries = new XYChart.Series<>();
			dayseries.setName("Hover/click data point to view more info.");
			while (rs.next()) {
				double amount = rs.getDouble("amount");
				int day = rs.getInt("day");
				if (amount < 0)
					type = "deduction";
				else
					type = "addition";
				amount = Math.abs(amount);
				dayseries.getData().add(new XYChart.Data<>(day, amount, type));
			}
			daylineChart.getData().add(dayseries);
			roothome.getChildren().set(2, daylineChart);
			daylineChart.setLayoutX(Design.GetX(50));
			daylineChart.setLayoutY(Design.GetY(0));
			for (XYChart.Data<Number, Number> data : dayseries.getData()) {
				// Tooltip for hover
				Tooltip tooltip = new Tooltip("Day: " + data.getXValue() + "\nType: " + data.getExtraValue()
						+ "\nAmount:R " + data.getYValue().doubleValue());
				Tooltip.install(data.getNode(), tooltip);

				// Visual feedback on hover
				data.getNode().setOnMouseEntered(event -> data.getNode().setStyle("-fx-background-color: #FFA500;"));
				data.getNode().setOnMouseExited(event -> data.getNode().setStyle("-fx-background-color: #000000;"));

				// Click functionality
				data.getNode().setOnMouseClicked(event -> {
					// Display additional information in an alert dialog
					alert.setTitle("Data Point Clicked");
					alert.setHeaderText("Details for Data Point");
					if (data.getYValue().doubleValue() < 0)
						type = "deduction";
					else
						type = "addition";
					alert.setContentText("Day: " + data.getXValue() + "\nType: " + data.getExtraValue() + "\nAmount:R "
							+ data.getYValue());
					alert.showAndWait();
				});
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}
