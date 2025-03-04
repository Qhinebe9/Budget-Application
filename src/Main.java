

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	static Stage ps;
	public static Connection con;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		createConnection();
		launch(args);

	}
	public static void createConnection()
	{
		String dbUrl=System.getenv("JAWSDB_URL");
		try {
			//"jdbc:mysql://localhost:3306/budgetdb","root","Qhinebe13."
			
			if (dbUrl != null) {
                String[] parts = dbUrl.split("[:@/]");
                String username = parts[1].substring(2); // Remove "//"
                String password = parts[2];
                String host = parts[3];
                String port = parts[4];
                String dbName = parts[5];

                String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

                con = DriverManager.getConnection(url, username, password);
                System.out.println("Connected to JawsDB!");
            }
			else {
				System.out.println("URL not found");
			}
		//con=  DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetdb","root","Qhinebe13.");
			
		} catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void start(Stage ps) throws Exception {
		// TODO Auto-generated method stub
		this.ps=ps;
		double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // Set the stage size to match the screen size
        ps.setWidth(screenWidth);
        ps.setHeight(screenHeight);
		
	 	
		File file= new File("docs/Plan.txt");
		//FileInputStream fin= new FileInputStream(file);
		try(Scanner txtin = new Scanner(file))
		 {
			 if (txtin.hasNext())
			 {
				 Homepage home=new Homepage();
					ps.setScene(home.getscene());
					
				  
			 }
			 else
			 {
				 NewUser newuser= new NewUser();
				    ps.setScene(newuser.getscene());
			 }
				 
			 
		 }catch(FileNotFoundException ex)
		 {
			 ex.printStackTrace();
		 }
		 
		
		ps.show();		
	}
		
		
		
	

}

