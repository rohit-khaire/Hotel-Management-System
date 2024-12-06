import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class HotelManagementApp {
    private static final String URL = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String USER = "root"; 
    private static final String PASSWORD = "tiger"; 
    private static final int TOTAL_ROOMS = 50; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void addCustomer() {
        try (Connection connection = getConnection()) {
            String roomCountQuery = "SELECT COUNT(*) AS room_count FROM customers";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(roomCountQuery)) {
                if (resultSet.next() && resultSet.getInt("room_count") >= TOTAL_ROOMS) {
                    System.out.println("All rooms are booked. No more customers can be added.");
                    return;
                }
            }
            Scanner scanner = new Scanner(System.in);
            String name = "";
            while (true) {
                System.out.print("Enter Customer Name: ");
                name = scanner.nextLine();
                if (isValidName(name)) {
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter valid name.");
                }
            }
            int age = 0;
            while (true) {
                System.out.print("Enter Customer Age: ");
                if (scanner.hasNextInt()) {
                    age = scanner.nextInt();
                    if (age > 0 && age < 120) break;
                } else {
                    System.out.println("Invalid input. Please enter a valid age.");
                }
                scanner.nextLine(); 
            }

            scanner.nextLine(); 
            String contact = "";
            while (true) {
                System.out.print("Enter Customer Contact Number (10 digits only): ");
                contact = scanner.nextLine();
                if (contact.length() == 10 && isAllDigits(contact)) {
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid 10-digit contact number.");
                }
            }

            int daysBooked = 0;
            while (true) {
                System.out.print("Enter Number of Days Room Booked: ");
                if (scanner.hasNextInt()) {
                    daysBooked = scanner.nextInt();
                    if (daysBooked >= 0) break; 
                } else {
                    System.out.println("Invalid input. Please enter a valid number of days.");
                }
                scanner.nextLine(); 
            }

            LocalDate checkinDate = LocalDate.now();
            LocalDate checkoutDate = checkinDate.plusDays(daysBooked);

            String roomQuery = "SELECT MIN(room_number + 1) AS next_room FROM customers WHERE room_number + 1 NOT IN (SELECT room_number FROM customers)";
            int roomNumber = 1; 
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(roomQuery)) {
                if (resultSet.next() && resultSet.getInt("next_room") > 0) {
                    roomNumber = resultSet.getInt("next_room");
                }
            }

            String insertQuery = "INSERT INTO customers (name, age, contact, checkin_date, checkout_date, room_number) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, contact);
                preparedStatement.setDate(4, Date.valueOf(checkinDate));
                preparedStatement.setDate(5, Date.valueOf(checkoutDate));
                preparedStatement.setInt(6, roomNumber); 
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Customer added successfully. Room Number: " + roomNumber + ", Checkout Date: " + checkoutDate);
                } else {
                    System.out.println("Failed to add customer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isAllDigits(String contact) {
        for (char c : contact.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+");
    }

    public static void showCheckoutsToday() {
        LocalDate today = LocalDate.now();
        String selectQuery = "SELECT * FROM customers WHERE checkout_date = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setDate(1, Date.valueOf(today));
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Customers checking out today:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");
                int roomNumber = resultSet.getInt("room_number");
                System.out.println("ID: " + id + ", Name: " + name + ", Contact: " + contact + ", Room Number: " + roomNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewRoomOccupancy() {
        String selectQuery = "SELECT * FROM customers";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            System.out.println("Room Occupancy Details:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String contact = resultSet.getString("contact");
                int roomNumber = resultSet.getInt("room_number");
                Date checkinDate = resultSet.getDate("checkin_date");
                Date checkoutDate = resultSet.getDate("checkout_date");

                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + 
                    ", Contact: " + contact + ", Room Number: " + roomNumber + 
                    ", Check-in: " + checkinDate + ", Check-out: " + checkoutDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Customer ID to delete: ");
        int customerId = scanner.nextInt();

        String deleteQuery = "DELETE FROM customers WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, customerId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer with ID " + customerId + " deleted successfully.");
            } else {
                System.out.println("No customer found with ID " + customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
	System.out.println("\nHotel Management System");
        while (true) {
            System.out.println("\n1. Add Customer");
            System.out.println("2. Show Customers Checking Out Today");
            System.out.println("3. View Room Occupancy");
            System.out.println("4. Delete Customer");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            System.out.println();

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    showCheckoutsToday();
                    break;
                case 3:
                    viewRoomOccupancy();
                    break;
                case 4:
                    deleteCustomer(); 
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}