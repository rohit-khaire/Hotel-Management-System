# Hotel-Management-System
**Overview**

The Hotel Management System is a command-line application designed to automate and simplify the process of managing customer bookings, room occupancy, and check-outs in a hotel with a 50-room capacity. This project is developed using Java and JDBC (Java Database Connectivity), with MySQL as the backend database to handle and store customer data and room booking information.

The system allows hotel staff to:
Add new customers to the hotel’s database, ensuring room availability.
Track customers checking out on a particular day.
View the real-time room occupancy and manage bookings.
Delete customer records from the system.

**Features**

Customer Management:
Add new customers by collecting details such as name, age, contact information, and the number of days for booking.
Automatically assigns available rooms based on occupancy status.
Allows deleting customer records when necessary.

Room Management:
The system ensures room assignments are made dynamically, based on availability.
Prevents new bookings when the hotel has reached its maximum capacity of 50 rooms.

Check-in and Check-out Management:
Provides the functionality to display customers who are checking out on the current day.
Maintains real-time room status, showing the details of the customers occupying each room.

Validation:
The system includes input validation for customer details, ensuring proper data (e.g., valid age, contact number) is entered, improving data integrity.

**Technologies Used**

Java: The application is developed using Java to ensure portability, ease of use, and object-oriented design principles.<br>
JDBC (Java Database Connectivity): Utilized for database connection, SQL queries, and updates.<br>
MySQL: A relational database used to store customer and room booking information.<br>
LocalDate (Java): Used for handling dates (e.g., check-in/check-out dates) for better date management.<br>

**Installation**

Prerequisites:
Java & MySQL

**Steps to Run:**

Clone this repository to your local machine:

git clone https://github.com/rohit-khaire/Hotel-Management-System.git

Set up a MySQL database named hoteldb and configure the necessary tables. You can use the SQL script provided to create the necessary schema.

Update the URL, USER, and PASSWORD values in the HotelManagementApp.java file with your MySQL database credentials.

Compile and run the application:

javac -cp .;mysql-connector-j-9.0.0.jar HotelManagementApp.java <br>
java HotelManagement.java

Follow the on-screen prompts to interact with the system. Ready To Use!

**Future Enhancements**

Graphical User Interface (GUI): Implement a user-friendly GUI using JavaFX or Swing for improved interaction.<br>
Booking Confirmation: Send email confirmations to customers upon successful booking. <br>
Payment Integration: Add support for integrating payment systems for booking management.

**Conclusion**

This project provides a practical solution for small to medium-sized hotel operations, streamlining room management and customer data handling. By using Java, JDBC, and MySQL, the application showcases the integration of core technologies to manage business operations efficiently.
