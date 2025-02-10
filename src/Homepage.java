import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
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
public class Homepage extends Canvas implements Navigation, Design{
	
	Scene scene;
	String name,path,strnetpay;
	double dblnetpay;
	public Button btntransact;
	public Button btnprevtransact;
	public Button btnmore;
	Label lblname;
	ImageView imgprofileview;
	ResultSet rs;
	Statement stm;
	Image imgprofile;
	public File txtplan= new File("docs/Plan.txt");
	public Homepage()
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
    	//Getting info from textfile
    	//
		/*
		 * try(Scanner txtin = new Scanner(txtplan)) { while(txtin.hasNext()) { name=
		 * txtin.nextLine(); path=txtin.nextLine(); strnetpay=txtin.nextLine();
		 * 
		 * } }catch(FileNotFoundException ex) { ex.printStackTrace(); }
		 */
    	 //rootnode
		 Group roothome= new Group();
		 
		 
         //displaying name label
		 name="Q";
         lblname= new Label("Welcome"+'\n'+name);
         lblname.setFont(Design.H2Font());
         Design.Layout(lblname, 20, 160, roothome);
         
         
         //Handling image
         Image image = new Image("file:data/App Profile pic.jpeg");
         ImageView imageView = new ImageView(image);
         // Set size and position
         imageView.setFitWidth(140);
         imageView.setFitHeight(140);
         Design.Layout(imageView, 30, 20, roothome);
         // Clip the image to a circular shape
         Circle clip = new Circle(70, 70, 70); // Center (50, 50) with radius 50
         imageView.setClip(clip);
         
         
         //Label for recent transactions
         Label lblrecent=new Label();
         lblrecent.setText("Latest Transactions");
         lblrecent.setFont(Design.H2Font());
         Design.Layout(lblrecent, Design.GetX(2), Design.GetY(30), roothome);
         
         
       //Last 3 transactions section
	        try { 
			      Statement stm= Main.con.createStatement(); 
				   rs=stm.executeQuery("SELECT  date AS transaction_date, amount AS transaction_amount, name AS item_name,\r\n"
				  		                      + "category AS item_category, type AS item_type FROM transaction JOIN items i\r\n"
				  		                      + "ON transaction.itemid = i.iditems ORDER BY transaction_date DESC LIMIT 3;"); 
				  VBox transactionList = new VBox(10);
			      transactionList.setPadding(new Insets(10));
			      transactionList.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10px; -fx-background-radius: 10px;");
			      while (rs.next()) 
				  {  
					  Date date=rs.getDate("transaction_date");
					  String strdate=date.toString();
					  double amount=rs.getDouble("transaction_amount");
					  String name=rs.getString("item_name");
					  String type=rs.getString("item_type");
					  HBox box=History.createTransactionCard(strdate, type, name, amount);
					  transactionList.getChildren().add(box);
				  }
				  ScrollPane scrollpane=new ScrollPane(transactionList);
				  scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
				  Design.Layout(scrollpane, Design.GetX(2), Design.GetY(35), roothome);
				  scrollpane.setMaxHeight(500);
				  scrollpane.setMinWidth(250);
				  scrollpane.setFitToWidth(true);
			      scrollpane.setStyle(" border-radius:5px;");
	        }catch(SQLException ex) {
	        	ex.printStackTrace();
	        }
         
  
	        
	        
	      //Section for Top category
	         Label lblcategory=new Label();
	         lblcategory.setText("Top Spending Categories");
	         lblcategory.setFont(Design.H2Font());
	         Design.Layout(lblcategory, Design.GetX(20), Design.GetY(30), roothome);
		        try { 
				      Statement stm= Main.con.createStatement(); 
				      ResultSet rs=stm.executeQuery("SELECT \r\n"
				      		+ "    SUM(transaction.amount) AS amount, \r\n"
				      		+ "    items.category \r\n"
				      		+ "FROM \r\n"
				      		+ "    transaction \r\n"
				      		+ "JOIN \r\n"
				      		+ "    items ON transaction.itemid = items.iditems \r\n"
				      		+ "WHERE \r\n"
				      		+ "    items.type = 'expense' \r\n"
				      		+ "    AND YEAR(transaction.date) = YEAR(CURDATE())    -- Current year\r\n"
				      		+ "    AND MONTH(transaction.date) = MONTH(CURDATE())  -- Current month\r\n"
				      		+ "GROUP BY \r\n"
				      		+ "    items.category \r\n"
				      		+ "ORDER BY \r\n"
				      		+ "    amount \r\n"
				      		+ "LIMIT 3;\r\n"); 
					  VBox transactionList = new VBox(10);
				      transactionList.setPadding(new Insets(10));
				      transactionList.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10px; -fx-background-radius: 10px;");
					  while (rs.next()) 
					  {  
						  LocalDate date=LocalDate.now();
						  String strdate=date.toString();
						  double amount=rs.getDouble("amount");
						  String category=rs.getString("category");
						  HBox box=History.createTransactionCard(strdate, "", category, amount);
						  transactionList.getChildren().add(box);
					  }
					  ScrollPane scrollpane=new ScrollPane(transactionList);
					  scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
					  Design.Layout(scrollpane, Design.GetX(20), Design.GetY(35), roothome);
					  scrollpane.setMaxHeight(500);
					  scrollpane.setMinWidth(250);
					  scrollpane.setFitToWidth(true);
				      scrollpane.setStyle(" border-radius:5px;");
		        }catch(SQLException ex) {
		        	ex.printStackTrace();
		        }
	         
         
         
         //Month selection combobox(Indices start at 0)
         ObservableList<String> strMonths= FXCollections.observableArrayList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
         ComboBox<String> cmbMonths= new ComboBox<String>(strMonths);
         cmbMonths.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15px; -fx-font: 18px \"Comic Sans Ms\";");
         Design.Layout(cmbMonths, Design.GetX(65), Design.GetY(1), roothome);
         cmbMonths.setPrefSize(100, 30);
         Date date= new Date();
         int intdate= date.getMonth();
         cmbMonths.setPromptText(strMonths.get(intdate));
         cmbMonths.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 // Event triggered when a new selection is made
                 System.out.println("Selected Item: " + newValue);
                 int month=cmbMonths.getSelectionModel().getSelectedIndex();
                 month++;
                 System.out.println("month before: "+month);
                 if (month==0) {
                	 month=date.getMonth();
                 }
                 Populate(roothome,month);
             }
         });
         cmbMonths.valueProperty().addListener((observable, oldValue, newValue) -> {
         });
         
         
        
         
         
         //Transact button
         btntransact= new Button("Transact");
         btntransact.setPrefSize(280, 30);
         btntransact.setFont(Design.ButtonFont());
         btntransact.setStyle(Design.ButtonStyle());
         Design.Layout(btntransact, Design.GetX(61), Design.GetY(23), roothome);
         
         
         //Previous Transact button
         //
         btnprevtransact= new Button("View Transactions");
         btnprevtransact.setPrefSize(150, 30);
         btnprevtransact.setFont(Design.ButtonFont());
         btnprevtransact.setStyle(Design.ButtonStyle());
         btnprevtransact.setLayoutX(210);
         btnprevtransact.setLayoutY(240);
         Design.Layout(btnprevtransact, Design.GetX(65), Design.GetY(30), roothome);
         
         //btnprevtransact processing
         btnprevtransact.setOnAction(e->{
        	 Navigation.toHistoryPage();
         });
         
         
         
			 
         
         //more info button
         btnmore= new Button("More Info");
         btnmore.setPrefSize(150, 30);
         btnmore.setFont(Design.ButtonFont());
         btnmore.setStyle(Design.ButtonStyle());
         Design.Layout(btnmore, 210, 700, roothome);
         btnmore.setOnAction(e->{
        	 Navigation.toGraphs();
        	 
         });
         
         
         //Adding infobutton to button
         Image imginfo= new Image("file:data/images/MoreInfoIcon.jpeg");
         ImageView view= new ImageView(imginfo);
         view.setFitHeight(20);
         view.setFitWidth(20);
         btnmore.setGraphic(view);
         
         
         //Adding components to root node
		 roothome.getStyleClass().add("color-palette");
		 scene= new Scene(roothome);
		 
		 
		 //colour of the scene
		 scene.setFill(new LinearGradient(0, 0, 1, 1, true,CycleMethod.NO_CYCLE,new Stop(0, Color.web("#DCE8E0")),new Stop(1, Color.web("#D2D8D6"))));
		 
		 //linear-gradient(to bottom right, #ff7f50, #6a5acd)

		 
		 //Buttons code
		 btntransact.setOnAction(e->
			{
				Navigation.toTransactionPage();
				
			});
			/*
			 * imgprofileview.setOnMouseClicked(e-> { Main.ps.setScene(getscene());
			 * 
			 * });
			 */
	}

	public Scene getscene()
	{
		return scene;
	}
	private void Populate(Group roothome,int month) {
		//Spending graph
        try { 
        	
        	//Calculating spending from db
            Label lblpercentagespending=new Label();
            try {
   			stm= Main.con.createStatement();
   			  rs=stm.executeQuery("SELECT sum(setAmount) FROM items Where type='expense'");
   			  double dblbudget=0;
   			  double dblspent=0;
   			  if (rs.next())
   			  {
   				  dblbudget+=rs.getDouble(1);
   			  }
   			 rs=stm.executeQuery("SELECT amount AS transaction_amount\r\n"
                      + "FROM transaction JOIN items i\r\n"
                      + "ON transaction.itemid = i.iditems");
   			if (rs.next())
 			  {
 				  dblspent+=rs.getDouble(1);
 			  }
   			  double dblremaining=dblbudget-dblspent;
   			  stm.close();
   			  int spendingpercent=(int) (dblremaining/dblbudget*100);
   			  lblpercentagespending.setText( spendingpercent+"% Spent");
   			  double barlength=Design.GetX(40)*spendingpercent/100;
   			  
   			  
   			//Stroking spending rectangles
   		         Canvas spendingcanvas= new Canvas(1000,200); 
   		      roothome.getChildren().remove(spendingcanvas);
   		         GraphicsContext gc= spendingcanvas.getGraphicsContext2D();
   		         Design.Layout(spendingcanvas, Design.GetX(25), Design.GetY(5), roothome);
   		         gc.setStroke(Color.FLORALWHITE);
   		         gc.setFill(Color.FLORALWHITE);
   		         gc.fillRoundRect(Design.GetX(25), Design.GetY(7),Design.GetX(40), 40,40, 40);
   		       //percentage spending
   				 if (spendingpercent<50)
   					 gc.setFill(Color.LIGHTGREEN);
   				 else if(spendingpercent>=50 && spendingpercent<80)
   					 gc.setFill(Color.ORANGE);
   				 else if (spendingpercent>=80 &&spendingpercent<=100)
   					 gc.setFill(Color.RED);
   		         gc.fillRoundRect(Design.GetX(25),Design.GetY(7),barlength,40,40,40);
   		         
   		         

   		         lblpercentagespending.setFont(Design.ButtonFont());
   		         Design.Layout(lblpercentagespending, Design.GetX(65), Design.GetY(18), roothome);
   		       //Available balance handling
   		         Label lblavail= new Label();
   		         String strspent=Double.toString(dblremaining);
   		      //displaying money spent label
   		         Label lblmoneyspent= new Label("Monthly Spending:");
   		      roothome.getChildren().remove(lblmoneyspent);
   		         lblmoneyspent.setFont(Design.H2Font());
   		         lblmoneyspent.setText("Monthly Spending:"+"R"+strspent);
   		         lblmoneyspent.setFont(Design.H2Font());
   		         Design.Layout(lblmoneyspent, Design.GetX(60), Design.GetY(6), roothome);
   		} catch (SQLException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
            
            
		      Statement stm= Main.con.createStatement(); 
			  //ResultSet rs=stm.executeQuery("Select category, sum(actualAmount) as amount from items where type='expense' group by category"); 
			  ResultSet rs=stm.executeQuery("SELECT \r\n"
			  		+ "    items.category, \r\n"
			  		+ "    SUM(transaction.amount) AS amount\r\n"
			  		+ "FROM \r\n"
			  		+ "    transaction\r\n"
			  		+ "JOIN \r\n"
			  		+ "    items ON transaction.itemid = items.iditems\r\n"
			  		+ "WHERE \r\n"
			  		+ "    items.type = 'expense'\r\n"
			  		+ "    AND YEAR(transaction.date) = YEAR(CURDATE())\r\n"
			  		+ "    AND MONTH(transaction.date) = "+month+"\r\n"
			  		+ "GROUP BY \r\n"
			  		+ "    items.category;\r\n"
			  		+ "");
			  
			  ObservableList<PieChart.Data> piedata= FXCollections.observableArrayList();
			  while (rs.next()) 
			  { 
				  String category=rs.getString("category");
				  double amount=rs.getDouble("amount");
				  amount=Math.abs(amount);
				  piedata.add(new PieChart.Data(category, amount));
			  }
			  PieChart piechart= new PieChart(piedata);
			  piechart.setMinWidth(Design.GetX(36));
			  piechart.setTitle("Spending");
			  Design.Layout(piechart, Design.GetX(50), Design.GetY(38), roothome);
			  }
			  catch (SQLException e) 
			  { // TODO Auto-generated catch block
			  e.printStackTrace();
			  System.out.println("flop with try");
			  }
		
	}
}
