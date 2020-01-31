import java.sql.*;
import java.util.*;

/* ------------------------- Application Protocol  --------------------------- */
public class ApplicationProtocol {
  public static void main(String[] args) throws Exception {
    try {
      Connection DBConnect = DatabaseConnector.DBConnect();  //Calling DBConnect method from DatabaseConnector.java so that connection to database can be establised

      while (DBConnect != null) {
        Scanner userDecision = new Scanner(System.in);
        String UserDecisionOption;

        System.out.println(" ");
        System.out.println("Welcome to Gym Managment System");
        System.out.println("Type opt to list all available options or exit to exit");
        String UserDecision = userDecision.next();

        if (UserDecision.equals("opt") | UserDecision.equals("OPT")) { //Both all Uppercase and all Lowercase input can be accepted
          System.out.println("Select One of Options (TYPE STRING):");
          System.out.println("ADD booking-details");
          System.out.println("LISTALL Lists All Bookings");
          System.out.println("LISTALLC Lists All Bookings For Specific CLient");
          System.out.println("LISTALLD Lists All Bookings For Specific Date");
          System.out.println("UPDATE booking-id booking-details Update the booking with specified ID");
          System.out.println("DELETE booking-id Remove Booking with the specified ID");
          System.out.println("EXTRA SEE MORE OPTIONS FOR CLIENTS AND TRAINERS");
          UserDecisionOption = userDecision.next();

        } else if (UserDecision.equals("exit") | UserDecision.equals("EXIT")) { //Both all Uppercase and all Lowercase input can be accepted
          System.out.println("MANAGMENT TOOL Will NOW STOP AND YOU WILL BE DISCONNECTED");
          DatabaseConnector.DBDisconnect(); //Calling DBDisconnect method from DatabaseConnector.java so that connection to database can be closed
          break;
        } else {
          System.out.println("SELECTION ERROR OCCURED"); //If none of user input is valid, display message and stop program from running
          break;
        }

        //Switch Statement calling appropriate method depending on selected option
        switch (UserDecisionOption) {
        case "ADD":
          AddBookingIntoDatabase();
          break;
        case "LISTALL":
          DisplayAllContentsOfTableBooking();
          break;
        case "LISTALLC":
          DisplayAllContentsOfTableForClient();
          break;
          case "LISTALLD":
          DisplayAllContentsOfTableForDate();
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
          System.out.println("DAC Lists All Clients From Clients table");
          System.out.println("DAT Lists All Trainers From Trainers table");
          String UserDecisionOptionExtra = userDecision.next();

          //Switch Statement for extra options for managing Trainer and Client Tables
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
          case "DAC":
            Client.DisplayContentsOfTableClient();
            break;
          case "DAT":
            Trainer.DisplayContentsOfTableTrainer();
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

  // Method for Adding Bookings Into Database
  public static void AddBookingIntoDatabase() throws SQLException, Exception {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
   
     while (DBCONC != null) {
      System.out.print("ENTER IN THIS ORDER TO ADD(CLIENT_ID TRAINER_ID BOOKING_DATE(YYYY-MM-DD) BOOKING_TIME(HH-MM) DURATION_OF_SESSION FOCUS): ");
      while (scanner.hasNext()) {
          int CLIENT_ID = scanner.nextInt();
          int TRAINER_ID = scanner.nextInt();
          String BOOKING_DATE = scanner.next(); ;
          String BOOKING_TIME = scanner.next();
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
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
  }catch(Exception ex){
    System.out.println("Make Sure that CLIENT/USER ID THAT YOU ENTERED EXIST AND YOU ENTERED ALL REQUIRED DATA");
  }
  }

  // Method for displaying all contents of table Bookings
  public static void DisplayAllContentsOfTableBooking() throws SQLException, Exception {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
      String queryAllBookings = "SELECT * FROM Bookings";
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
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

// Method for displaying all bookings for specific CLIENT_ID
  public static void DisplayAllContentsOfTableForClient() throws SQLException, Exception {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
      System.out.print("ENTER Client_ID:");
      int Client_ID = scanner.nextInt();
      String queryAllBookingsForClient = "SELECT * FROM Bookings WHERE CLIENT_ID = ?";
      PreparedStatement ListForClient = DBCONC.prepareStatement(queryAllBookingsForClient);
      ListForClient.setInt(1, Client_ID);
      ResultSet ListBookingsResult = ListForClient.executeQuery();
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
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

// Method for displaying all bookings for specific BookingDATE
  public static void DisplayAllContentsOfTableForDate() throws SQLException, Exception {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
      System.out.print("ENTER DATE(YYYY-MM-DD): ");
      String BookingDate = scanner.next();

      String queryAllBookingsForSpecifiedDate = "SELECT * FROM Bookings WHERE BOOKING_DATE = ?";
      PreparedStatement ListForDate = DBCONC.prepareStatement(queryAllBookingsForSpecifiedDate);
      ListForDate.setString(1, BookingDate);

      ResultSet ListBookingsResult = ListForDate.executeQuery();
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
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // Method for Updating Contents of Table Bookings
  public static void UpdateBookingDetails() throws SQLException {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      System.out.print("ENTER IN THIS ORDER TO UPDATE(BOOKING_ID CLIENT_ID TRAINER_ID BOOKING_DATE(YYYY-MM-DD) BOOKING_TIME(HH-MM) DURATION_OF_SESSION FOCUS): ");
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
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

   // Method for Deleting Specific Tuples in the database for specified BookingID
  public static void DeleteBooking() throws SQLException {
    try {
      Connection DBCONC = DatabaseConnector.DBConnect();
      Scanner scanner = new Scanner(System.in);
      System.out.print("ENTER BOOKING_ID TO DELETE SPECIFIC RECORD:");

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
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

/* ------------------------- Application Protocol  --------------------------- */
