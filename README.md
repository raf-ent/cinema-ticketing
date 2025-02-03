
# Cinema Management System

A Java-based Movie Ticket Booking System that uses SQLite for data persistence. This project provides a simple console application that allows users to create an account, log in, browse movies, and book or cancel tickets. The system also includes a suite of JUnit tests to verify functionality.

## Features

- **User Authentication:** Create new accounts and log in as a visitor (or admin, if implemented).
- **Movie Management:** Add, update, and delete movies.
- **Show Management:** Add and edit movie showtimes.
- **Ticket Booking:** Book tickets for movie shows and cancel bookings.
- **SQLite Integration:** Uses a file-based SQLite database for storage.
- **JUnit Tests:** A comprehensive suite of tests to validate functionality.

## Prerequisites

- **Java Development Kit (JDK) 8 or later**
- **SQLite JDBC Driver**
- **JUnit 5** 

## Setup & Installation 

1. **Clone the Repository**

```
git clone https://github.com/raf-ent/cinema-ticketing.git

cd cinema-ticketing
```
2. **Add Library Dependencies**

    - In IntelliJ, open **File → Project Structure**.
    - Select **Modules** on the left, then the **Dependencies** tab.
    - Click the **+** button and choose **JARs or Directories...**.
    - Navigate to the `lib/` folder in your project and select all the jar files.
    - Click **OK** and **Apply** the changes.

3. **Build the Project**

    - IntelliJ should automatically compile the project. If not, select **Build → Build Project** from the top menu.

## Running the Application

1. **Run the Main Class**

    - In the Project Explorer, locate `Main.java` under the `src/` folder.
    - Right-click on `Main.java` and select **Run 'Main.main()'**.
    - The console window will open, and you can follow the on-screen instructions to log in or create a new account.

## Running JUnit Tests

1. **Run Tests from IntelliJ IDEA**

    - In the Project Explorer, locate `JUnit.java` under the `src/` folder.
    - Right-click on `JUnit.java` and select **Run**.
    - The **Run** window will display the test results.



