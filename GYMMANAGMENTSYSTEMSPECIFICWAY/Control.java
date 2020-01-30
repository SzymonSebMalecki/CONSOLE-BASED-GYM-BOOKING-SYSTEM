import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Control{
  public static void main(String[] args) throws Exception {
    try {
      Connection DBConnect = DatabaseConnector.DBConnect();
    
      while(DBConnect != null) {
        String UserDecisionOption;
        System.out.println(" ");
        System.out.println("Welcome to Gym Managment System");
        System.out.println("Type opt to list all available options or exit to exit");
        Scanner userDecision = new Scanner(System.in);
        String UserDecision = userDecision.next();
    
      if (UserDecision.equals("opt") | UserDecision.equals("OPT")){
        System.out.println("Select One of Options (TYPE STRING):");
        System.out.println("ADD booking-details");
        System.out.println("LISTALL Lists All Bookings");
        System.out.println("LISTALL client-id Lists All Bookings For Specific CLient");
        System.out.println("LISTALL date Lists All Bookings For Specific Date");
        System.out.println("UPDATE booking-id booking-details Update the booking with specified ID");
        System.out.println("DELETE booking-id Remove Booking with the specified ID");
        System.out.println("EXTRA SEE MORE OPTIONS FOR CLIENTS AND TRAINERS");
        Scanner userOption = new Scanner(System.in);
        UserDecisionOption = userDecision.next();

      } else if(UserDecision.equals("exit") | UserDecision.equals("EXIT") ){
          System.out.println("MANAGMENT TOOL Will NOW STOP AND YOU WILL BE DISCONNECTED FROM DATABASE:");
          DatabaseConnector.DBDisconnect();
          //DBConnect.close();
          break;
      }else{
          System.out.println("SELECTION ERROR OCCURED");
          break;
      }
      
      switch(UserDecisionOption) {
        case "ADD":
         AddBookingIntoDatase();
          break;
        case "LISTALL":
         DisplayAllContentsOfTableBooking();
         break;
        case "UPDATE":
         UpdateBookingDetails();
         break;
        case "DELETE":
        DeleteBooking();
        break;
        case "EXTRA":
        System.out.println("ADDT TRAINER DETAILS");
        System.out.println("ADDC CLIENT DETAILS");
        System.out.println("UPC UPDATE CLIENT DETAILS");
        System.out.println("UPT UPDATE TRAINER DETAILS");
        System.out.println("RMC REMOVE CLIENT DETAILS FOR SPECIFIC CLIENT");
        System.out.println("RMT REMOVE CLIENT DETAILS FOR SPECIFIC TRAINER");
        String UserDecisionOptionExtra = userDecision.next();
        
        switch(UserDecisionOptionExtra){
        case "ADDT":
          Trainer.ADDCTrainerIntoDatabase();
          break;
        case "ADDC":
          Client.ADDClientIntoDatabase();
          break;
        case "UPC":
         Client.UpdateClientDetails();
         break;
        case "UPT":
         Trainer.UpdateTrainerDetails();
         break;
        case "RMC":
         Client.DeleteClientDetails();
         break;
        case "RMT":
         Trainer.DeleteTrainerDetails();
         break;
        }
        default:
          break;
      }

    }
    
}catch(SQLException ex){
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
  }
}

public static void AddBookingIntoDatase() throws SQLException, Exception{
    try{
        Connection DBCONC = DatabaseConnector.DBConnect();
        Scanner scanner = new Scanner(System.in);
        boolean COND = true;
  
  while(COND = true){
        while(scanner.hasNext())
        {
            int CLIENT_ID = scanner.nextInt();
            int TRAINER_ID = scanner.nextInt();
            String ABOOKING_DATE = scanner.next(); // Format (YYYY-MM-DD)
            java.util.Date BookingDateFormated = new SimpleDateFormat("YYYY-MM-DD").parse(ABOOKING_DATE);
            java.sql.Date BookingDate = new java.sql.Date(BookingDateFormated.getTime());  
            String BookingDateS = BookingDate.toString();
          
            String ABOOKING_TIME = scanner.next(); // Format (HH-MM-SS)
            java.util.Date Booking_Time = new SimpleDateFormat("hh:mm:ss").parse(ABOOKING_TIME);
            String BookingTime = new SimpleDateFormat("hh:mm").format(Booking_Time);
            System.out.println(BookingTime);

            int DURATION = scanner.nextInt();
            String Focus = scanner.next();

            String query = "INSERT INTO Bookings (CLIENT_ID, TRAINER_ID, BOOKING_DATE, BOOKING_TIME, DURATION_OF_SESSION, FOCUS) VALUES(?,?,?,?,?,?)"; 
            PreparedStatement Bookings = DBCONC.prepareStatement(query);
            Bookings.setInt(1, CLIENT_ID);
            Bookings.setInt(2,TRAINER_ID);
            Bookings.setString(3, BookingDateS);
            Bookings.setString(4, BookingTime);
            Bookings.setInt(5, DURATION);
            Bookings.setString(6, Focus);
            Bookings.executeUpdate();
            DisplayAllContentsOfTableBooking();
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
      }
        } catch(SQLException ex) {
        ex.printStackTrace();
      } catch (Exception ex){
        ex.printStackTrace();
      }
    }
    
     public static void DisplayAllContentsOfTableBooking() throws SQLException, Exception{
        try{
         Connection DBCONC = DatabaseConnector.DBConnect();
         String queryTestClient = "SELECT * FROM Bookings;";
         Statement myStatementClient = DBCONC.createStatement();
         ResultSet myStatementResultClient = myStatementClient.executeQuery(queryTestClient);
         ResultSetMetaData rsmd = myStatementResultClient.getMetaData();
         int columnsNumber = rsmd.getColumnCount();
  
         while(myStatementResultClient.next()){
             for (int i = 1; i <= columnsNumber; i++) {
                     if (i > 1) System.out.print(", ");
                     String columnValue = myStatementResultClient.getString(i);
                     System.out.print(rsmd.getColumnName(i) + " : " +  columnValue);
                 }
                 System.out.println("");
             }
            } catch(SQLException ex) {
              ex.printStackTrace();
            } catch (Exception ex){
              ex.printStackTrace();
            }
     }


     public static void UpdateBookingDetails() throws SQLException{
      try {
          Connection DBCONC = DatabaseConnector.DBConnect();
          boolean COND = true;
      
      while(COND = true){
          Scanner scanner = new Scanner(System.in);
          
           while(scanner.hasNext()) 
           {

            int Update_CLIENT_ID = scanner.nextInt();
            int Update_TRAINER_ID = scanner.nextInt();
            String BOOKING_DATE = scanner.next(); // Format (YYYY-MM-DD)
            java.util.Date BookingDateFormated = new SimpleDateFormat("YYYY-MM-DD").parse(BOOKING_DATE);
            java.sql.Date BookingDate = new java.sql.Date(BookingDateFormated.getTime());  
            String BookingDateS = BookingDate.toString();
          
            String BOOKING_TIME = scanner.next(); // Format (HH-MM-SS)
            java.util.Date Booking_Time = new SimpleDateFormat("hh:mm:ss").parse(BOOKING_TIME);
            String Update_BookingTime = new SimpleDateFormat("hh:mm").format(Booking_Time);
            System.out.println(Update_BookingTime);

            int Update_DURATION = scanner.nextInt();
            String Update_Focus = scanner.next();

              int Booking_ID = getSpecificBookingId(Update_CLIENT_ID, Update_TRAINER_ID, BookingDate, Update_BookingTime, Update_DURATION);
              String SQLQuery = "Update Bookings SET CLIENT_ID =?, TRAINER_ID=?, BOOKING_DATE = ?, BOOKING_TIME = ?, DURATION = ?, FOCUS = ? where BOOKING_ID = ?";
               PreparedStatement BookingUpdate = DBCONC.prepareStatement(SQLQuery);
               BookingUpdate.setInt(1, Update_CLIENT_ID);
               BookingUpdate.setInt(2, Update_TRAINER_ID);
               BookingUpdate.setDate(3, BookingDate);
               BookingUpdate.setString(4, Update_BookingTime);
               BookingUpdate.setInt(5, Update_DURATION);
               BookingUpdate.setString(6, Update_Focus);
               BookingUpdate.setInt(7, Booking_ID);
               BookingUpdate.executeUpdate();
               DisplayAllContentsOfTableBooking();
               scanner.close();
           }
               Scanner USERDECIDE = new Scanner(System.in);
               System.out.println("Are you happy with the result? (Y/N)");
               String UserDec = USERDECIDE.next();
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
          } catch (Exception ex){
            ex.printStackTrace();
          }
       }
      
       public static void DeleteBooking() throws SQLException{
          try {
            Connection DBCONC = DatabaseConnector.DBConnect();
              Scanner scanner = new Scanner(System.in);
            boolean COND = true;
       
          while(COND = true){
            while(scanner.hasNext()) 
            {
              //First Thread
                int Delete_CLIENT_ID = scanner.nextInt();
                int Delete_TRAINER_ID = scanner.nextInt();
                String Delete_BOOKING_DATE = scanner.next(); // Format (YYYY-MM-DD)
                java.util.Date BookingDateFormated = new SimpleDateFormat("YYYY-MM-DD").parse(Delete_BOOKING_DATE);
                java.sql.Date BookingDate = new java.sql.Date(BookingDateFormated.getTime());  
    
                String Delete_BOOKING_TIME = scanner.next(); // Format (HH:MM:SS)
                java.util.Date Booking_Time = new SimpleDateFormat("hh:mm:ss").parse(Delete_BOOKING_TIME);
                String BookingTime = new SimpleDateFormat("h:mm a").format(Booking_Time);
                System.out.println(BookingTime);
 
                int DELETE_DURATION = scanner.nextInt();
                String DELETE_Focus = scanner.next();


                //Second Thread
                int Booking_ID = getSpecificBookingId(Delete_CLIENT_ID, Delete_TRAINER_ID, BookingDate, BookingTime, DELETE_DURATION);
            
                String SQLQuery = "DELETE FROM Bookings WHERE Booking_ID = ?";
                PreparedStatement BookingUpdate = DBCONC.prepareStatement(SQLQuery);
                BookingUpdate.setInt(1, Booking_ID);
                BookingUpdate.executeUpdate();
                DisplayAllContentsOfTableBooking();
 
                //Third Thread
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
           }
           } catch(SQLException ex) {
             ex.printStackTrace();
           } catch (Exception ex){
             ex.printStackTrace();
           }
        }
       
  public static int getSpecificBookingId(int Client_ID, int Trainer_ID, java.sql.Date Booking_DATE, String Booking_TIME, int Duration){
    int Specific_ID = 0;
     try{
             Connection DBCONC = DatabaseConnector.DBConnect();
             String SQLQUERY = "SELECT BOOKING_ID FROM Bookings WHERE (CLIENT_ID = ? AND TRAINER_ID = ? AND BOOKING_DATE = ? AND BOOKING_TIME = ? AND DURATION = ?";
             PreparedStatement ps = DBCONC.prepareStatement(SQLQUERY);
             ps.setInt(1, Client_ID);
             ps.setInt(2, Trainer_ID);
             ps.setDate(3, Booking_DATE);
             ps.setString(4, Booking_TIME);
             ps.setInt(5, Duration);
             ResultSet RS = ps.executeQuery();
          ;
             if(RS.next()){
              Specific_ID = RS.getInt("BOOKING_ID");
              System.out.println("SPECIFIED BOOKING_ID: "+ Specific_ID);
             }
             }catch(SQLException ex){
                 System.out.println("SQLException: " + ex.getMessage());
                 System.out.println("SQLState: " + ex.getSQLState());
             }
             return Specific_ID;
      }
    }
  
