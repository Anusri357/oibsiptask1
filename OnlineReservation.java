package rail;

import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class OnlineReservation 
{

    public static class User 
    {
        String username;
        String password;

        Scanner sc = new Scanner(System.in);

     //Method to get username from user input
        public String getUsername() 
        {
            System.out.println("Enter Username:\n");
            return sc.nextLine();
        }

     //Method to get password from user input        
        public String getPassword() 
        {
            System.out.println("Enter Password:\n");
            return sc.nextLine();
        }
    }

    public static class PnrRecord 
    {
         int pnrNumber;
         String passengerName;
         String trainNumber;
         String trainName; 
         String classType;
         String journeyDate;
         String from;
         String to;

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

     //Method to generate a random PNR number
        public int generatePnrNumber() 
        {
            return random.nextInt(9000) + 1000;
        }

     //Method to get passenger name from user input
        public String getPassengerName() 
        {
            System.out.println("Enter passenger name:\n");
            return sc.nextLine();
        }

     //Method to get train number from user input and fetch train name from database
        public String getTrainNumber() 
        {
            System.out.println("Enter train number:\n");
            trainNumber = sc.nextLine();
            fetchTrainName(); // Fetch train name based on train number
            return trainNumber;
        }

      //Method to fetch train name based on train number from database
        private void fetchTrainName() 
        {
            String url = "jdbc:mysql://localhost:3306/ANU";
            String username = "root";
            String password = "Anusri@357";

            String query = "SELECT train_name FROM trains WHERE train_number = '" + trainNumber + "'";
            try (Connection connection = DriverManager.getConnection(url, "root", "Anusri@357");
                 Statement stmt = connection.createStatement()) 
            {
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) 
                {
                    trainName = rs.getString("train_name");
                } 
                else 
                {
                    trainName = "Train name not found";
                }
            } 
            catch (SQLException e) 
            {
                System.err.println("Error in fetching train name: " + e.getMessage());
            }
        }

      //Method to get class type from user input
        public String getClassType() 
        {
            System.out.println("Enter class type:\n");
            return sc.nextLine();
        }

      //Method to get journey date from user input
        public String getJourneyDate() 
        {
            System.out.println("Enter journey date (YYYY-MM-DD):");
            return sc.nextLine();
        }

      //Method to get starting place from user input
        public String getFrom() 
        {
            System.out.println("Enter starting place:\n");
            return sc.nextLine();
        }

     //Method to get destination place from user input
        public String getTo() 
        {
            System.out.println("Enter destination place:\n");
            return sc.nextLine();
        }

     //Method to return the fetched train name
        public String getTrainName() 
        {
            return trainName;
        }
    }

    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        User user = new User();
        String username = user.getUsername();
        String password = user.getPassword();

        String url = "jdbc:mysql://localhost:3306/ANU";
        try (Connection connection = DriverManager.getConnection(url, "root", "Anusri@357")) 
        {
            System.out.println("User Connection Granted.\n");
            Statement stmt = connection.createStatement();
            while (true) 
            {
                System.out.println("Enter your choice:");
                System.out.println("1. Insert Record");
                System.out.println("2. Delete Record");
                System.out.println("3. Show All Records");
                System.out.println("4. Exit");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) 
                {
                    case 1:
                        PnrRecord pnrRecord = new PnrRecord();
                        int pnrNumber = pnrRecord.generatePnrNumber();
                        String passengerName = pnrRecord.getPassengerName();
                        String trainNumber = pnrRecord.getTrainNumber();
                        String trainName = pnrRecord.getTrainName(); 
                        String classType = pnrRecord.getClassType();
                        String journeyDate = pnrRecord.getJourneyDate();
                        String from = pnrRecord.getFrom();
                        String to = pnrRecord.getTo();

                     //SQL query to insert a new record into the reservations table
                        String insertSQL = "INSERT INTO reservations VALUES (" +
                                pnrNumber + ", '" +
                                passengerName + "', '" +
                                trainNumber + "', '" +
                                trainName + "', '" + 
                                classType + "', '" +
                                journeyDate + "', '" +
                                from + "', '" +
                                to + "')";

                     //Execute the insert query
                        int rowsAffected = stmt.executeUpdate(insertSQL);
                        if (rowsAffected > 0) 
                        {
                            System.out.println("Ticket booked successfully!!");
                        } 
                        else 
                        {
                            System.out.println("Ticket not booked! Try Again!!");
                        }
                        break;

                    case 2:
                        System.out.println("Enter the PNR number to cancel the ticket:\n"+ "");
                        int pnrToDelete = sc.nextInt();
                        sc.nextLine(); // consume newline
                     //SQL query to delete a record(cancel ticket) from the reservations table
                        String deleteSQL = "DELETE FROM reservations WHERE pnr_number = " + pnrToDelete;
                     //Execute the delete query
                        int rowsDeleted = stmt.executeUpdate(deleteSQL);
                        if (rowsDeleted > 0) 
                        {
                            System.out.println("Ticket cancellation confirmed!!");
                        } 
                        else 
                        {
                            System.out.println("Ticket not cancelled, Try Again!!");
                        }
                        break;

                    case 3:
                    //Retrieve all records from the reservations table and display them
                        ResultSet rs= stmt.executeQuery("SELECT * FROM reservations");
                        while (rs.next()) 
                        {
                            System.out.println("PNR Number: " + rs.getInt("pnr_number"));
                            System.out.println("Passenger Name: " + rs.getString("passenger_name"));
                            System.out.println("Train Number: " + rs.getString("train_number"));
                            System.out.println("Train Name: " + rs.getString("train_name")); 
                            System.out.println("Class Type: " + rs.getString("class_type"));
                            System.out.println("Journey Date: " + rs.getString("journey_date"));
                            System.out.println("From Location: " + rs.getString("departure_place"));
                            System.out.println("To Location: " + rs.getString("destination_place"));
                            System.out.println("**********");
                        }
                        break;

                    case 4:
                        System.out.println("Exit");
                        return;

                    default:
                        System.out.println("Invalid choice!!");
                        break;
                }
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
}
