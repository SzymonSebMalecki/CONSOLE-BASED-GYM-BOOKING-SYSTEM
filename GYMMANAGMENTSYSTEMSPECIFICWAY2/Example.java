import java.util.Scanner;
import java.sql.*;

public class Example{
    public static void main(String args[]){
	String dburl = "jdbc:mysql://localhost/example";
	String user = "root";
	String pwd = "";

	Scanner input = new Scanner(System.in);

	try(Connection conn = DriverManager.getConnection(dburl, user, pwd)){

	    Statement stat = conn.createStatement();

	    System.out.print("Enter name: ");
	    String name = input.nextLine();
	    
	    System.out.print("Enter password: ");
	    String pass = input.nextLine();

	    String query = "SELECT * FROM user " +
		"WHERE name = '" + name + "' " +
		"AND password = '" + pass + "'";

	    ResultSet rs = stat.executeQuery(query);
	    
	    boolean none = true;
	    
	    while(rs.next()){
		System.out.println(rs.getString("id") + '\t' +
				   rs.getString("name") + '\t' +
				   rs.getString("password"));
		
		none = false;
	    }

	    if(none)
		System.out.println("Sorry the name and/or password is incorrect");
	    
	}catch(SQLException ex){
	    System.out.println("SQL error: " + ex.getMessage());
	}
    }
}
