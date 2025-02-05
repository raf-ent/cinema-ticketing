# **Movie Ticket Booking System**

This project is a **Java-based** console application for booking movie tickets. It utilizes **SQLite** as a file-based database to manage movies, users, and bookings efficiently. The system supports user authentication, movie and show management, ticket booking/cancellation, and includes **JUnit tests** to ensure stability.

---

## **🚀 Features**
- 🎟 **User Authentication** – Register and log in as a visitor (or admin, if extended)
- 🎬 **Movie Management** – Add, update, and delete movies
- ⏰ **Show Management** – Dynamically manage movie showtimes
- 🍿 **Ticket Booking** – Book and cancel tickets seamlessly
- 💾 **SQLite Integration** – Stores all data in a local SQLite database
- ✅ **Automated Testing** – Comprehensive JUnit test coverage

---

## **🛠 Technologies Used**
- **Java** – Core programming language
- **SQLite** – Lightweight file-based database
- **JUnit 5** – Testing framework

---

## **📦 Setup & Installation (Using IntelliJ IDEA)**

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/raf-ent/cinema-ticketing
cd cinema-ticketing
```

### **2️⃣ Add Library Dependencies**
- Open IntelliJ IDEA and go to **File → Project Structure** (or press `Ctrl+Alt+Shift+S` on Windows/Linux, or `Cmd+;` on macOS).
- Select **Modules** on the left, then go to the **Dependencies** tab.
- Click the **+** button and choose **JARs or Directories...**.
- Navigate to the `lib/` folder in your project and select all the jar files.
- Click **OK** and **Apply** the changes.

### **3️⃣ Build the Project**
IntelliJ IDEA should automatically compile the project. If not, manually trigger a build from the top menu.


---

## **▶ Running the Application**
### **1️⃣ Start the Application**
- In the **Project Explorer**, locate `Main.java` under the `src/` folder.
- Right-click on `Main.java` and select **Run 'Main.main()'**.
- The console will open; follow the on-screen instructions to log in or create a new account.

### **2️⃣ Run JUnit Tests**
- In the **Project Explorer**, locate `AllTests.java` under the `src/` folder.
- Right-click on `AllTests.java` and select **Run 'AllTests'**.
- The **Run** window will display test results.

---

## **💾 Database Initialization**
- The application automatically creates the SQLite database file (`cinema-system.db`) and initializes the main tables (`movies`, `visitors`, and `admins`) if they do not already exist.
- Dynamic tables (such as **"Movie <ID> - Shows"** or **"User <ID> - Bookings"**) are created on demand when movies or visitors are added.

