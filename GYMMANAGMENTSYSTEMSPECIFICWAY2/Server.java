import java.io.*;
import java.net.*;
import java.sql.*;

public class Server{
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static BufferedReader bufferedReader;
	private static String inputLine;
	private static final int port = 2000; //SPECIFIED PORT NUMBER
	
	public static void main(String[] args){
		try{
			System.out.println("Starting the socket server at port:" + port);
			serverSocket = new ServerSocket(port);
	
	while (true) {
			System.out.println("Waiting for clients...");
			clientSocket = serverSocket.accept();
			System.out.println("You're now connected to the server.");
	
			//START of DB connection
			try{
				System.out.println("Trying to connect to MySQL...");
	
				Class.forName("com.mysql.jdbc.Driver"); //Load the driver
				Connection databaseConnection = DatabaseConnector.DBConnect();
			//	DriverManager.getConnection("jdbc:mysql://localhost:3306/gymbookingsystem", "root", "Szymek737918"); 
	
				if(databaseConnection == null){
					System.out.println("MySQL Server is NOT running!");
					return;
				} else {
					System.out.println("Successfully connected to MySQL!");
				}
	
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				Statement stmt = null;
				ResultSet resultSet = null;
	
				try{
				  stmt = (Statement) databaseConnection.createStatement();
				  while((inputLine = bufferedReader.readLine()) != null) {
					  resultSet = stmt.executeQuery(inputLine); 
				  }

				  while (resultSet.next()) {
					 System.out.print("TEST");
				  }
				} catch(SQLException ex1){
				  ex1.printStackTrace();
				}
				finally{
				  try{
					if(stmt != null) stmt.close();
					if(resultSet != null) resultSet.close();
					if(databaseConnection != null) databaseConnection.close();
				  } catch(SQLException ex1){
					e2.printStackTrace();
				  }
				}
			} catch (Exception err){System.out.println(err);}
			//END of DB connection
		}
	    } catch (IOException ex){
			System.out.println("Error creating server socket: " + ex.getMessage());
			System.exit(2);
		}
	}
	}
