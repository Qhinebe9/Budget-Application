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
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
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
				      ResultSet rs=stm.executeQuery("SELECT \r\n"
				      		+ "    SUM(subquery.amount) AS totalAmount, \r\n"
				      		+ "    SUM(subquery.totalSetAmount) AS totalSetAmount, \r\n"
				      		+ "    subquery.category\r\n"
				      		+ "FROM \r\n"
				      		+ "    (SELECT \r\n"
				      		+ "        transaction.itemid, \r\n"
				      		+ "        SUM(transaction.amount) AS amount,  -- Sum amount for each itemid\r\n"
				      		+ "        MAX(items.setAmount) AS totalSetAmount,  -- Ensure setAmount is added once per itemid\r\n"
				      		+ "        items.category\r\n"
				      		+ "     FROM transaction\r\n"
				      		+ "     JOIN items ON transaction.itemid = items.iditems\r\n"
				      		+ "     WHERE items.type = 'expense'\r\n"
				      		+ "     GROUP BY \r\n"
				      		+ "        transaction.itemid, \r\n"
				      		+ "        items.category) AS subquery\r\n"
				      		+ "GROUP BY \r\n"
				      		+ "    subquery.category;\r\n"
				      		+ ""); 
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
						  double amount=rs.getDouble("totalAmount");
						  amount=Math.abs(amount);
						  String category=rs.getString("category");
						  double setamount=rs.getDouble("totalSetAmount");
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
	   
         
       
         
        
         
         
         //Spending graph
         try { 
		      Statement stm= Main.con.createStatement(); 
			  rs=stm.executeQuery("SELECT \r\n"
				  		+ "    SUM(transaction.amount) AS amount\r\n"
				  		+ "FROM \r\n"
				  		+ "    transaction\r\n"
				  		+ "JOIN \r\n"
				  		+ "    items ON transaction.itemid = items.iditems\r\n"
				  		+ "GROUP BY \r\n"
				  		+ "    MONTH(transaction.date) ;\r\n"
				  		+ "");
			  NumberAxis xAxis= new NumberAxis();
			  NumberAxis yAxis= new NumberAxis();
			  double[] xPoints= {1,2,3,4,5,6,7,8,9,10,11,12};
			  double[] ypoints= new double[12];
			  Path path= new Path();
			  path.setStroke(Color.BLUE);
			  path.setStrokeWidth(4);
			  int index=0;
			  while (rs.next()) 
			  { 
				  double amount=rs.getDouble("amount");
				  amount=Math.abs(amount);
				  ypoints[index]=amount;
				  index++;
			  }
			  MoveTo moveTo = new MoveTo();
		        moveTo.setX(xAxis.getDisplayPosition(xPoints[0]));
		        moveTo.setY(yAxis.getDisplayPosition(ypoints[0]));
		        path.getElements().add(moveTo);
			  for (int k=1; k<xPoints.length-1;k++) {
				  double ctrlX=(xPoints[k]+xPoints[k+1])/2;
				  double ctrlY = (ypoints[k] + ypoints[k + 1]) / 2; 
				  QuadCurveTo quadcurve = new QuadCurveTo();
				  quadcurve.setControlX(xAxis.getDisplayPosition(ctrlX));
				  quadcurve.setControlY(yAxis.getDisplayPosition(ctrlY));
				  quadcurve.setX(xAxis.getDisplayPosition(xPoints[k + 1]));
		          quadcurve.setY(yAxis.getDisplayPosition(ypoints[k + 1]));
		          path.getElements().add(quadcurve);
		            
		            
				  
				  
			  }
			  Design.Layout(path, Design.GetX(50), Design.GetY(10), roothome);
         //Adding components to root node
		 roothome.getStyleClass().add("color-palette");
		 scene= new Scene(roothome);
		 
		 
		 //colour of the scene
		 scene.setFill(new LinearGradient(0, 0, 1, 1, true,CycleMethod.NO_CYCLE,new Stop(0, Color.web("#DCE8E0")),new Stop(1, Color.web("#D2D8D6"))));

	}catch(SQLException ex) {
		ex.printStackTrace();
	}
	}

	public Scene getscene()
	{
		return scene;
	}
}
