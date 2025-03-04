package main.java;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.StringConverter;

public class Transaction extends Canvas implements Navigation, Design {
	Button btnadd;
	Button btndeduct;
	Button btnback;
	Scene scene;
	Statement stm;
	ResultSet rs;
	public Transaction()
	{
		this.createPage();
		
	}
    void createPage()
	{
    	//root node
		 Group roothome= new Group();
		 
		 
		 //back button
		 btnback= new Button("Back");
		 btnback.setPrefSize(100, 30);
		 Design.Layout(btnback, 20, 20, roothome);
		 
		 
		 //button styling
		 btnback.setFont(Design.ButtonFont());
		 btnback.setStyle(Design.ButtonStyle());
		 
		 
         Label lblchoice= new Label("What would you like to do?");
         lblchoice.setFont(Design.H2Font());
         Design.Layout(lblchoice, 180, 70, roothome);
         
         
         //Combobox setup
         ObservableList<String> strItems=FXCollections.observableArrayList();
         try { 
		      stm= Main.con.createStatement(); 
			  rs=stm.executeQuery("Select * from items");
			  while (rs.next()) 
			  {
				  String name=rs.getString(2);
				  String type=rs.getString(4);
				  name=name+" ("+type+")";
				  strItems.add(name);  
			  } 
			 }
             catch (SQLException e) {
			  e.printStackTrace();
			  System.out.println("flop with try");
			  }
         ComboBox<String> cmbitems= new ComboBox<String>(strItems);
         cmbitems.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15px; -fx-font: 18px \"Comic Sans Ms\";");
         cmbitems.setPrefSize(280, 40);
         cmbitems.setPromptText("Select Budget Item");
         Design.Layout(cmbitems, 140, 120, roothome);
         
         
         //Amount text field
         TextField txtamount= new TextField();
         txtamount.setText("Amount");
         Design.Layout(txtamount, 210, 180, roothome);
         txtamount.setOnMouseClicked(e->
         {
        	txtamount.clear(); 
         });
         
         
         //Add button
         btnadd= new Button("Add");
         btnadd.setPrefSize(230, 30);
         Design.Layout(btnadd, 170, 250, roothome);
         //Transacting button styling
         btnadd.setFont(Design.ButtonFont());
         btnadd.setStyle(Design.ButtonStyle());
         
         
         //btnadd processing
         btnadd.setOnAction(e->
         {
        	 Pattern netpaypat= Pattern.compile("\\d*[.,]?d{0,2}");
			 Matcher netpaymatcher=netpaypat.matcher((CharSequence) txtamount.getText());
			 if(netpaymatcher.matches())
			 {
				 try {
					 double dbladd=Double.parseDouble(txtamount.getText());
					  stm= Main.con.createStatement();
					  StringTokenizer st= new StringTokenizer(cmbitems.getSelectionModel().getSelectedItem()," ");
					  String name=st.nextToken();
					  rs=stm.executeQuery("SELECT iditems,itemtype,actualAmount FROM items Where name='"+name+"'");
					  if (rs.next())
					  {   
						  LocalDateTime date=LocalDateTime.now();
						  int itemid=rs.getInt("iditems");
						  double amount=rs.getDouble("actualAmount")+dbladd;
						  stm.execute("Update items set actualAmount='"+amount+"' where iditems='"+itemid+"'");
						  stm.execute("Insert Into transaction (amount,date,itemID) Values ('"+dbladd+"','"+date+"','"+itemid+"')"); 
						  Design.AlertMsg(1,"Success","Added", "You have successfully made an addition to "+name+". You have R"+Double.toString(amount)+" in this budget.");
					  }
					  stm.close();
				 } catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }
         });
         
    
         //Deduct button
         btndeduct= new Button("Deduct");
         btndeduct.setPrefSize(230, 30);
         btndeduct.setLayoutX(160);
         btndeduct.setLayoutY(330);
         Design.Layout(btndeduct, 100, 330, roothome);
         btndeduct.setFont(Design.ButtonFont());
         btndeduct.setStyle(Design.ButtonStyle());
         
         
         //btndeduct processing
         btndeduct.setOnAction(e->
         {
        	 Pattern netpaypat= Pattern.compile("\\d*[.,]?d{0,2}");
			 Matcher netpaymatcher=netpaypat.matcher((CharSequence) txtamount.getText());
			 if(netpaymatcher.matches())
			 {
				 try {
					 double dblded=Double.parseDouble(txtamount.getText());
					  stm= Main.con.createStatement();
					  StringTokenizer st= new StringTokenizer(cmbitems.getSelectionModel().getSelectedItem(),"(");
					  String name=st.nextToken();
					  name=name.trim();
					  System.out.println(name);
					  rs=stm.executeQuery("SELECT iditems,name,actualAmount FROM items Where name='"+name+"'");
					  if (rs.next())
					  {
						  System.out.println("gets here");
						  LocalDateTime date=LocalDateTime.now();
						  int itemid=rs.getInt("iditems");
						  double actualamount=rs.getDouble("actualAmount");
						  if ((actualamount-dblded)<0)
						  {
							  Design.AlertMsg(3,"Failed","Unable to process your transaction","You have insufficient funds in your "+rs.getString("name")+"budget. You have R"+Double.toString(actualamount)+" remaining in this budget.");  
						  }else {
						  double amount=actualamount-dblded;
						  stm.execute("Update items set actualAmount='"+amount+"' where iditems='"+itemid+"'");
						  stm.execute("Insert Into transaction (amount,date,itemID) Values ('"+-dblded+"','"+date+"','"+itemid+"')"); 
						  Design.AlertMsg(1,"Success","Deducted", "You have successfully made an deduction to "+name+". You have R"+Double.toString(amount)+" remaining in this budget.");
						  }
					  }
					  stm.close();
				 } catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }else {
				 System.out.println("not working");
			 }
           });
         
         
         
         // Expenses table
	        Label lblexpense= new Label("Amounts left in each item");
	        lblexpense.setFont(Design.ButtonFont());
	        Design.Layout(lblexpense, Design.GetX(50), Design.GetY(10), roothome);
	        
	        TableView<ObservableList<Object>> table = new TableView<>();
	        TableColumn<ObservableList<Object>, String> namecol = new TableColumn<>("Name");
	        namecol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(0)));
	   
	        TableColumn<ObservableList<Object>, Double> amountcol = new TableColumn<>("Amount Available");
	        amountcol.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue().get(1)).asObject()); 

	        //setTableStyle(table,namecol, categorycol,amountcol);
	        table.setPrefHeight(Design.GetY(50)-Design.GetY(10));
	        // ObservableList for entries
	        ObservableList<ObservableList<Object>> data = FXCollections.observableArrayList();

	        // Layout setup
	        Design.Layout(table, Design.GetX(50), Design.GetY(13), roothome);

	        // Fetch data and populate table
	        try {
	            // Create Statement to execute SQL query
	            Statement stm = Main.con.createStatement();
	            ResultSet rs = stm.executeQuery("SELECT name, actualAmount FROM items WHERE itemtype='expense';");

	            while (rs.next()) {
	                // Get values from the result set
	                String name = rs.getString("name");
	                double amount = rs.getDouble("actualAmount");

	                // Adding to table data
	                data.add(FXCollections.observableArrayList(name, amount));
	            }

	            // Set items to TableView
	            table.setItems(data);

	            // Add columns to TableView
	            table.getColumns().add(namecol);
	            table.getColumns().add(amountcol);

	        } catch (SQLException e1) {
	            // Handle SQL exception
	            e1.printStackTrace();
	        }
         
         
         //btnback processing
         btnback.setOnAction(e->{
        	 Navigation.toHomepage();
         });
        
         
         //Adding components to root node
		 roothome.getStyleClass().add("color-palette");
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
