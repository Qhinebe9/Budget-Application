import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import javafx.application.Application;
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
			con=  DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetdb","root","Qhinebe13.");
			
		} catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void start(Stage ps) throws Exception {
		// TODO Auto-generated method stub
		this.ps=ps;
		
	 	
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

