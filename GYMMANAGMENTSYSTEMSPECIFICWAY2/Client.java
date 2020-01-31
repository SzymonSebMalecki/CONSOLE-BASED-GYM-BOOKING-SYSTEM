import java.sql.*;
import java.util.*;

public class Client{
  
    public static void ADDClientIntoDatabase(){
        try {
    
            Connection DBCONC = DatabaseConnector.DBConnect();
            boolean UserDecs = true;
            Scanner scanner = new Scanner(System.in);
         
            while(UserDecs){
            System.out.println("Please Enter Client Name:");
            String UserName = scanner.next();
         
            System.out.println("Please Enter Client Last Name:");
            String UserLastName = scanner.next();
        
            System.out.println("Please Enter Client's Gender: MALE / FEMALE / PREFER NOT TO SAY");
            String UserGender = scanner.next();
         
            System.out.println("Please Enter Client's Age/Date of Birth?");
            int UserAge = scanner.nextInt();

            String DoubleBookingQuery = "SELECT COUNT(*) AS COUNT FROM Clients WHERE FNAME = ? AND LNAME = ? AND AGE = ?";
            PreparedStatement CheckWhetherAlreadyThere = DBCONC.prepareStatement(DoubleBookingQuery);
            CheckWhetherAlreadyThere.setString(1, UserName);
            CheckWhetherAlreadyThere.setString(2, UserLastName);
            CheckWhetherAlreadyThere.setInt(3, UserAge);
            ResultSet RSDB = CheckWhetherAlreadyThere.executeQuery();

            if((RSDB.next()) && (RSDB.getInt("COUNT") > 1)){
                   System.out.println("This client has been already registered");
                   System.out.println("Please Try Again");
                   break;
            }else {
             String sqlquery = "INSERT INTO Clients(FNAME, LNAME, GENDER, AGE) VALUES (?,?,?,?)";
             PreparedStatement ClientAdd = DBCONC.prepareStatement(sqlquery);  
             ClientAdd.setString(1, UserName);
             ClientAdd.setString(2, UserLastName);
             ClientAdd.setString(3, UserGender);
             ClientAdd.setInt(4, UserAge);
             ClientAdd.executeUpdate();  
             DisplayContentsOfTableClient();

             int Client_IDA = getSpecificCLientId(UserName, UserLastName, UserAge);

             String QueryResetAutoIncrement = "ALTER TABLE Clients AUTO_INCREMENT = ?";
             PreparedStatement ResetAutoIncrement = DBCONC.prepareStatement(QueryResetAutoIncrement);
             ResetAutoIncrement.setInt(1, Client_IDA);
             ResetAutoIncrement.executeUpdate();

             System.out.println("Are you happy with the result? (Y/N)");
             String UserDec = scanner.next();
             if (UserDec.equals("Y")){
                 UserDecs = false;
                 break;
             }else if (UserDec.equals("N")){
                 UserDecs = true;
                 System.out.println("Now You Will Be Able To Do It Again");
             }else {
                 break;
             }
             }
            }
        }catch(SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
          }
      }
  
      public static int getLastCLientId(){
         int lastid = 0;
        try{         
                Connection DBCONC = DatabaseConnector.DBConnect();
                PreparedStatement ps = DBCONC.prepareStatement("SELECT * FROM  Clients where CLIENT_ID");
                ResultSet RS = ps.executeQuery();
                RS = ps.executeQuery("SELECT MAX(CLIENT_ID) AS CLIENT_ID FROM Clients");
                if(RS.next()){
                 lastid = RS.getInt("CLIENT_ID");
                 System.out.println("Last ID from function :"+ lastid); 
                }
                }catch(SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
           return lastid; 
    }

    public static void ClearEntireTable(){
        try{
            int Clientid = getLastCLientId();
            Connection DBCONC = DatabaseConnector.DBConnect();
            String Query = "DELETE TOP(?) FROM Clients";

            PreparedStatement Clear = DBCONC.prepareStatement(Query);
            Clear.setInt(1, Clientid);
            Clear.executeUpdate();
          //  Statement Clear = DBCONC.createStatement();
           // Clear.execute(Query);
            }catch(SQLException ex){
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
            }
    }


    public static int getFirstCLientId(){
        int FirstID = 0;
        try{
                Connection DBCONC = DatabaseConnector.DBConnect();
                PreparedStatement ps = DBCONC.prepareStatement("SELECT * FROM  Clients where CLIENT_ID");
                ResultSet RS = ps.executeQuery();
                RS = ps.executeQuery("SELECT MIN(CLIENT_ID) AS CLIENT_ID FROM Clients");
                if(RS.next()){
                 FirstID = RS.getInt("CLIENT_ID");
                 System.out.println("LastID from function :"+ FirstID);
                }
                }catch(SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                }
                return FirstID;
            }

    public static int getSpecificCLientId(String UserFirstName, String UserLastName, int AGE){
       int Specific_ID = 0;
        try{
                Connection DBCONC = DatabaseConnector.DBConnect();
                String SQLQUERY = "SELECT CLIENT_ID FROM Clients WHERE (FNAME = ? AND LNAME = ? AND AGE = ?)";
                PreparedStatement ps = DBCONC.prepareStatement(SQLQUERY);
                ps.setString(1, UserFirstName);
                ps.setString(2, UserLastName);
                ps.setInt(3, AGE);
                ResultSet RS = ps.executeQuery();
             ;
                if(RS.next()){
                 Specific_ID = RS.getInt("CLIENT_ID");
                 System.out.println("SPECIFIED CLIENT_ID:"+ Specific_ID);
                }
                }catch(SQLException ex){
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                }
                return Specific_ID;
         }

      public static void UpdateClientDetails() throws SQLException{
       try {
            Connection DBCONC = DatabaseConnector.DBConnect();
            String UpdateFirstName, UpdateLastName;
            int Client_ID = 0;
            int UpdateAge;
            boolean COND = true;
            
            Scanner scanner = new Scanner(System.in);
            while(COND == true){
          
            System.out.println("Do you want to see contents of table before you update it? (Y/N): ");
            String UserDecision = scanner.next();

            if(UserDecision.equals("Y")){
                DisplayContentsOfTableClient();
            }
                System.out.println("Now you will be able to update details for specific CLient:");

                System.out.println("Please Enter Client Name:");
                UpdateFirstName = scanner.next();
                
                System.out.println("Please Enter Client Last Name:");
                UpdateLastName= scanner.next();

                System.out.println("Please Enter Client's Age/Date of Birth?");
                UpdateAge = scanner.nextInt();
            
                Client_ID = getSpecificCLientId(UpdateFirstName, UpdateLastName, UpdateAge);
                System.out.println("Client_ID" + Client_ID);

                String SQLQuery = "Update Clients SET FNAME=?, LNAME=?, AGE=? where Client_ID =?";
                PreparedStatement ClientUpdate = DBCONC.prepareStatement(SQLQuery);
                ClientUpdate.setString(1, UpdateFirstName);
                ClientUpdate.setString(2, UpdateLastName);
                ClientUpdate.setInt(3, UpdateAge);
                ClientUpdate.setInt(4,Client_ID);
                ClientUpdate.executeUpdate();
                DisplayContentsOfTableClient();
                System.out.println("Are you happy with the result? (Y/N)");
                String UserDec = scanner.next();
                if (UserDec.equals("Y")){
                    COND = false;
                    break;
                }else if (UserDec.equals("N")){
                    COND = true;
                    System.out.println("Now You Will Be Able To Do It Again");
                }else {
                    break;
                }
             }
            } catch(SQLException ex) {
                    ex.printStackTrace();
            }
        }

    public static void DeleteClientDetails() throws SQLException{
        try {
            Connection DBCONC = DatabaseConnector.DBConnect();
            Scanner scanner = new Scanner(System.in);
            int Client_ID = 0;
            String DFirstName, DLastName;
            int DAge;
            boolean COND = true;
            
            System.out.println("Do you want to see contents of table before you delete Client from it it? (Y/N): ");
            String UserDecision = scanner.next();

            if(UserDecision.equals("Y")){
                DisplayContentsOfTableClient();
            }

            while(COND == true){
                System.out.println("Want to Delete Information about Specific Client From Database: 'SC' or LastClientAdded: 'LC'?:");
                String UserMemberDec = scanner.next();
    
                if(UserMemberDec.equals("SC")){
                    System.out.println("Now you will be able to update details for specific CLient:");
    
                    System.out.println("Please Enter Client Name:");
                    DFirstName = scanner.next();
                    
                    System.out.println("Please Enter Client Last Name:");
                    DLastName= scanner.next();
    
                    System.out.println("Please Enter Client's Age/Date of Birth?");
                    DAge = scanner.nextInt();
                
                    Client_ID = getSpecificCLientId(DFirstName, DLastName, DAge);
                    System.out.println("Client_ID" + Client_ID);

                    String SQLQuery = "DELETE FROM Clients WHERE Client_ID =? AND FNAME=? AND LNAME=? AND AGE=?";
                    PreparedStatement ClientUpdate = DBCONC.prepareStatement(SQLQuery);
                    ClientUpdate.setInt(1,Client_ID);
                    ClientUpdate.setString(2, DFirstName);
                    ClientUpdate.setString(3, DLastName);
                    ClientUpdate.setInt(4, DAge);
                    ClientUpdate.executeUpdate();
                
                    }else if(UserMemberDec.equals("LC")){
                    Client_ID = getLastCLientId();
                    System.out.println(Client_ID);
                    String[] ar = searchForSpecificClientDetails(Client_ID);
                    DFirstName = ar[0];
                    DLastName = ar[1];
                    int Age = Integer.parseInt(ar[3]);
                    System.out.println(Age);
                    
                    String SQLQuery = "DELETE FROM Clients WHERE Client_ID =? AND FNAME=? AND LNAME=? AND AGE=?";
                    PreparedStatement ClientUpdate = DBCONC.prepareStatement(SQLQuery);
                    ClientUpdate.setInt(1, Client_ID);
                    ClientUpdate.setString(2, DFirstName);
                    ClientUpdate.setString(3, DLastName);
                    ClientUpdate.setInt(4, Age);
                    ClientUpdate.executeUpdate();

                    String QueryResetAutoIncrement = "ALTER TABLE Clients AUTO_INCREMENT = ?";
                    PreparedStatement ResetAutoIncrement = DBCONC.prepareStatement(QueryResetAutoIncrement);
                    ResetAutoIncrement.setInt(1, Client_ID);
                    ClientUpdate.executeUpdate();

                    }
                    DisplayContentsOfTableClient();
    
                    System.out.println("Are you happy with the result? (Y/N)");
                    String UserDec = scanner.next();
                    if (UserDec.equals("Y")){
                        COND = false;
                        break;
                    }else if (UserDec.equals("N")){
                        COND = true;
                        System.out.println("Now You Will Be Able To Do It Again");
                    }else {
                        break;
                    }
                }

          } catch(SQLException ex) {
                ex.printStackTrace();
        }
    }

    public static void DisplayContentsOfTableClient() throws SQLException{
       try{
        Connection DBCONC = DatabaseConnector.DBConnect();
        String queryTestClient = "SELECT * FROM Clients;";
        Statement myStatementClient = DBCONC.createStatement();
        ResultSet myStatementResultClient = myStatementClient.executeQuery(queryTestClient);
        ResultSetMetaData rsmd = myStatementResultClient.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while(myStatementResultClient.next()){
            for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(", ");
                    String columnValue = myStatementResultClient.getString(i);
                    System.out.print(columnValue + ":" + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
      }
    }

    public static String[] searchForSpecificClientDetails(int SearchID) throws SQLException{
                Connection DBCONC = DatabaseConnector.DBConnect();
                String ClientFirstname, ClientLastname, ClientId, ClientGender;
               // boolean FoundClient = false;
                int ClientAge;
                String ar[] = new String[4];
                
                String SearchIDStr = Integer.toString(SearchID);
                if(SearchIDStr == ""){
                    System.out.println("Please enter an id number to search member details" );
                }

                else{
                    try {
                    String sql1 = "Select * FROM Clients Where Client_ID = ?";
                    PreparedStatement sc = DBCONC.prepareStatement(sql1);
                    sc.setInt(1, SearchID);
                    ResultSet rs = sc.executeQuery();
                    if(rs.next()){
                        ClientFirstname = rs.getString("FNAME");
                        ClientLastname = rs.getString("LNAME");
                        ClientId = rs.getString("Client_ID");
                        ClientGender = rs.getString("GENDER");
                        ClientAge = rs.getInt("AGE");
                        System.out.println("FNAME: " + ClientFirstname + "LNAME: " + ClientLastname + "Client_ID" + ClientId + "GENDER: " + ClientGender + "AGE" + ClientAge);
                        ar[0]=  ClientFirstname;
                        ar[1] = ClientLastname;
                        ar[2] = ClientGender;
                        String ClientAgeS = Integer.toString(ClientAge);
                        ar[3] = ClientAgeS;
                        }
                        else{
                         System.out.println("There is no "+SearchID+" inside the database");
                         System.out.println(SearchID);
                     }
                    }catch(SQLException ex) {
                        ex.printStackTrace();
                }
               
                }
              return ar;
            }
    

    public static boolean CheckIfClientExists(int SearchID) throws SQLException {
        Connection DBCONC = DatabaseConnector.DBConnect();
        boolean FoundClient = false;
        String SearchIDStr = Integer.toString(SearchID);
        
        if(SearchIDStr.toString() == ""){
            System.out.println("Please enter an id number to search member details" );
            return FoundClient;
        } else{
            try {
            String sql1 = "SELECT * FROM Clients WHERE Client_ID = ?";
            PreparedStatement sc = DBCONC.prepareStatement(sql1);
            sc.setInt(1, SearchID);
            ResultSet rs = sc.executeQuery();

            if(rs.next()){
                FoundClient = true;
                System.out.println("Client_ID" + SearchID + "exists");    
                }
                else{
                 System.out.println("There is no "+SearchID+" inside the database");
                 FoundClient = false;
             }
            }catch(SQLException ex) {
                ex.printStackTrace();
          }
        }
        return FoundClient;
    }
}