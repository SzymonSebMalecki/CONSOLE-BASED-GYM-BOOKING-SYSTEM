import java.sql.*;
import java.util.*;

public class Trainer{

    public static void ADDCTrainerIntoDatabase(){
        try {
            Connection DBCONC = DatabaseConnector.DBConnect();
            boolean UserDecs = true;
    
            while (UserDecs = true){
            if(DBCONC != null){

           Scanner scanner = new Scanner(System.in);
        
            System.out.println("Now You Will Be Able to Add New Trainer To The Database");
            System.out.println("Please Enter Trainer's First Name:");
            String TrainerName = scanner.next();

            System.out.println("Please Enter Trainer's Last Name:");
            String TrainerLastName = scanner.next();
           // TrainerLastNames.add(TrainerLastName);
  
            System.out.println("Please Enter Trainer's Gender: MALE / FEMALE / PREFER NOT TO SAY");
            String TrainerGender = scanner.next();
      
        //Could Add Trainer Date Of Birth
         String DoubleBookingQuery = "SELECT COUNT(*) FROM Trainers WHERE FNAME = ? AND LNAME = ? AND GENDER = ?";
         PreparedStatement CheckWhetherAlreadyThere = DBCONC.prepareStatement(DoubleBookingQuery);
         CheckWhetherAlreadyThere.setString(1, TrainerName);
         CheckWhetherAlreadyThere.setString(2, TrainerLastName);
         CheckWhetherAlreadyThere.setString(3, TrainerGender);
         ResultSet RSDB = CheckWhetherAlreadyThere.executeQuery();
         if(RSDB.next()){
                System.out.println("This trainer has been already registered");
                break;
         }else {
            String sqlquery = "INSERT INTO Trainers(FNAME, LNAME, GENDER) VALUES (?,?,?)";
             PreparedStatement TrainerAdd = DBCONC.prepareStatement(sqlquery);  
             TrainerAdd.setString(1, TrainerName);
             TrainerAdd.setString(2, TrainerLastName);
             TrainerAdd.setString(3, TrainerGender);
             TrainerAdd.executeUpdate();  
             DisplayContentsOfTableTrainer();

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
    }
  }catch(SQLException ex){
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    }
}

    public static int getLastTrainerId(){
      int lastid = 0;
     try{       
             Connection DBCONC = DatabaseConnector.DBConnect();
             PreparedStatement ps = DBCONC.prepareStatement("SELECT * FROM  Trainers where TRAINER_ID");
             ResultSet RS = ps.executeQuery();
             RS = ps.executeQuery("SELECT MAX(TRAINER_ID) AS TRAINER_ID FROM Trainers");
             if(RS.next()){
              lastid = RS.getInt("Trainer_ID");
              System.out.println("LastID from function :"+ lastid); 
             }
             }catch(SQLException ex){
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
             }
        return lastid; 
 }


 public static int getSpecificTrainerId(String TrainerFirstName, String TrainerLastName){
    int Specific_ID = 0;
     try{
             Connection DBCONC = DatabaseConnector.DBConnect();
             String SQLQUERY = "SELECT CLIENT_ID FROM Trainers WHERE (FNAME = ? AND LNAME = ?)";
             PreparedStatement ps = DBCONC.prepareStatement(SQLQUERY);
             ps.setString(1, TrainerFirstName);
             ps.setString(2, TrainerLastName);
             ResultSet RS = ps.executeQuery();
             if(RS.next()){
              Specific_ID = RS.getInt("TRAINER_ID");
              System.out.println("SPECIFIED CLIENT_ID:"+ Specific_ID);
             }
             }catch(SQLException ex){
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
             }
             return Specific_ID;
      }

 
public static void DisplayContentsOfTableTrainer()  throws SQLException{
    try{
     Connection DBCONC = DatabaseConnector.DBConnect();
     String queryTestClient = "SELECT * FROM Trainers;";
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
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
   }
 }


 public static void DeleteTrainerDetails() throws SQLException{
    try {
        Connection DBCONC = DatabaseConnector.DBConnect();
        Scanner scanner = new Scanner(System.in);
        int Trainer_ID = 0;
        String DFirstName, DLastName;
        boolean COND = true;
        
        System.out.println("Do you want to see contents of table before you delete trainer from it it? (Y/N): ");
        String UserDecision = scanner.next();

        if(UserDecision.equals("Y")){
            DisplayContentsOfTableTrainer();
        }

        while(COND == true){
            System.out.println("Want to Delete Information about Specific Trainer From Database: 'ST' or LastTrainerAdded: 'LT'?:");
            String UserMemberDec = scanner.next();

            if(UserMemberDec.equals("ST")){
                System.out.println("Now you will be able to update details for specific trainer:");

                System.out.println("Please Enter Trainer Name:");
                DFirstName = scanner.next();
                
                System.out.println("Please Enter Trainer Last Name:");
                DLastName= scanner.next();

                Trainer_ID = getSpecificTrainerId(DFirstName, DLastName);
                System.out.println("Trainer_ID" + Trainer_ID);

                String SQLQuery = "DELETE FROM Trainers WHERE Trainer_ID =? AND FNAME=? AND LNAME=?";
                PreparedStatement ClientUpdate = DBCONC.prepareStatement(SQLQuery);
                ClientUpdate.setInt(1,Trainer_ID);
                ClientUpdate.setString(2, DFirstName);
                ClientUpdate.setString(3, DLastName);
                ClientUpdate.executeUpdate();
            
                }else if(UserMemberDec.equals("LT")){
                Trainer_ID = getLastTrainerId();
                System.out.println(Trainer_ID);
                String[] ar = searchTrainer(Trainer_ID);
                    DFirstName = ar[0];
                    DLastName = ar[1];
                    int Age = Integer.parseInt(ar[3]);
                    System.out.println(Age);

                    String SQLQuery = "DELETE FROM Trainers WHERE Trainer_ID =? AND FNAME=? AND LNAME=? AND AGE=?";
                    PreparedStatement ClientUpdate = DBCONC.prepareStatement(SQLQuery);
                    ClientUpdate.setInt(1, Trainer_ID);
                    ClientUpdate.setString(2, DFirstName);
                    ClientUpdate.setString(3, DLastName);
                    ClientUpdate.setInt(4, Age);
                    ClientUpdate.executeUpdate();

                    String QueryResetAutoIncrement = "ALTER TABLE Clients AUTO_INCREMENT = ?";
                    PreparedStatement ResetAutoIncrement = DBCONC.prepareStatement(QueryResetAutoIncrement);
                    ResetAutoIncrement.setInt(1, Trainer_ID);
                    ClientUpdate.executeUpdate();
                }
                DisplayContentsOfTableTrainer();

                System.out.println("Are you happy with the result? (Y/N)");
                String UserDec = scanner.next();
                if (UserDec.equals("Y")) {
                    COND = false;
                    break;
                } else if (UserDec.equals("N")) {
                    COND = true;
                    System.out.println("Now You Will Be Able To Do It Again");
                } else {
                    break;
                }
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
        }
    }

    public static void UpdateTrainerDetails() throws SQLException {
        try {
            Connection DBCONC = DatabaseConnector.DBConnect();
            String TrainerFirstName, TrainerLastName, TrainerGender;
            int Trainer_ID = 0;
            boolean COND = true;

            Scanner scanner = new Scanner(System.in);
            while (COND == true) {

                System.out.println("Do you want to see contents of table trainer before you update it? (Y/N): ");
                String UserDecision = scanner.next();

                if (UserDecision.equals("Y")) {
                    DisplayContentsOfTableTrainer();
                }
                System.out.println("Now you will be able to update details for specific CLient:");

                System.out.println("Please Enter Trainer Name:");
                TrainerFirstName = scanner.next();

                System.out.println("Please Enter Trainer Last Name:");
                TrainerLastName = scanner.next();

                System.out.println("Please Enter Client's Gender: MALE / FEMALE / PREFER NOT TO SAY");
                TrainerGender = scanner.next();

                Trainer_ID = getSpecificTrainerId(TrainerFirstName, TrainerLastName);
                System.out.println("Trainer_ID" + Trainer_ID);

                String SQLQuery = "Update Trainers SET FNAME=?, LNAME=? where Trainer_ID =?";
                PreparedStatement ClientUpdate = DBCONC.prepareStatement(SQLQuery);
                ClientUpdate.setString(1, TrainerFirstName);
                ClientUpdate.setString(2, TrainerLastName);
                ClientUpdate.setString(3, TrainerGender);
                ClientUpdate.setInt(4, Trainer_ID);
                ClientUpdate.executeUpdate();
                DisplayContentsOfTableTrainer();
                System.out.println("Are you happy with the result? (Y/N)");
                String UserDec = scanner.next();
                if (UserDec.equals("Y")) {
                    COND = false;
                    break;
                } else if (UserDec.equals("N")) {
                    COND = true;
                    System.out.println("Now You Will Be Able To Do It Again");
                } else {
                    break;
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
        }
    }

    public static String[] searchTrainer(int SearchID) {
             Connection DBCONC = DatabaseConnector.DBConnect();
             String ar[] = new String[4];
             String TrainerFirstname, TrainerLastName, TrainerId, TrainerGender;
             int TrainerAge;
             
             String SearchIDStr = Integer.toString(SearchID);
             if(SearchIDStr == ""){
                 System.out.println("Please enter an id number to search trainer for details" );
             }
             else{
                 try {
                 String sql = "Select * FROM Trainers Where Trainer_ID = ?";
                 PreparedStatement sc = DBCONC.prepareStatement(sql);
                 sc.setInt(1, SearchID);
                 ResultSet rs = sc.executeQuery();
                 if(rs.next()){
                     TrainerFirstname = rs.getString("FNAME");
                     TrainerLastName = rs.getString("LNAME");
                     TrainerId = rs.getString("Client_ID");
                     TrainerGender = rs.getString("GENDER");
                     TrainerAge = rs.getInt("AGE");
                     System.out.println("FNAME: " + TrainerFirstname + "LNAME: " + TrainerLastName + "Trainer_ID: " + TrainerId + "GENDER: " + TrainerGender + "AGE" + TrainerAge);
                     ar[0] = TrainerFirstname;
                     ar[1] = TrainerLastName;
                     ar[2] = TrainerGender;
                     String TrainerAgeS = Integer.toString(TrainerAge);
                     ar[3] = TrainerAgeS;
                     }
                     else{
                      System.out.println("there is no "+ SearchID +" inside the database");
                      
                  }
                 }catch(SQLException ex) {
                   System.out.println("SQLException: " + ex.getMessage());
                   System.out.println("SQLState: " + ex.getSQLState());
             }
             }
             return ar;
         }
 }