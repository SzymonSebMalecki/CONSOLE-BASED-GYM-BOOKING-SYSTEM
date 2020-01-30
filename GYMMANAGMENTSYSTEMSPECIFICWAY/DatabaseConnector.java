import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector{
    public static Connection DatabaseConnector;
    public static String dbName = "gymbookingsystem";

    public static Connection DBConnect(){
        try {
        // Need to add this to bashrc (gedit ~/.bashrc) in order to make database driver work: "export CLASSPATH=$CLASSPATH:/usr/share/java/mysql-connector-java.jar"
        String dbUrl = "jdbc:mysql://localhost:3306/gymbookingsystem";
        String DatabaseUsername = "root";
        String DatabaseUserPassword = "Szymek737918";
        DatabaseConnector = DriverManager.getConnection(dbUrl, DatabaseUsername, DatabaseUserPassword);   

        if(DatabaseConnector != null) {
            System.out.println(" ");
            System.out.println("Succesfully Connected to the Database " + dbName);
        } else{
            System.out.println("Connection to Database Failed");
        }

    }catch(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
      }
        return DatabaseConnector;
     }

public static Connection DBDisconnect() throws SQLException{
    try{
        if(DatabaseConnector != null && !DatabaseConnector.isClosed()){
            System.out.println("Will now disconnect from Database" + dbName);
            DatabaseConnector.close();
        }
    }catch(SQLException ex){
     ex.printStackTrace();
     throw ex;
    } 
    return DatabaseConnector;
  }
}
    