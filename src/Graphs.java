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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
public class Graphs extends Canvas implements Navigation, Design{
	
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
	public Graphs()
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
    	 //rootnode
		 Group roothome= new Group();
         
         
         //Label for recent transactions
         Label lblrecent=new Label();
         lblrecent.setText("Analytics");
         lblrecent.setFont(Design.HeadingFont());
         Design.Layout(lblrecent, Design.GetX(45), Design.GetY(6), roothome);
         
   
	      //Section for Budget VS Actual amount
		        try { 
				      Statement stm= Main.con.createStatement(); 
				      ResultSet rs=stm.executeQuery("SELECT SUM(transaction.amount) AS actualamount, SUM(items.setAmount) as setamount, items.category \r\n"
				      		+ "FROM transaction \r\n"
				      		+ "JOIN items ON transaction.itemid = items.iditems where items.type='expense' \r\n"
				      		+ "GROUP BY items.category \r\n"); 
					  //Bar chart
				      CategoryAxis x_axis=new CategoryAxis();
				      x_axis.setLabel("Categories");
				      NumberAxis y_axis= new NumberAxis();
				      BarChart<String, Number> stackedbarchart= new BarChart<>(x_axis,y_axis);
				      stackedbarchart.setTitle("Budget vs. Actual Spending");
				      List<XYChart.Series<String, Number>> seriesList= new ArrayList<>();
				      XYChart.Series<String, Number> series1= new XYChart.Series<>();
					  series1.setName("Budgeted");
					  XYChart.Series<String, Number> series2= new XYChart.Series<>();
					  series2.setName("Actual");
				      while (rs.next()) 
					  {
						  double amount=rs.getDouble("actualamount");
						  amount=Math.abs(amount);
						  String category=rs.getString("category");
						  double setamount=rs.getDouble("setamount");
						  System.out.println("Category: "+category+" Budgeted amount: "+setamount+" Actual amount: "+amount);
						  series1.getData().add(new XYChart.Data<>(category,setamount));
						  series2.getData().add(new XYChart.Data<>(category,amount));
						   
					  }
				      seriesList.add(series1);
					  seriesList.add(series2); 
				      stackedbarchart.getData().addAll(seriesList);
				      Design.Layout(stackedbarchart, Design.GetX(10), Design.GetY(6), roothome);
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
         
         
         //displaying money spent label
         Label lblmoneyspent= new Label("Monthly Spending:");
         lblmoneyspent.setFont(Design.H2Font());
         
         
         //Calculating spending from db
         Label lblpercentagespending=new Label();
         try {
			stm= Main.con.createStatement();
			  rs=stm.executeQuery("SELECT sum(setAmount),sum(actualAmount) FROM items Where type='expense'");
			  double dblbudget=0;
			  double dblspent=0;
			  if (rs.next())
			  {
				  dblbudget+=rs.getDouble(1);
				  dblspent+=rs.getDouble(2);
				 }
			  double dblremaining=dblbudget-dblspent;
			  stm.close();
			  int spendingpercent=(int) (dblremaining/dblbudget*100);
			  lblpercentagespending.setText( spendingpercent+"% Spent");
			  double barlength=Design.GetX(40)*spendingpercent/100;
			  
			  
			//Stroking spending rectangles
		         Canvas spendingcanvas= new Canvas(1000,200); 
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
		         lblmoneyspent.setText("Monthly Spending:"+"R"+strspent);
		         lblmoneyspent.setFont(Design.H2Font());
		         Design.Layout(lblmoneyspent, Design.GetX(60), Design.GetY(6), roothome);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
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
         
         
         //Spending graph
         try { 
		      Statement stm= Main.con.createStatement(); 
			  //ResultSet rs=stm.executeQuery("Select category, sum(actualAmount) as amount from items where type='expense' group by category"); 
			  ResultSet rs=stm.executeQuery("Select category,SUM(amount) AS amount  from transaction join items ON transaction.itemid = items.iditems where items.type='expense' group by items.category");
			  
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
			 
         
         //more info button
         btnmore= new Button("More Info");
         btnmore.setPrefSize(150, 30);
         btnmore.setFont(Design.ButtonFont());
         btnmore.setStyle(Design.ButtonStyle());
         Design.Layout(btnmore, 210, 700, roothome);
         
         
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
}
