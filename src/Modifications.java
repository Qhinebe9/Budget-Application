import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.PreparedStatement;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
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
	        Design.Layout(title, Design.GetX(45), Design.GetY(3), roothome);
	        
	        
	        //personal details heading
	        Label lbldetails = new Label("Personal details");
	        lbldetails.setFont(Design.HeadingFont());
	        Design.Layout(lbldetails, Design.GetX(20), Design.GetY(10), roothome);
	        
	        
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
	        Design.Layout(lbldetailsexplainer, Design.GetX(15), Design.GetY(15), roothome);
	        
	      //Handling image
	         Image image = new Image("file:"+path);
	         ImageView imageView = new ImageView(image);
	         // Set size and position
	         imageView.setFitWidth(140);
	         imageView.setFitHeight(140);
	         Design.Layout(imageView, Design.GetX(1), Design.GetY(16), roothome);
	         // Clip the image to a circular shape
	         Circle clip = new Circle(70, 70, 70); // Center (50, 50) with radius 50
	         imageView.setClip(clip);
	         //Changing profile picture button
		     Button btnchange= new Button("Change");
		     btnchange.setFont(Design.ButtonFont());
		     btnchange.setStyle(Design.ButtonStyle());
		     Design.Layout(btnchange, Design.GetX(3), Design.GetY(35), roothome);
		     
		     //change button processing
		     btnchange.setOnAction(e->{
		    	 File file; 
				  do { 
					  FileChooser fc= new FileChooser();
					  fc.setTitle("Choose Profile Picture"); 
					  file= fc.showOpenDialog(Main.ps);
					  if (file!=null) {
						  path=file.getAbsolutePath();} 
					  if (file==null) 
					  { 
						  break;
					  } 
				  }while(!(!path.toString().contains(".jpeg") ||!path.toString().contains(".jpg")|| !path.toString().contains(".png")));
				  if(file!=null) 
				  { 
					  String extension=null;
					  int dotIndex = file.getName().lastIndexOf('.');
				      if (dotIndex > 0 && dotIndex < file.getName().length() - 1) {
				            extension = file.getName().substring(dotIndex + 1);
				        }
					  File newfile= new File("data/pp."+extension); 
					  try {
					  newfile.createNewFile(); 
					  } 
					  catch (IOException e1) 
					  { // TODO Auto-generated
					  e1.printStackTrace(); 
					  }
					  FileOutputStream fos=null; 
					  try
					  { 
						  fos = new FileOutputStream(newfile); 
					  }
					  catch (FileNotFoundException e1) 
					  {
					  // TODO Auto-generated catch block 
						  e1.printStackTrace(); 
					  } 
				  
					  try {
					  Files.copy(file.toPath(),fos); 
					  } 
					  catch (IOException e1) { // TODO Auto-generated catch block 
						  e1.printStackTrace(); } 
					  try {
					  path=newfile.getCanonicalPath();} 
					  catch (IOException e1) { // TODO Auto-generated catch block 
						  e1.printStackTrace(); } }
				  Image newimg = new Image("file:"+path);
				  imageView.setImage(newimg);
				  
		     });
	        
			Label lblname = new Label("Name:");
			lblname.setFont(Design.ButtonFont());
			Design.Layout(lblname, Design.GetX(20), Design.GetY(20), roothome);
			TextField txtname = new TextField();
			txtname.setText(name);
			txtname.setPrefWidth(200);
			Design.Layout(txtname, Design.GetX(20), Design.GetY(23), roothome);
	        
			Label lblreset = new Label("Reset Day:");
			lblreset.setFont(Design.ButtonFont());
			Design.Layout(lblreset, Design.GetX(20), Design.GetY(27), roothome);
	        TextField txtresetday= new TextField(resetday);
	        txtresetday.setPrefWidth(200);
	        Design.Layout(txtresetday, Design.GetX(20), Design.GetY(30), roothome);
	        
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
	        Design.Layout(btndetails, Design.GetX(22), Design.GetY(35), roothome);
	        
	     // Expenses table
	        Label lblexpense= new Label("Click on the cell(s) you would like to change, and click save when done");
	        lblexpense.setFont(Design.ButtonFont());
	        Design.Layout(lblexpense, Design.GetX(11), Design.GetY(42), roothome);
	        
	        TableView<ObservableList<Object>> table = new TableView<>();
	        table.setEditable(true);
	        TableColumn<ObservableList<Object>, String> namecol = new TableColumn<>("Name");
	        namecol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(0))); // Index 0 for name
	        namecol.setCellFactory(TextFieldTableCell.forTableColumn());
	        namecol.setOnEditCommit(e->{
	        	ObservableList<Object> row=e.getRowValue();
	        	row.set(0,e.getNewValue());
	        });
	        TableColumn<ObservableList<Object>, String> categorycol = new TableColumn<>("Category");
	        categorycol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(1))); // Index 1 for category
	        categorycol.setCellFactory(TextFieldTableCell.forTableColumn());
	        categorycol.setOnEditCommit(e->{
	        	ObservableList<Object> row=e.getRowValue();
	        	row.set(1,e.getNewValue());
	        });
	        TableColumn<ObservableList<Object>, Double> amountcol = new TableColumn<>("Budgeted Amount");
	        amountcol.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue().get(2)).asObject()); // Index 2 for amount
	        StringConverter<Double> doubleConverter = new StringConverter<>() {
	            @Override
	            public String toString(Double object) {
	                return object != null ? object.toString() : ""; // Convert Double to String
	            }

	            @Override
	            public Double fromString(String string) {
	                try {
	                    return Double.parseDouble(string);  // Convert String to Double
	                } catch (NumberFormatException e) {
	                    return 0.0;  // If invalid input, return a default value (could be customized)
	                }
	            }
	        };
	        // Apply custom StringConverter to the Amount column
	        amountcol.setCellFactory(TextFieldTableCell.forTableColumn(doubleConverter));
	        amountcol.setOnEditCommit(event -> {
	            ObservableList<Object> row = event.getRowValue();
	            try {
	                // Update the "amount" in the row after converting the value
	                row.set(2, Double.parseDouble(event.getNewValue().toString()));  // Update the "amount" in the row
	            } catch (NumberFormatException e) {
	                // Handle the case where the value is not a valid double
	                System.out.println("Invalid input for amount. Please enter a valid number.");
	            }
	        });

	        setTableStyle(table,namecol, categorycol,amountcol);
	        table.setPrefHeight(Design.GetY(50)-Design.GetY(10));
	        // ObservableList for entries
	        ObservableList<ObservableList<Object>> data = FXCollections.observableArrayList();

	        // Layout setup
	        Design.Layout(table, Design.GetX(10), Design.GetY(45), roothome);

	        // Fetch data and populate table
	        try {
	            // Create Statement to execute SQL query
	            Statement stm = Main.con.createStatement();
	            ResultSet rs = stm.executeQuery("SELECT name, setAmount, category AS item_category FROM items WHERE itemtype='expense';");

	            while (rs.next()) {
	                // Get values from the result set
	                String name = rs.getString("name");
	                String category = rs.getString("item_category");
	                double amount = rs.getDouble("setAmount");

	                // Adding to table data
	                data.add(FXCollections.observableArrayList(name, category, amount));
	            }

	            // Set items to TableView
	            table.setItems(data);

	            // Add columns to TableView
	            table.getColumns().add(namecol);
	            table.getColumns().add(categorycol);
	            table.getColumns().add(amountcol);

	        } catch (SQLException e1) {
	            // Handle SQL exception
	            e1.printStackTrace();
	        }
	     // Create a Save Button
	        Button expsave = new Button("Save");
	        expsave.setFont(Design.ButtonFont());
	        expsave.setStyle(Design.ButtonStyle());
	        expsave.setOnAction(event -> {
	            updateDatabase(table);
	        });

	        // Add button to the layout
	        Design.Layout(expsave, Design.GetX(25), Design.GetY(90),roothome);  // Adjust X, Y positions as needed



	     // Incomes table
	        Label lblsave= new Label("Click on the cell(s) you would like to change, and click save when done");
	        lblsave.setFont(Design.ButtonFont());
	        Design.Layout(lblsave, Design.GetX(50), Design.GetY(9), roothome);
	        
	        TableView<ObservableList<Object>> inctable = new TableView<>();
	        inctable.setEditable(true);
	        namecol = new TableColumn<>("Name");
	        namecol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(0))); // Index 0 for name
	        namecol.setCellFactory(TextFieldTableCell.forTableColumn());
	        namecol.setOnEditCommit(e->{
	        	ObservableList<Object> row=e.getRowValue();
	        	row.set(0,e.getNewValue());
	        });
	        categorycol = new TableColumn<>("Category");
	        categorycol.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get(1))); // Index 1 for category
	        categorycol.setCellFactory(TextFieldTableCell.forTableColumn());
	        categorycol.setOnEditCommit(e->{
	        	ObservableList<Object> row=e.getRowValue();
	        	row.set(1,e.getNewValue());
	        });
	        amountcol = new TableColumn<>("Amount");
	        amountcol.setCellValueFactory(cellData -> new SimpleDoubleProperty((Double) cellData.getValue().get(2)).asObject()); // Index 2 for amount
	        doubleConverter = new StringConverter<>() {
	            @Override
	            public String toString(Double object) {
	                return object != null ? object.toString() : ""; // Convert Double to String
	            }

	            @Override
	            public Double fromString(String string) {
	                try {
	                    return Double.parseDouble(string);  // Convert String to Double
	                } catch (NumberFormatException e) {
	                    return 0.0;  // If invalid input, return a default value (could be customized)
	                }
	            }
	        };
	     // Apply custom StringConverter to the Amount column
	        amountcol.setCellFactory(TextFieldTableCell.forTableColumn(doubleConverter));
	        amountcol.setOnEditCommit(event -> {
	            ObservableList<Object> row = event.getRowValue();
	            try {
	                // Update the "amount" in the row after converting the value
	                row.set(2, Double.parseDouble(event.getNewValue().toString()));  // Update the "amount" in the row
	            } catch (NumberFormatException e) {
	                // Handle the case where the value is not a valid double
	                System.out.println("Invalid input for amount. Please enter a valid number.");
	            }
	        });
	        setTableStyle(inctable,namecol, categorycol,amountcol);
	        inctable.setPrefHeight(Design.GetY(50)-Design.GetY(15));
	        // ObservableList for entries
	        ObservableList<ObservableList<Object>> incdata = FXCollections.observableArrayList();

	        // Layout setup
	        Design.Layout(inctable, Design.GetX(50), Design.GetY(13), roothome);

	        // Fetch data and populate table
	        try { 
	            Statement stm = Main.con.createStatement(); 
	            ResultSet rs = stm.executeQuery("SELECT name, setAmount, category AS item_category FROM items WHERE itemtype='income';");

	            while (rs.next()) {  
	                // Get values from the result set
	                String name = rs.getString("name");
	                String category = rs.getString("item_category");
	                double amount = rs.getDouble("setAmount");

	                // Adding to table
	                incdata.add(FXCollections.observableArrayList(name, category, amount));
	            }

	            // Set items to TableView
	            inctable.setItems(incdata);

	            // Add columns to TableView
	            inctable.getColumns().add(namecol);
	            inctable.getColumns().add(categorycol);
	            inctable.getColumns().add(amountcol);

	        } catch (SQLException e1) {
	            // Handle SQL exception
	            e1.printStackTrace();
	        }
	     // Create a Save Button
	        Button incsave = new Button("Save");
	        incsave.setFont(Design.ButtonFont());
	        incsave.setStyle(Design.ButtonStyle());
	        incsave.setOnAction(event -> {
	            updateDatabase(inctable);
	        });

	        // Add button to the layout
	        Design.Layout(incsave, Design.GetX(65), Design.GetY(50),roothome);  // Adjust X, Y positions as needed


	        
	       

	        
	     
	        
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
    private void setTableStyle(TableView<ObservableList<Object>> table,TableColumn<ObservableList<Object>, String> namecol, TableColumn<ObservableList<Object>, String> categorycol,TableColumn<ObservableList<Object>, Double> amountcol) {
        // Set column width (adjust based on your content)
        namecol.setMinWidth(200);
        categorycol.setMinWidth(150);
        amountcol.setMinWidth(150);

        // Set column header font style and size
        namecol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        categorycol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        amountcol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Set background color for the table and alternating row colors
        table.setStyle("-fx-background-color: #f4f4f4;");
        table.setRowFactory(tv -> {
            TableRow<ObservableList<Object>> row = new TableRow<>();
            row.setStyle("-fx-border-color: #d3d3d3; -fx-border-width: 0 0 1px 0;");
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #e0e0e0;"); // Highlight row on click
                }
            });
            return row;
        });

        // Add alternating row striping for readability
        table.setRowFactory(tv -> {
            TableRow<ObservableList<Object>> row = new TableRow<>();
            row.setStyle("-fx-background-color: #ffffff;");
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #f0f0f0;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #ffffff;"));
            return row;
        });

        // Add padding around the cells for better spacing
        table.setStyle("-fx-padding: 10;");
    }
 // Method to update the database based on edited rows
    private void updateDatabase(TableView<ObservableList<Object>> table) {
        try {
            // Loop through the rows in the table
            for (ObservableList<Object> row : table.getItems()) {
                // Extract values from the row
                String name = (String) row.get(0);  // Name column
                String category = (String) row.get(1);  // Category column
                Double amount = (Double) row.get(2);  // Amount column

                // Create SQL update statement
                String query = "UPDATE items SET setAmount = ?, category = ? WHERE name = ?";
                PreparedStatement pstmt = Main.con.prepareStatement(query);

                // Set values to prepared statement
                pstmt.setDouble(1, amount);
                pstmt.setString(2, category);
                pstmt.setString(3, name);

                // Execute the update
                pstmt.executeUpdate();
            }
            // Optionally, show a success message after saving
            System.out.println("Data saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}