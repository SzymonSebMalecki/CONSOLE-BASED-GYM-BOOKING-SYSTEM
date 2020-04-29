import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* ------------------------- DATABASE CONNECTOR --------------------------- */
public class DatabaseConnector{
    public static Connection DatabaseConnector;
    public static String dbName = "gymbookingsystem";

    public static Connection DBConnect(){//Method for Connecting to Database gymbookingsystem
        try {
        String dbUrl = "jdbc:mysql://localhost:3306/gymbookingsystem";
        String DatabaseUsername = "root";
        String DatabaseUserPassword = "";
        DatabaseConnector = DriverManager.getConnection(dbUrl, DatabaseUsername, DatabaseUserPassword);   

        if(DatabaseConnector == null) {
            System.out.println("Connection to Database Failed");
        }

    }catch(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
      }
        return DatabaseConnector;
     }

public static Connection DBDisconnect() throws SQLException{ //Method for Disconnecting from Database gymbookingsystem
    try{
        if(DatabaseConnector != null && !DatabaseConnector.isClosed()){
            System.out.println("Disconnecting from database: " + dbName);
            DatabaseConnector.close();
        }
    }catch(SQLException ex){
     ex.printStackTrace();
     throw ex;
    } 
    return DatabaseConnector;
  }
}
    
/* ------------------------- DATABASE CONNECTOR --------------------------- */