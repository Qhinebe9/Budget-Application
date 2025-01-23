import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Statement;
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

public class NewUser extends Main implements Navigation {
		
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
			 Design.Layout(lblwelcome, 100, 30, roothome);
			 Label lblquote= new Label("'The budget is not just a collection of numbers, but an expression of our values and aspirations.'");
	         lblquote.setFont(Design.ButtonFont());
	         Design.Layout(lblquote, 20, 100, roothome);
	         
	         
			 //
			 //getting budget info setup
			 //
				/*
				 * Label lblname= new Label("Please enter your name:"); lblname.setLayoutX(20);
				 * lblname.setLayoutY(150); TextField txtname= new TextField("Name here");
				 * txtname.setLayoutX(280); txtname.setLayoutY(150); //clearing
				 * txtname.setOnMouseClicked(e-> { txtname.clear();
				 * 
				 * }); //profile picture setup Label lblprofilepic= new
				 * Label("Set Profile picture"); lblprofilepic.setFont(font);
				 * lblprofilepic.setLayoutX(20); lblprofilepic.setLayoutY(180); Button
				 * btnselect= new Button("Set"); btnselect.setFont(font);
				 * btnselect.setLayoutX(280); btnselect.setLayoutY(180);
				 * btnselect.setStyle("-fx-background-radius: 15px;"); btnselect.setOnAction(e->
				 * { File file; do { FileChooser fc= new FileChooser();
				 * fc.setTitle("Choose Profile Picture"); fc.setInitialDirectory(new
				 * File("C:")); file= fc.showOpenDialog(ps); if (file!=null) {
				 * path=file.getAbsolutePath();
				 * 
				 * 
				 * } if (file==null) { break; } System.out.println(path.toString());
				 * 
				 * } while(!(!path.toString().contains(".jpeg")
				 * ||!path.toString().contains(".jpg")|| !path.toString().contains(".png")));
				 * if(file!=null) { File newfile= new File("data/"+file.getName()); try {
				 * newfile.createNewFile(); } catch (IOException e1) { // TODO Auto-generated
				 * catch block e1.printStackTrace(); }
				 * System.out.println(newfile.getAbsolutePath()); FileOutputStream fos=null; try
				 * { fos = new FileOutputStream(newfile); } catch (FileNotFoundException e1) {
				 * // TODO Auto-generated catch block e1.printStackTrace(); } try {
				 * Files.copy(file.toPath(),fos); } catch (IOException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); } try {
				 * path=newfile.getCanonicalPath(); } catch (IOException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); } } }); if (path!=null) {
				 * path="file:"+path; Image imgprofile=imgprofile= new Image(path); } //netpay
				 * setup Label lblnetpay= new Label("What is your net pay?");
				 * lblnetpay.setFont(font); lblnetpay.setLayoutX(20); lblnetpay.setLayoutY(220);
				 * TextField txtnetpay= new TextField(); txtnetpay.setText("Amount");
				 * txtnetpay.setOnMouseClicked(e-> { txtnetpay.clear();
				 * 
				 * }); txtnetpay.setLayoutX(280); txtnetpay.setLayoutY(220);
				 */
	         
	         
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
			 Design.Layout(btnaddexpense, 20, 420, roothome);
			 Design.Layout(btnaddincome, 280, 420, roothome);
			 Design.Layout(btnaddDetails, 180, 250, roothome);
			 btnaddDetails.setStyle("-fx-background-radius: 15px;");
			 
			 
			 //expenses setup
			 Label lblexpense= new Label("Add an expense");
			 Design.Layout(lblexpense, 20, 300, roothome);
			 Label lblexpmsg= new Label();
			 lblexpmsg.setStyle(" -fx-background-color: white;");
			 lblexpmsg.setLayoutX(20);
			 lblexpmsg.setLayoutY(460);
			 Design.Layout(lblexpmsg, 20, 460, roothome);
			 
			 
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
			 Design.Layout(txtexpense, 20, 330, roothome);
			 Design.Layout(expcombo, 20, 360, roothome);
			 
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
	         Design.Layout(txtexpenseamount, 20, 390, roothome);
	         
	         
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
							stm.execute("Insert Into items (name,setAmount, type,category, actualAmount) Values ('"+txtexpense.getText()+"','"+dblamount+"','expense','"+expcombo.getSelectionModel().getSelectedItem()+"','"+dblamount+"')");
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
			 Design.Layout(lblincome, 280, 300, roothome);
			 Label lblincmsg= new Label();
			 lblincmsg.setStyle(" -fx-background-color: white;");
			 Design.Layout(lblincmsg, 280, 460, roothome);
			 
			 
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
			 Design.Layout(txtinc, 280, 330, roothome);
			 Design.Layout(incCombo, 280, 360, roothome);
			 
			 
			 TextField txtincAmount= new TextField();
	         txtincAmount.setText("Budgeted amount");
	         txtincAmount.setOnMouseClicked(e->
			 {
				 txtincAmount.clear();
				 lblincmsg.setVisible(false);
				 
			 });
	         Design.Layout(txtincAmount, 280, 390, roothome);
	         
	         
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
							stm.execute("Insert Into items (name,setAmount, type,category,actualAmount) Values ('"+txtinc.getText()+"','"+dblamount+"','income','"+incCombo.getSelectionModel().getSelectedItem()+"','0')");
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
				/*
				 * Label lblsavings= new Label("Savings Plan"); lblsavings.setFont(font);
				 * lblsavings.setLayoutX(20); lblsavings.setLayoutY(490); TextField txtsavings=
				 * new TextField(); txtsavings.setText("Name of Savings Plan");
				 * txtsavings.setOnMouseClicked(e-> { txtsavings.clear();
				 * 
				 * }); txtsavings.setLayoutX(20); txtsavings.setLayoutY(520); TextField
				 * txtsavingstarget= new TextField();
				 * txtsavingstarget.setText("Savings Target");
				 * txtsavingstarget.setOnMouseClicked(e-> { txtsavingstarget.clear();
				 * 
				 * }); txtsavingstarget.setLayoutX(20); txtsavingstarget.setLayoutY(550);
				 * //savings button btnsavings.setLayoutX(20); btnsavings.setLayoutY(590);
				 * btnsavings.setStyle("-fx-background-radius: 15px;"); font=
				 * Font.font("Comic Sans Ms",FontWeight.BOLD,25);  
				 * btnsavings.setOnAction(e->
				 * { Pattern netpaypat= Pattern.compile("\\d*[.,]?\\d{0,2}"); Matcher
				 * netpaymatcher=netpaypat.matcher((CharSequence) txtsavingstarget.getText());
				 * if(netpaymatcher.matches() && txtsavings.getText()!=null) { double dblamount=
				 * Double.parseDouble(txtsavingstarget.getText()); try { Statement stm=
				 * Main.con.createStatement(); String
				 * strquery="Insert Into savings (Name,target) Values ('"+txtsavings.getText()+
				 * "','"+dblamount+"')"; stm.execute(strquery); stm.close(); } catch
				 * (SQLException e1) { // TODO Auto-generated catch block e1.printStackTrace();
				 * }
				 * 
				 * } else { Label lblfail= new Label("Invalid savings details");
				 * lblfail.setStyle(" -fx-background-color: white;");
				 * failmsg.getContent().add(lblfail); failmsg.show(Main.ps);
				 * 
				 * 
				 * }
				 * 
				 * });
				 */
	         
	         
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
				/*
				 * btnaddDetails.setOnAction(e-> { Pattern netpaypat=
				 * Pattern.compile("\\d*[.,]?\\d{0,2}"); Matcher
				 * netpaymatcher=netpaypat.matcher((CharSequence) txtnetpay.getText());
				 * if(netpaymatcher.matches()) { double intnetpay=
				 * Double.parseDouble(txtnetpay.getText()); try(PrintWriter txtout= new
				 * PrintWriter(new File("docs/Plan.txt"))) { txtout.println(txtname.getText());
				 * txtout.println(path); txtout.println(intnetpay); }catch(FileNotFoundException
				 * ex) { ex.printStackTrace(); } //Adding Allowance amount to db try {
				 * 
				 * Statement stm= Main.con.createStatement(); String tdate=
				 * Calendar.YEAR+"-"+Calendar.MONTH+"-"+Calendar.DATE; stm.
				 * execute("Insert Into transaction (itemName,amount,date,amountleft) Values ('Income','"
				 * +intnetpay+"','"+java.time.LocalDate.now()+"','"+intnetpay+"')");
				 * stm.close(); } catch (SQLException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } btnaddDetails.setDisable(true); }
				 * 
				 * });
				 */
			 
	         
	    }
		public Scene getscene()
		{
			return scene;
		}
		
		

}
