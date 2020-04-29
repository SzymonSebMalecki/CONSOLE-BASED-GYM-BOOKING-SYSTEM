import java.io.*;
import java.net.*;
import java.sql.*;


/* ------------------------- SERVER --------------------------- */

public class Server{
	private static ServerSocket serverSocket;
	private static Socket ClientSocket;
	private static BufferedReader bufferedReader;
	private static String inputLine;
	private static final int port = 2000; //SPECIFIED PORT NUMBER ON WHICH SERVER RUNS
	
	public static void main(String[] args){
	try{
			System.out.println("Starting the socket server at port:" + port);
			serverSocket = new ServerSocket(port);
	
	  while (true) {
			System.out.println("Waiting for clients...");
			ClientSocket = serverSocket.accept();
			System.out.println("You're now connected to the server.");

			try{
				System.out.println("Trying to connect to MySQL...");
				Class.forName("com.mysql.jdbc.Driver"); //Load mysql driver
				Connection databaseConnection = DatabaseConnector.DBConnect();

				if(databaseConnection == null){
					System.out.println("MySQL Server is NOT running!");
					return;
				} else {
					System.out.println("Successfully connected to MySQL!");
				}
	
				bufferedReader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
				Statement ConnectionStatement = null;
				ResultSet RESULTSet = null;
	
				try{
				  ConnectionStatement = (Statement) databaseConnection.createStatement();
				  while((inputLine = bufferedReader.readLine()) != null) {
					RESULTSet = ConnectionStatement.executeQuery(inputLine); 
				  }
				} catch(SQLException cnE){
					System.out.println("SQLException: " + cnE.getMessage());
					System.out.println("SQLState: " + cnE.getSQLState());
				}
				finally{
				  try{
					if( ConnectionStatement != null)  ConnectionStatement.close();
					if(RESULTSet != null) RESULTSet.close();
					if(databaseConnection != null) databaseConnection.close();
				  } catch(SQLException ceX){
					System.out.println("SQLException: " + ceX.getMessage());
					System.out.println("SQLState: " + ceX.getSQLState());
				  }
				}
			} catch (Exception leX){
				System.out.println("Exception: " + leX.getMessage());
			}
		   }
	    } catch (IOException exS){
			System.out.println("Error creating server socket: " + exS.getMessage());
		}
	}
}


/* ------------------------- SERVER --------------------------- */
