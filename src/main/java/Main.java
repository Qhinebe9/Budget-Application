package Main.java;

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
		try {
			//
			String dburl=System.getenv("JAWSDB_URL");
			if (dburl!=null) {
				con=  DriverManager.getConnection(dburl);	
			}
			else {
				System.out.println("JAWSDB environment var not found");
			}
			
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

