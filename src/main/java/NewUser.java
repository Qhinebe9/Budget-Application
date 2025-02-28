package Main.java;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

public class NewUser extends Main implements Navigation,Design {
		
		Scene scene;
		String path=null;
		Button btnaddexpense;
		Button btnaddincome;
		Button btnaddDetails;
		Button btnsavings;
		Button btndone;
		Popup failmsg= new Popup();
		
	
		public File txtplan= new File("docs/Plan.txt");
		public NewUser()
		{
			
			this.createPage();
			
		}
	    @SuppressWarnings("deprecation")
		void createPage()
		{
	    	failmsg.hide();
	    	
	    	//rootnode
			 Group roothome= new Group();
			 
			 
			 //button declaration
			 btnaddexpense= new Button("Add Expense");
			 btnaddincome= new Button("Add Income");
			 btnaddDetails= new Button("Add Details");
			 btnsavings= new Button("Add Plan");
			 btndone= new Button("Done");
			 
			 
			 //Welcome and quote
			 Label lblwelcome= new Label("Welcome Budgeteer!");
			 lblwelcome.setFont(Design.HeadingFont());
			 Design.Layout(lblwelcome, Design.GetX(40), Design.GetY(3), roothome);
			 Label lblquote= new Label("'The budget is not just a collection of numbers, but an expression of our values and aspirations.'");
	         lblquote.setFont(Design.ButtonFont());
	         Design.Layout(lblquote, Design.GetX(25), Design.GetY(10), roothome);
	         
	         
			 //
			 //getting budget info setup
			 //
				
				  Label lblname= new Label("Please enter your name:"); 
				  Design.Layout(lblname, Design.GetX(20), Design.GetY(15), roothome);
				  TextField txtname= new TextField("Name here");
				  txtname.setLayoutX(280); 
				  txtname.setLayoutY(150); //clearing
				  Design.Layout(txtname, Design.GetX(35), Design.GetY(15), roothome);
				  txtname.setOnMouseClicked(e-> { 
					  txtname.clear();
				  }); 
				  //profile picture setup 
				  Label lblprofilepic= new
				  Label("Set Profile picture"); 
				  lblprofilepic.setFont(Design.H2Font());
				  Design.Layout(lblprofilepic, Design.GetX(20), Design.GetY(20), roothome);
				  
				  Button btnselect= new Button("Set"); 
				  btnselect.setFont(Design.ButtonFont());
				  Design.Layout(btnselect, Design.GetX(35), Design.GetY(20), roothome);
				  
				  
				  //Btnselect processing
				  btnselect.setOnAction(e->
				  { File file; 
				  do 
				  { FileChooser fc= new FileChooser();
				  fc.setTitle("Choose Profile Picture"); 
				  file= fc.showOpenDialog(ps);
				  if (file!=null) {
				  path=file.getAbsolutePath();
				  } 
				  if (file==null) 
				  { 
					  break;
				  } 
				  System.out.println(path.toString());
				  } while(!(!path.toString().contains(".jpeg") ||!path.toString().contains(".jpg")|| !path.toString().contains(".png")));
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
				  
				  System.out.println(newfile.getAbsolutePath()); 
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
					  e1.printStackTrace(); } } });
				  
				  
				  
				  if (path!=null) {
					  path="file:"+path; 
					  Image imgprofile=imgprofile= new Image(path);
				  }
				  
				  //netpay setup
	              Label lblnetpay= new Label("Monthly budget \n reset day:");
	              lblnetpay.setFont(Design.H2Font()); 
				  Design.Layout(lblnetpay,Design.GetX(20), Design.GetY(25),roothome);
				  TextField txtresetday= new TextField(); 
				  txtresetday.setText("enter day");
				  txtresetday.setOnMouseClicked(e-> { 
					  txtresetday.clear();
				  }); 
				  Design.Layout(txtresetday, Design.GetX(35), Design.GetY(26), roothome);
				  //making sure number in reset day textbox is in range
				  txtresetday.textProperty().addListener((observable, oldValue, newValue) -> {
			            // Validate the new input
			            if (!newValue.isEmpty()) {
			                try {
			                    int value = Integer.parseInt(newValue); // Try parsing the number
			                    // If the value is within the desired range, leave it
			                    if (value < 0 || value > 31) {
			                    	txtresetday.setStyle("-fx-border-color: red;-fx-border-width:3px;"); // Red border for invalid input
			                    } else {
			                    	txtresetday.setStyle(""); // Clear the border if valid
			                    }
			                } catch (NumberFormatException e) {
			                    // If it's not a number, show a red border
			                	txtresetday.setStyle("-fx-border-color: red;-fx-border-width:3px;");
			                }
			            } else {
			            	txtresetday.setStyle(""); // Reset the border when the field is empty
			            }
			        });
				 
	         
	         
			 //fonts and styling of buttons
			 btnaddDetails.setFont(Design.ButtonFont());
			 btnaddDetails.setStyle(Design.ButtonStyle());
			 btnaddexpense.setFont(Design.ButtonFont());
			 btnaddexpense.setStyle(Design.ButtonStyle());
			 btnsavings.setFont(Design.ButtonFont());
			 btnsavings.setStyle(Design.ButtonStyle());
			 btnaddincome.setFont(Design.ButtonFont());
			 btnaddincome.setStyle(Design.ButtonStyle());
			 
			 
			 //positions of buttons
			 Design.Layout(btnaddDetails, Design.GetX(32), Design.GetY(32), roothome);
			 Design.Layout(btnaddexpense, Design.GetX(20), Design.GetY(75), roothome);
			 Design.Layout(btnaddincome, Design.GetX(38), Design.GetY(75), roothome);
			 Design.Layout(btnsavings, Design.GetX(56), Design.GetY(90), roothome);
			 
			 //Bugdet layout message
			 Label lbllayout= new Label("Budget Layout");
			 lbllayout.setFont(Design.HeadingFont());
			 Design.Layout(lbllayout, Design.GetX(38), Design.GetY(40), roothome);
			 
			 
			 //expenses setup
			 Label lblexpense= new Label("Add an expense");
			 Design.Layout(lblexpense, Design.GetX(20), Design.GetY(53), roothome);
			 Label lblexpmsg= new Label();
			 lblexpmsg.setStyle(" -fx-background-color: white;");
			 Design.Layout(lblexpmsg, Design.GetX(20), Design.GetY(55), roothome);
			 
			 
			 //combobox for expenses
			 ObservableList<String> expitems=FXCollections.observableArrayList("Tuition Fees","Textbooks and Stationery","Accomodation","Food","Transportation","Medical Expenses","Phone and Internet","Clothing","Personal Care","Leisure","Loans", "Misc");
			 ComboBox<String> expcombo= new ComboBox<>(expitems);
			 expcombo.setEditable(true);
			 TextField txtcombo= expcombo.getEditor();
			 expcombo.setPromptText("Expense Category");
			 TextField txtexpense= new TextField("Expense Name");
			 txtexpense.setOnMouseClicked(e->
			 {
				 txtexpense.clear();
				 lblexpmsg.setVisible(false);
				 
				 
			 });
			 txtcombo.setOnMouseClicked(e->
			 {
				 txtcombo.clear();
				 lblexpmsg.setVisible(false);
				 
			 });
			 
			 
			 //filteredList
			 txtcombo.textProperty().addListener((observable,oldValue,newValue)->
			 {
				 //filter items based on user input
				 if (newValue.isEmpty() || newValue.equals("Expense Name")) {
					 expcombo.setItems(expitems);
				 }else
				 {
					 ObservableList<String> filtered= FXCollections.observableArrayList();
					 for (String item: expitems) {
						 if (item.toLowerCase().contains(newValue.toLowerCase())) {
							 filtered.add(item);
						 }
					 }
					 expcombo.setItems(filtered);
				 }
			 });
			 
			 
			 //Adding labels to stage
			 Design.Layout(txtexpense, Design.GetX(20), Design.GetY(60), roothome);
			 Design.Layout(expcombo, Design.GetX(20), Design.GetY(65), roothome);
			 
			 //combobox clearing
			 expcombo.setOnMouseClicked(e->
			 {
				 expcombo.setPromptText("");
			 
			 });
			 
			 //expense amount text field
			 TextField txtexpenseamount= new TextField();
	         txtexpenseamount.setText("Budgeted amount");
	         txtexpenseamount.setOnMouseClicked(e->
			 {
				 txtexpenseamount.clear();
				 lblexpmsg.setVisible(false);
				 
			 });
	         Design.Layout(txtexpenseamount, Design.GetX(20), Design.GetY(70), roothome);
	         
	         
	         //Adding expense processing
	         btnaddexpense.setOnAction(e->
	         {
	        	 Pattern netpaypat= Pattern.compile("\\d*[.,]?\\d{0,2}");
				 Matcher netpaymatcher=netpaypat.matcher((CharSequence) txtexpenseamount.getText());
				 if(netpaymatcher.matches() && (txtexpense.getText()!=null || !txtexpense.getText().equals("Expense Name")) && expcombo.getSelectionModel().getSelectedIndex()!=-1)
				 {
					 double dblamount= Double.parseDouble(txtexpenseamount.getText());
					 try {
						 Statement stm= Main.con.createStatement();
							stm.execute("Insert Into items (name,setAmount, itemtype,category, actualAmount) Values ('"+txtexpense.getText()+"','"+dblamount+"','expense','"+expcombo.getSelectionModel().getSelectedItem()+"','"+dblamount+"')");
						    stm.close();
						    lblexpmsg.setVisible(true);
							 lblexpmsg.setText("Added Expense Successfully");
							 lblexpmsg.setStyle("color: red;");
					 } catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
				 }
				 else
				 {
					 lblexpmsg.setVisible(true);
					 lblexpmsg.setText("Invalid Expense Details"); 
				     
					 
				 }
	         });
	         
	         
	         //Incomes setup
			 Label lblincome= new Label("Add an Income");
			 Design.Layout(lblincome, Design.GetX(38), Design.GetY(53), roothome);
			 Label lblincmsg= new Label();
			 lblincmsg.setStyle(" -fx-background-color: white;");
			 Design.Layout(lblincmsg, Design.GetX(38), Design.GetY(74), roothome);
			 
			 
			 //combobox for expenses
			 ObservableList<String> incitems=FXCollections.observableArrayList("Stipends","Part-time Job","Freelancing","Online Work","Scholarships and Grants","Internship","Selling Goods and Services","Content Creation");
			 ComboBox<String> incCombo= new ComboBox<>(incitems);
			 incCombo.setEditable(true);
			 incCombo.setPromptText("Income Category");
			 TextField txtincCombo= incCombo.getEditor();
			 TextField txtinc= new TextField("Income Name");
			 txtinc.setOnMouseClicked(e->
			 {
				 txtinc.clear();
				 lblincmsg.setVisible(false); 
			 });
			 txtincCombo.setOnMouseClicked(e->
			 {
				 txtincCombo.clear();
				 lblincmsg.setVisible(false);
				 
			 });
			 
			 
			 //filteredList
			 txtincCombo.textProperty().addListener((observable,oldValue,newValue)->
			 {
				 //filter items based on user input
				 if (newValue.isEmpty() || newValue.equals("Income Name")) {
					 incCombo.setItems(incitems);
				 }else
				 {
					 ObservableList<String> filtered= FXCollections.observableArrayList();
					 for (String item: incitems) {
						 if (item.toLowerCase().contains(newValue.toLowerCase())) {
							 filtered.add(item);
						 }
					 }
					 incCombo.setItems(filtered);
				 }
			 });
			 Design.Layout(txtinc, Design.GetX(38), Design.GetY(60), roothome);
			 Design.Layout(incCombo, Design.GetX(38), Design.GetY(65), roothome);
			 
			 
			 TextField txtincAmount= new TextField();
	         txtincAmount.setText("Budgeted amount");
	         txtincAmount.setOnMouseClicked(e->
			 {
				 txtincAmount.clear();
				 lblincmsg.setVisible(false);
				 
			 });
	         Design.Layout(txtincAmount, Design.GetX(38), Design.GetY(70), roothome);
	         
	         
	         //Adding income processing
	         btnaddincome.setOnAction(e->
	         {
	        	 Pattern netpaypat= Pattern.compile("\\d*[.,]?\\d{0,2}");
				 Matcher netpaymatcher=netpaypat.matcher((CharSequence) txtincAmount.getText());
				 if(netpaymatcher.matches() && (txtinc.getText()!=null || !txtinc.getText().equals("Income Name")) && incCombo.getSelectionModel().getSelectedIndex()!=-1)
				 {
					 double dblamount= Double.parseDouble(txtincAmount.getText());
					 try {
						 Statement stm= Main.con.createStatement();
							stm.execute("Insert Into items (name,setAmount, itemtype,category,actualAmount) Values ('"+txtinc.getText()+"','"+dblamount+"','income','"+incCombo.getSelectionModel().getSelectedItem()+"','0')");
						    stm.close();
						    lblincmsg.setVisible(true);
							 lblincmsg.setText("Added Income Successfully");
							 lblincmsg.setStyle("color: red;");
					 } catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
				 }
				 else
				 {
					 lblincmsg.setVisible(true);
					 lblincmsg.setText("Invalid Income Details"); 
				     
					 
				 }
	         });
	         
	         
	         //savings plan setup
				
				  Label lblsavings= new Label("Savings Plan"); 
				  lblsavings.setFont(Design.H2Font());
				  Design.Layout(lblsavings, Design.GetX(56), Design.GetX(53), roothome);
				  TextField txtsavings= new TextField(); 
				  txtsavings.setText("Name of Savings Plan");
				  txtsavings.setOnMouseClicked(e-> { 
					  txtsavings.clear();
				  
				  });
                  Design.Layout(txtsavings, Design.GetX(56), Design.GetY(60), roothome);
				  
                //combobox for savings
     			 ObservableList<String> savings=FXCollections.observableArrayList("Emergency Fund","Short-Term Savings","Long-term Savings","Education Savings","Health Savings","Debt Repayment Savings","Leisure Savings","Fun Savings");
     			 ComboBox<String> savingsCombo= new ComboBox<>(savings);
     			 savingsCombo.setEditable(true);
     			 savingsCombo.setPromptText("Savings Category");
     			 TextField txtsavingscombo= savingsCombo.getEditor();
     			 txtsavingscombo.setOnMouseClicked(e->
     			 {
     				 txtsavingscombo.clear();
     				 lblincmsg.setVisible(false);
     				 
     			 });
     			 
     			 
     			 //filteredList
     			 txtsavingscombo.textProperty().addListener((observable,oldValue,newValue)->
     			 {
     				 //filter items based on user input
     				 if (newValue.isEmpty() || newValue.equals("Savings Name")) {
     					 savingsCombo.setItems(savings);
     				 }else
     				 {
     					 ObservableList<String> filtered= FXCollections.observableArrayList();
     					 for (String item: savings) {
     						 if (item.toLowerCase().contains(newValue.toLowerCase())) {
     							 filtered.add(item);
     						 }
     					 }
     					 savingsCombo.setItems(filtered);
     				 }
     			 });
     			 Design.Layout(savingsCombo, Design.GetX(56), Design.GetY(65), roothome);
                  TextField txtsavingstarget= new TextField();
				  txtsavingstarget.setText("Savings Target");
				  txtsavingstarget.setOnMouseClicked(e-> { 
					  txtsavingstarget.clear();
				  
				  }); 
				  Design.Layout(txtsavingstarget, Design.GetX(56), Design.GetY(70), roothome);
				  //savings button 

				  btnsavings.setOnAction(e->
				  { Pattern netpaypat= Pattern.compile("\\d*[.,]?\\d{0,2}"); 
				  Matcher netpaymatcher=netpaypat.matcher((CharSequence) txtsavingstarget.getText());
				  if(netpaymatcher.matches() && txtsavings.getText()!=null) 
				  { double dblamount= Double.parseDouble(txtsavingstarget.getText()); 
				  try { Statement stm=Main.con.createStatement(); 
				  String strquery="Insert Into savings (Name,target) Values ('"+txtsavings.getText()+"','"+dblamount+"')"; stm.execute(strquery); stm.close(); } 
				  catch
				  (SQLException e1) { // TODO Auto-generated catch block e1.printStackTrace();
				  }
				  
				  } 
				  else 
				  { Label lblfail= new Label("Invalid savings details");
				  lblfail.setStyle(" -fx-background-color: white;");
				  failmsg.getContent().add(lblfail); failmsg.show(Main.ps);
				  }
				  
				  });
				 
	         
	         
	         //btndone processing
	         btndone.setFont(Design.ButtonFont());
			 Design.Layout(btndone, 470, 650, roothome);
			 btndone.setStyle("-fx-background-radius: 15px;");
	         btndone.setOnAction(e->{
	        	 Navigation.toHomepage();
	         });
			
			
			 scene= new Scene(roothome);
			//colour of the scene
			 scene.setFill(new LinearGradient(0, 0, 1, 1, true,CycleMethod.NO_CYCLE,new Stop(0, Color.web("#ff7f50")),new Stop(1, Color.web("#6a5acd"))));
			 
			 //
			 //BtnaddDetails Code
			 //
				
				  btnaddDetails.setOnAction(e-> { 
					  try(PrintWriter txtout= new PrintWriter(new File("docs/Plan.txt"))) 
					  { txtout.println(txtname.getText());
				        txtout.println(path); 
				        txtout.println(txtresetday.getText()); }
					  catch(FileNotFoundException ex)
					  { ex.printStackTrace(); }
					  btnaddDetails.setDisable(true);
					  //adding date of when budget should reset
				        int currentYear = LocalDateTime.now().getYear();
				        int currentMonth = LocalDateTime.now().getMonthValue();

				        int day = Integer.parseInt(txtresetday.getText());
				        
				        // Create a LocalDateTime for the specified day of the current month and year, at 00:00:00
				        LocalDateTime dateTime = LocalDateTime.of(currentYear, currentMonth, day, 0, 0);

				        // Format the LocalDateTime to the desired format: yyyy-MM-dd HH:mm:ss
				        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				        
				        String formattedDateTime = dateTime.format(formatter);
				        System.out.println(formattedDateTime);
				        Statement stm;
				        try {
				            stm = Main.con.createStatement();

				            // Drop the event if it exists
				            stm.execute("DROP EVENT IF EXISTS reset_actual_amounts");

				            // Create the event with proper formatting
				            String createEventQuery = 
				                "CREATE EVENT reset_actual_amounts " +
				                "ON SCHEDULE EVERY 1 MONTH " +
				                "STARTS '" + formattedDateTime + "' " +  // Ensure formattedDateTime is quoted
				                "DO " +
				                "BEGIN " +
				                "    UPDATE items " +
				                "    SET actualAmount = setAmount " +
				                "    WHERE itemtype = 'expense'; " +
				                "    UPDATE items " +
				                "    SET actualAmount = 0.00 " +
				                "    WHERE itemtype = 'income'; " +
				                "END";

				            stm.execute(createEventQuery);
				            stm.close();
				        } catch (SQLException e1) {
				            e1.printStackTrace();
				        }	
				  
				  });
				 
			 
	         
	    }
		public Scene getscene()
		{
			return scene;
		}
		
		

}
