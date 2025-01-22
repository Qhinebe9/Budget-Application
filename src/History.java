import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class History extends Canvas implements Navigation, Design {
	Button btnadd;
	Button btndeduct;
	Button btnback;
	Scene scene;
	Statement stm;
	ResultSet rs;
	public History()
	{
		this.createPage();
		
	}
    void createPage()
	{
    	//
        //font
        //
    	//rootnode
		 Group roothome= new Group();
		 //
		 //back button
		 //
		 btnback= new Button("Back");
		 btnback.setPrefSize(100, 30);
		 Design.Layout(btnback, 20, 20, roothome);
		 btnback.setFont(Design.ButtonFont());
		 btnback.setStyle(Design.ButtonStyle());
		// Header Section
	        Label title = new Label("Transaction History");
	        title.setFont(Design.HeadingFont());
	        Design.Layout(title, 180, 10, roothome);
	        TextField searchBar = new TextField();
	        searchBar.setPromptText("Search transactions...");
	        searchBar.setPrefWidth(200);
	        Design.Layout(searchBar, 200, 60, roothome);
	        Button filterButton = new Button("Filter");
	        filterButton.setFont(Design.ButtonFont());
	        filterButton.setStyle(Design.ButtonStyle());
	        Design.Layout(filterButton,405,55,roothome);
	     // Transaction List
	        try { 
			      Statement stm= Main.con.createStatement(); 
				   rs=stm.executeQuery("SELECT  date AS transaction_date, amount AS transaction_amount, name AS item_name,\r\n"
				  		                      + "category AS item_category, type AS item_type FROM transaction JOIN items i\r\n"
				  		                      + "ON transaction.itemid = i.iditems ORDER BY transaction_date DESC;"); 
				  VBox transactionList = new VBox(10);
			      transactionList.setPadding(new Insets(10));
			      transactionList.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10px; -fx-background-radius: 10px;");
				  while (rs.next()) 
				  {  
					  Date date=rs.getDate("transaction_date");
					  String strdate=date.toString();
					  double amount=rs.getDouble("transaction_amount");
					  String name=rs.getString("item_name");
					  String category=rs.getString("item_category");
					  String type=rs.getString("item_type");
					  HBox box=createTransactionCard(strdate, type, name, amount);
					  transactionList.getChildren().add(box);  
				  }
				  
				  ScrollPane scrollpane=new ScrollPane(transactionList);
				  scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
				  Design.Layout(scrollpane, 20, 140, roothome);
				  scrollpane.setMaxHeight(500);
				  scrollpane.setMinWidth(400);
				  scrollpane.setFitToWidth(true);
			      scrollpane.setStyle(" border-radius:5px;");
			      rs=stm.executeQuery("SELECT  sum(amount) AS total_income FROM transaction where amount>0"); 
			     // Summary Section
			      HBox summary = new HBox(20);
			      summary.setAlignment(Pos.CENTER);
			      Label totalIncome = null;
			        if (rs.next()) {
			        	double amount=rs.getDouble("total_income");
			        	totalIncome= new Label("Total Income: "+amount);
			        	totalIncome.setFont(Design.H2Font());
				        totalIncome.setTextFill(Color.GREEN);
			        	
			        }
			        rs=stm.executeQuery("SELECT  sum(amount) AS total_expenses FROM transaction where amount<0");
			        Label totalExpenses = null;
				        if (rs.next()) {
				        	double amount=rs.getDouble("total_expenses");
				        	totalExpenses= new Label("Total Expenses: "+amount);
					        totalExpenses.setFont(Design.H2Font());
					        totalExpenses.setTextFill(Color.RED);
				        	
				        }
			        summary.getChildren().addAll(totalIncome, totalExpenses);
			        Design.Layout(summary, 20, 90, roothome);//add scrollpane
			        //
			        //Filter button processing
			        //
			        filterButton.setOnAction(e->{
			        	String searchvalue=searchBar.getText();
			        	System.out.println(searchvalue);
			        	transactionList.getChildren().clear();
					try {
						if (searchvalue.isEmpty())
						{
							rs=stm.executeQuery("SELECT  date AS transaction_date, amount AS transaction_amount, name AS item_name,\r\n"
		  		                      + "category AS item_category, type AS item_type FROM transaction JOIN items i\r\n"
		  		                      + "ON transaction.itemid = i.iditems ORDER BY transaction_date DESC;"); 
							
						}else {
						 rs=stm.executeQuery("SELECT  date AS transaction_date, amount AS transaction_amount, name AS item_name,\r\n"
							  		                      + "category AS item_category, type AS item_type FROM transaction JOIN items i\r\n"
							  		                      + "ON transaction.itemid = i.iditems Where name='"+searchvalue+"' OR  category='"+searchvalue+"' OR type='"+searchvalue+"' ORDER BY transaction_date DESC;");
						}
						if (!rs.next()) {
							HBox box=createTransactionCard("", "", "No transactions found", 00);
							  box.setLayoutY(300);
							  transactionList.getChildren().add(box);
						}
						while (rs.next()) 
						  {  
							  Date date=rs.getDate("transaction_date");
							  String strdate=date.toString();
							  double amount=rs.getDouble("transaction_amount");
							  String name=rs.getString("item_name");
							  String category=rs.getString("item_category");
							  String type=rs.getString("item_type");
							  HBox box=createTransactionCard(strdate, type, name, amount);
							  box.setLayoutY(300);
							  transactionList.getChildren().add(box);
							  
						  }
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
						  
			        	
			        });
			
				  }
				  
				  catch (SQLException e) 
				  { // TODO Auto-generated catch block
				  e.printStackTrace();
				  System.out.println("flop with try");}
	        
	      //btnback processing
	         btnback.setOnAction(e->{
	        	 Navigation.toHomepage();
	         });
	        

		 scene= new Scene(roothome,600,800);
		 //colour of the scene
		 scene.setFill(new LinearGradient(0, 0, 1, 1, true,CycleMethod.NO_CYCLE,new Stop(0, Color.web("#ff7f50")),new Stop(1, Color.web("#6a5acd"))));
		 //linear-gradient(to bottom right, #ff7f50, #6a5acd)
	}
	public Scene getscene()
	{
		return scene;
	}
	private HBox createTransactionCard(String date, String category, String description, double amount) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 8px; -fx-background-radius: 8px;");
		/*
		 * // Category icon (placeholder) ImageView icon = new ImageView(new
		 * Image("file:icon_placeholder.png")); // Update with actual icon path
		 * icon.setFitWidth(40); icon.setFitHeight(40);
		 */

        // Details
        VBox details = new VBox(5);
        Label dateLabel = new Label(date);
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dateLabel.setTextFill(Color.GRAY);
        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        Label categoryLabel = new Label(category);
        categoryLabel.setFont(Font.font("Arial", FontWeight.LIGHT, 12));
        categoryLabel.setTextFill(Color.DARKGRAY);
        details.getChildren().addAll(dateLabel, descriptionLabel, categoryLabel);

        // Amount
        Label amountLabel = new Label(String.format("%s%.2f", amount < 0 ? "-" : "+", Math.abs(amount)));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        amountLabel.setTextFill(amount < 0 ? Color.RED : Color.GREEN);

        card.getChildren().addAll(details, amountLabel);// dont forget to add icon
        HBox.setHgrow(details, Priority.ALWAYS);
        return card;
    }
	

}
