import java.io.File;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.DefaultPieDataset;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
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
         
         
         //Month selection combobox(Indices start at 0)
         ObservableList<String> strMonths= FXCollections.observableArrayList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
         ComboBox<String> cmbMonths= new ComboBox<String>(strMonths);
         cmbMonths.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15px; -fx-font: 18px \"Comic Sans Ms\";");
         cmbMonths.setLayoutX(310);
         cmbMonths.setLayoutY(20);
         cmbMonths.setPrefSize(100, 30);
         Date date= new Date();
         int intdate= date.getMonth();
         cmbMonths.setPromptText(strMonths.get(intdate));
         
         
         //displaying money spent label
         Label lblmoneyspent= new Label("Monthly Spending:");
         lblmoneyspent.setFont(Design.H2Font());
         lblmoneyspent.setLayoutX(300);
         lblmoneyspent.setLayoutY(70);
         
         
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
			  double barlength=350*spendingpercent/100;
			  
			  
			//Stroking spending rectangles
		         Canvas spendingcanvas= new Canvas(600,700); 
		         GraphicsContext gc= spendingcanvas.getGraphicsContext2D();
		         gc.setStroke(Color.FLORALWHITE);
		         gc.setFill(Color.FLORALWHITE);
		         gc.fillRoundRect(220, 110,350, 40,40, 40);
		         
		       //percentage spending
				 if (spendingpercent<50)
					 gc.setFill(Color.LIGHTGREEN);
				 else if(spendingpercent>=50 && spendingpercent<80)
					 gc.setFill(Color.ORANGE);
				 else if (spendingpercent>=80 &&spendingpercent<=100)
					 gc.setFill(Color.RED);
		         gc.fillRoundRect(220,110,barlength,40,40,40);
		         roothome.getChildren().add(spendingcanvas);

		         lblpercentagespending.setFont(Design.ButtonFont());
		         Design.Layout(lblpercentagespending, 350, 150, roothome);
		       //Available balance handling
		         Label lblavail= new Label();
		         String strspent=Double.toString(dblremaining);
		         lblmoneyspent.setText("Monthly Spending:"+"R"+strspent);
		         lblmoneyspent.setFont(Design.H2Font());
		         lblmoneyspent.setLayoutX(300);
		         lblmoneyspent.setLayoutY(70);
		         Design.Layout(lblmoneyspent, 300, 70, roothome);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
         //Transact button
         btntransact= new Button("Transact");
         btntransact.setPrefSize(280, 30);
         btntransact.setFont(Design.ButtonFont());
         btntransact.setStyle(Design.ButtonStyle());
         Design.Layout(btntransact, 150, 180, roothome);
         
         
         //Previous Transact button
         //
         btnprevtransact= new Button("View Transactions");
         btnprevtransact.setPrefSize(150, 30);
         btnprevtransact.setFont(Design.ButtonFont());
         btnprevtransact.setStyle(Design.ButtonStyle());
         btnprevtransact.setLayoutX(210);
         btnprevtransact.setLayoutY(240);
         Design.Layout(btnprevtransact, 210, 240, roothome);
         
         //btnprevtransact processing
         btnprevtransact.setOnAction(e->{
        	 Navigation.toHistoryPage();
         });
         
         
         //Spending graph
         try { 
		      Statement stm= Main.con.createStatement(); 
			  ResultSet rs=stm.executeQuery("Select category, sum(actualAmount) as amount from items where type='expense' group by category"); 
			  ObservableList<PieChart.Data> piedata= FXCollections.observableArrayList();
			  while (rs.next()) 
			  { 
				  String category=rs.getString("category");
				  double amount=rs.getDouble("amount");
				  piedata.add(new PieChart.Data(category, amount));
			  }
			  PieChart piechart= new PieChart(piedata);
			  piechart.setTitle("Spending");
			  roothome.getChildren().add(piechart);
			  piechart.setLayoutX(50);
			  piechart.setLayoutY(300);
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
		 roothome.getChildren().add(cmbMonths);
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
