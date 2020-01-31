import java.sql.*;
//import java.sql.Date;
import java.util.*;

public class ApplicationProtocol {
  public static void main(String[] args) throws Exception {
    try {
      Connection DBConnect = DatabaseConnector.DBConnect();

      while (DBConnect != null) {
        Scanner userDecision = new Scanner(System.in);
        String UserDecisionOption;
        System.out.println(" ");
        System.out.println("Welcome to Gym Managment System");
        System.out.println("Type opt to list all available options or exit to exit");
        String UserDecision = userDecision.next();

        if (UserDecision.equals("opt") | UserDecision.equals("OPT")) {
          System.out.println("Select One of Options (TYPE STRING):");
          System.out.println("ADD booking-details");
          System.out.println("LISTALL Lists All Bookings");
          System.out.println("LISTALL client-id Lists All Bookings For Specific CLient");
          System.out.println("LISTALL date Lists All Bookings For Specific Date");
          System.out.println("UPDATE booking-id booking-details Update the booking with specified ID");
          System.out.println("DELETE booking-id Remove Booking with the specified ID");
          System.out.println("EXTRA SEE MORE OPTIONS FOR CLIENTS AND TRAINERS");
          UserDecisionOption = userDecision.next();

        } else if (UserDecision.equals("exit") | UserDecision.equals("EXIT")) {
          System.out.println("MANAGMENT TOOL Will NOW STOP AND YOU WILL BE DISCONNECTED FROM DATABASE:");
          DatabaseConnector.DBDisconnect();
          // DBConnect.close();
          break;
        } else {
          System.out.println("SELECTION ERROR OCCURED");
          break;
        }

        switch (UserDecisionOption) {
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

          switch (UserDecisionOptionExtra) {
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

    } catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    }
  }

  public static void AddBookingIntoDatase() throws SQLException, Exception {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
   
     while (DBCONC != null) {
      System.out.print("ENTER IN THIS ORDER TO ADD(CLIENT_ID TRAINER_ID BOOKING_DATE(YYYY-MM-DD) BOOKING_TIME(HH-MM) DURATION_OF_SESSION FOCUS): ");
      while (scanner.hasNext()) {
          int CLIENT_ID = scanner.nextInt();
          int TRAINER_ID = scanner.nextInt();
          String BOOKING_DATE = scanner.next(); 
         // System.out.println(BOOKING_DATE);
          String BOOKING_TIME = scanner.next();
        //  System.out.println(BOOKING_TIME);
          int DURATION = scanner.nextInt(); 
          String Focus = scanner.next();
    
        String CheckDB = "SELECT * FROM Bookings WHERE BOOKING_TIME= ? OR BOOKING_DATE = ?";   
        PreparedStatement Check_Double = DBCONC.prepareStatement(CheckDB);
        Check_Double.setString(1, BOOKING_TIME);
        Check_Double.setString(2, BOOKING_DATE);
        ResultSet CHECKDouble = Check_Double.executeQuery();

          if(CHECKDouble.next()){
               System.out.println("Double Bookings Are Not Allowed");
               System.out.println("There's already one booking on " + BOOKING_DATE + " at " + BOOKING_TIME);
               break;
            } else{
              System.out.println("Your booking will be allowed");
              String query = "INSERT INTO Bookings (CLIENT_ID, TRAINER_ID, BOOKING_DATE, BOOKING_TIME, DURATION_OF_SESSION, FOCUS) VALUES(?,?,?,?,?,?)";
              PreparedStatement Bookings = DBCONC.prepareStatement(query);
              Bookings.setInt(1, CLIENT_ID);
              Bookings.setInt(2, TRAINER_ID);
              Bookings.setString(3, BOOKING_DATE);
              Bookings.setString(4, BOOKING_TIME);
              Bookings.setInt(5, DURATION);
              Bookings.setString(6, Focus);
              Bookings.executeUpdate();
              DisplayAllContentsOfTableBooking();
              System.out.println("DO YOU WANT TO ADD MORE USERS? (Y/N): ");
              String UserDec = scanner.next();
              if(UserDec.equals("Y") | UserDec.equals("y")){
                continue;
              }else if(UserDec.equals("N") | UserDec.equals("N")){
              DBCONC.close();
              break;
              }else{
                break;
              }
              }
            } 
          }
     }catch(SQLException ex){
    ex.printStackTrace();
  }catch(Exception ex){
    System.out.println("Make Sure that CLIENT/USER ID THAT YOU ENTERED EXIST AND YOU ENTERED ALL REQUIRED DATA");
    //ex.printStackTrace();
  }
  }

  public static void DisplayAllContentsOfTableBooking() throws SQLException, Exception {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
      String UserInput;

     // switch(UserInput){
     //   case("ALL");
      String queryAllBookings = "SELECT * FROM Bookings";


      String queryListSpecificClient = "SELECT * FROM Bookings WHERE CLIENT_ID = ?";

      Statement ListBookings = DBCONC.createStatement();
      ResultSet ListBookingsResult = ListBookings.executeQuery(queryAllBookings);
      ResultSetMetaData LBMD = ListBookingsResult.getMetaData();
      int columnsNumber = LBMD.getColumnCount();

      while (ListBookingsResult.next()) {
        for (int i = 1; i <= columnsNumber; i++) {
          if (i > 1)
            System.out.print(", ");
          String columnValue = ListBookingsResult.getString(i);
          System.out.print(LBMD.getColumnName(i) + " : " + columnValue);
        }
        System.out.println("");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void UpdateBookingDetails() throws SQLException {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
     // boolean COND = true;
      System.out.print("TYPE TO UPDATE(BOOKING_ID CLIENT_ID TRAINER_ID BOOKING_DATE(YYYY-MM-DD) BOOKING_TIME(HH-MM) DURATION_OF_SESSION FOCUS): ");
        Scanner scanner = new Scanner(System.in);

  while (DBCONC != null) {
        while (scanner.hasNext()) {
          int Booking_ID = scanner.nextInt();
          int Update_CLIENT_ID = scanner.nextInt();
          int Update_TRAINER_ID = scanner.nextInt();
          String BOOKING_DATE = scanner.next(); // Format (YYYY-MM-DD)
          String BOOKING_TIME = scanner.next(); // Format (HH-MM)
          int Update_DURATION = scanner.nextInt();
          String Update_Focus = scanner.next();
  
          String CheckB = "SELECT * FROM Bookings WHERE BOOKING_ID= ?";   
          PreparedStatement Check_Double = DBCONC.prepareStatement(CheckB);
           Check_Double.setInt(1, Booking_ID);
           ResultSet CHECKBooking = Check_Double.executeQuery();

          //Select * FROM BOOKINGS WHERE NUMBER ENTERED IF SUCCESFUL ELSE BOOKING DOES NOT EXCITS
          if(CHECKBooking.next()){
            System.out.println("Specified BookingID exist");
            String SQLQuery = "Update Bookings SET CLIENT_ID =?, TRAINER_ID=?, BOOKING_DATE = ?, BOOKING_TIME = ?,  DURATION_OF_SESSION = ?, FOCUS = ? where BOOKING_ID = ?";
            PreparedStatement BookingUpdate = DBCONC.prepareStatement(SQLQuery);
            BookingUpdate.setInt(1, Update_CLIENT_ID);
            BookingUpdate.setInt(2, Update_TRAINER_ID);
            BookingUpdate.setString(3, BOOKING_DATE);
            BookingUpdate.setString(4, BOOKING_TIME);
            BookingUpdate.setInt(5, Update_DURATION);
            BookingUpdate.setString(6, Update_Focus);
            BookingUpdate.setInt(7, Booking_ID);
            BookingUpdate.executeUpdate();
            DisplayAllContentsOfTableBooking();
          } else{
            System.out.println("Specified BookingID does not exist");
            break;
          }
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void DeleteBooking() throws SQLException {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
      System.out.print("YOUR BOOKINGID:");

    while (DBCONC != null) {
        while (scanner.hasNext()) {
          int Booking_ID = scanner.nextInt();
         
          String SQLQuery = "DELETE FROM Bookings WHERE Booking_ID = ?";
          PreparedStatement BookingUpdate = DBCONC.prepareStatement(SQLQuery);
          BookingUpdate.setInt(1, Booking_ID);
          BookingUpdate.executeUpdate();
          DisplayAllContentsOfTableBooking();
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static int getSpecificBookingId(int Client_ID, int Trainer_ID, String Booking_DATE, String Booking_TIME,
      int Duration) {
    int Specific_ID = 0;
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      String SQLQUERY = "SELECT BOOKING_ID FROM Bookings WHERE (CLIENT_ID = ? AND TRAINER_ID = ? AND BOOKING_DATE = ? AND BOOKING_TIME = ? AND DURATION = ?";
      PreparedStatement ps = DBCONC.prepareStatement(SQLQUERY);
      ps.setInt(1, Client_ID);
      ps.setInt(2, Trainer_ID);
      ps.setString(3, Booking_DATE);
      ps.setString(4, Booking_TIME);
      ps.setInt(5, Duration);
      ResultSet RS = ps.executeQuery();
      ;
      if (RS.next()) {
        Specific_ID = RS.getInt("BOOKING_ID");
        System.out.println("SPECIFIED BOOKING_ID: " + Specific_ID);
      }
    } catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    }
    return Specific_ID;
  }
}

/*
 * public static boolean CheckForDoubleBookings(int SearchID) throws
 * SQLException{
 * 
 * Connection DBCONC = DatabaseConnector.DBConnect(); String ClientID,
 * TrainerID, Focus; String BookingDate; int DurationOfSession;
 * 
 * // CLIENT_ID =?, TRAINER_ID=?, BOOKING_DATE = ?, BOOKING_TIME = ?,
 * DURATION_OF_SESSION = ?, FOCUS = ?
 * 
 * String ar[] = new String[4];
 * 
 * String SearchIDStr = Integer.toString(SearchID); if(SearchIDStr == ""){
 * System.out.println("Please enter an id number to search member details" ); }
 * 
 * else{ try { String SQL = "Select * FROM Clients Where Booking_ID = ?";
 * PreparedStatement sc = DBCONC.prepareStatement(SQL); sc.setInt(1, SearchID);
 * ResultSet rs = sc.executeQuery(); if(rs.next()){ ClientID =
 * rs.getString("CLIENT_ID"); TrainerID = rs.getString("TRAINER_ID");
 * BookingDate = rs.getString("BOOKING_DATE"); DurationOfSession =
 * rs.getInt("DURATION_OF_SESSION");
 * 
 * ClientAge = rs.getInt("AGE"); System.out.println("FNAME: " + ClientFirstname
 * + "LNAME: " + ClientLastname + "Client_ID" + ClientId + "GENDER: " +
 * ClientGender + "AGE" + ClientAge);
 * 
 * } else{ System.out.println("There is no "+SearchID+" inside the database");
 * System.out.println(SearchID); } }catch(SQLException ex) {
 * ex.printStackTrace(); }
 */
