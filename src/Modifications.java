import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
public class Modifications extends Canvas implements Navigation, Design{
	
	Scene scene;
	String name,path,resetday;
	double dblnetpay;
	public Button btntransact;
	public Button btnprevtransact;
	public Button btnback;
	Label lblname;
	ImageView imgprofileview;
	ResultSet rs;
	Statement stm;
	Image imgprofile;
	public File txtplan= new File("docs/Plan.txt");
	public Modifications()
	{
		try {
			stm=  Main.con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.createPage();
		
	}
    @SuppressWarnings("resource")
	void createPage()
	{
    	//
        //font
        //
    	//rootnode
		 Group roothome= new Group();
		 
		 
		 //back button
		 btnback= new Button("Back");
		 btnback.setPrefSize(100, 30);
		 Design.Layout(btnback, 20, 20, roothome);
		 btnback.setFont(Design.ButtonFont());
		 btnback.setStyle(Design.ButtonStyle());
		 
		 
		// Header Section
	        Label title = new Label("Edit Budget");
	        title.setFont(Design.HeadingFont());
	        Design.Layout(title, Design.GetX(50), Design.GetY(3), roothome);
	        
	        
	        //personal details heading
	        Label lbldetails = new Label("Personal details");
	        lbldetails.setFont(Design.HeadingFont());
	        Design.Layout(lbldetails, Design.GetX(25), Design.GetY(20), roothome);
	        
	        
	        //personal details set up
	        try(Scanner txtin = new Scanner(txtplan)) 
			  { while(txtin.hasNext()) 
			  { name=txtin.nextLine(); 
			  path=txtin.nextLine(); 
			  resetday=txtin.nextLine();
			  
			  } 
			  }
			  catch(FileNotFoundException ex) 
			  { ex.printStackTrace(); }
	        Label lbldetailsexplainer= new Label("Change your details in the fields and click save");
	        lbldetailsexplainer.setFont(Design.ButtonFont());
	        Design.Layout(lbldetailsexplainer, Design.GetX(15), Design.GetY(10), roothome);
	        
	        TextField txtname = new TextField();
	        txtname.setPromptText(name);
	        txtname.setPrefWidth(200);
	        Design.Layout(txtname, Design.GetX(20), Design.GetY(15), roothome);
	        
	        TextField txtpath = new TextField();
	        txtname.setPromptText(path);
	        txtname.setPrefWidth(200);
	        Design.Layout(txtpath, Design.GetX(20), Design.GetY(25), roothome);
	        
	        TextField txtresetday= new TextField(resetday);
	        txtresetday.setPrefWidth(200);
	        Design.Layout(txtresetday, Design.GetX(20), Design.GetY(35), roothome);
	        
	        //Saving personal details button
	        Button btndetails= new Button("Save");
	        btndetails.setFont(Design.ButtonFont());
	        btndetails.setStyle(Design.ButtonStyle());
	        btndetails.setOnAction(e->{
	        	PrintWriter writer = null;
	            try {
	                writer = new PrintWriter("docs/Plan.txt");
	                // Clear contents
	                writer.print("");
	                writer.println(txtname.getText());
	                writer.println(path); 
	                writer.println(txtresetday.getText());
	                
	                // Flush to ensure writing occurs
	                writer.flush();
	                
	                System.out.println("File cleared successfully");
	                
	            } catch (FileNotFoundException ex) {
	                System.out.println("Error: Could not open file.");
	                ex.printStackTrace();
	            } finally {
	                // Ensure the writer is closed even if an exception occurs
	                if (writer != null) {
	                    writer.close();
	                }
	            }
	        });
	        
	      // expenses table
	        TableView<ObservableList<Object>> table= new TableView<>();
	        TableColumn<ObservableList<Object>, String> namecol=new TableColumn<>("Name");
	        namecol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(1)));
	        TableColumn<ObservableList<Object>, String> categorycol=new TableColumn<>("Category");
	        TableColumn<ObservableList<Object>, Double> amountcol=new TableColumn<>("Budgeted Amount");
	        amountcol.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue().get(4)).asObject());
	        Design.Layout(table, Design.GetX(20), Design.GetY(50), roothome);
	       //ObservableList for entries
	        ObservableList<ObservableList<Object>> data= FXCollections.observableArrayList();
	        
	     // Transaction List
	        try { 
			      Statement stm= Main.con.createStatement(); 
				   rs=stm.executeQuery("SELECT  name , setAmount\r\n"
				  		                      + ",category AS item_category FROM items\r\n"
				  		                      + "where itemtype='expense';"); 
				  VBox transactionList = new VBox(10);
			      transactionList.setPadding(new Insets(10));
			      transactionList.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10px; -fx-background-radius: 10px;");
				  while (rs.next()) 
				  {  
					  double amount=rs.getDouble("setAmount");
					  String name=rs.getString("name");
					  String category=rs.getString("item_category");
					  //adding to table
					  data.add(FXCollections.observableArrayList(name,category,amount));
					  table.setItems(data);
				  }
				  
			      table.getColumns().add(namecol);
			      table.getColumns().add(categorycol);
			      table.getColumns().add(amountcol);
	        } catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        //incomes table
	        TableView<ObservableList<Object>> inctable= new TableView<>();
	        namecol=new TableColumn<>("Name");
	        namecol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(1)));
	        categorycol=new TableColumn<>("Category");
	        amountcol=new TableColumn<>("Amount");
	        amountcol.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue().get(4)).asObject());
	        Design.Layout(inctable, Design.GetX(50), Design.GetY(10), roothome);
	       //ObservableList for entries
	        ObservableList<ObservableList<Object>> incdata= FXCollections.observableArrayList();
	        
	     // Transaction List
	        try { 
			      Statement stm= Main.con.createStatement(); 
			      rs=stm.executeQuery("SELECT  name , setAmount\r\n"
		                      + ",category AS item_category FROM items\r\n"
		                      + "where itemtype='income';");
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
					  //adding to table
					  incdata.add(FXCollections.observableArrayList(strdate,name,category,amount));
					  inctable.setItems(incdata);
				  }
	
			      table.getColumns().add(namecol);
			      table.getColumns().add(categorycol);
			      table.getColumns().add(amountcol);
	        } catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	        
	     
	        
	      //btnback processing
	         btnback.setOnAction(e->{
	        	 Navigation.toHomepage();
	         });
	         
	         
	         
	        

		 scene= new Scene(roothome);
		 //colour of the scene
		 scene.setFill(new LinearGradient(0, 0, 1, 1, true,CycleMethod.NO_CYCLE,new Stop(0, Color.web("#ff7f50")),new Stop(1, Color.web("#6a5acd"))));
		 //linear-gradient(to bottom right, #ff7f50, #6a5acd)
	}
	public Scene getscene()
	{
		return scene;
	}
}