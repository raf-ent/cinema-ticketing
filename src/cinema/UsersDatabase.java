package cinema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersDatabase {

    public static boolean isEmailUsed(String email, Database database) {
        boolean isUsed = false;
        try {
            ResultSet rs = database.getStatement().executeQuery("SELECT `ID`, "
                    + "`firstName`, `lastName`, `email`, `phoneNumber`, `password` "
                    + "FROM `visitors` WHERE `email` = '"+email+"';");
            isUsed = rs.next();

            if (!isUsed) {
                ResultSet rs2 = database.getStatement().executeQuery("SELECT `ID`, "
                        + "`firstName`, `lastName`, `email`, `phoneNumber`, `password` "
                        + "FROM `admins` WHERE `email` = '"+email+"';");
                isUsed = rs2.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUsed;
    }

    public static ArrayList<Visitor> getAllVisitors(Database database) {
        ArrayList<Visitor> visitors = new ArrayList<>();
        try {
            ResultSet rs = database.getStatement().executeQuery("SELECT * FROM `visitors`;");
            while (rs.next()) {
                Visitor visitor = new Visitor();
                visitor.setID(rs.getInt("ID"));
                visitor.setFirstName(rs.getString("firstName"));
                visitor.setLastName(rs.getString("lastName"));
                visitor.setEmail(rs.getString("email"));
                visitor.setPhoneNumber(rs.getString("phoneNumber"));
                visitor.setPassword(rs.getString("password"));
                visitors.add(visitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return visitors;
    }

    public static int getNextVisitorID(Database database) {
        int ID = 0;
        ArrayList<Visitor> visitors = getAllVisitors(database);
        if (visitors.size()!=0) {
            int lastRow = visitors.size()-1;
            Visitor lastVisitor = visitors.get(lastRow);
            ID = lastVisitor.getID()+1;
        }
        return ID;
    }

    public static void addVisitor(Visitor v, Database database) {
        String insert = "INSERT INTO `visitors`(`ID`, `firstName`, `lastName`, `email`,"
                + " `phoneNumber`, `password`) VALUES ('"+v.getID()+"','"+
                v.getFirstName()+"','"+v.getLastName()+"','"+v.getEmail()+"','"+
                v.getPhoneNumber()+"','"+v.getPassword()+"');";
        String create = "CREATE TABLE `User "+v.getID()+" - Bookings` "
                + "(ID int, Seats int, MovieID int, ShowID int);";
        try {
            database.getStatement().execute(insert);
            database.getStatement().execute(create);
            System.out.println("User created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String email, String password, Database database) {
        boolean login = false;
        ArrayList<User> users = new ArrayList<>();
        users.addAll(getAllVisitors(database));
        users.addAll(getAllAdmins(database));
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                login = true;
                break;
            }
        }
        return login;
    }

    public static User getUser(String email, String password ,Database database) {
        ArrayList<User> users = new ArrayList<>();
        users.addAll(getAllVisitors(database));
        users.addAll(getAllAdmins(database));
        User user = new Visitor();
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                user = u;
                break;
            }
        }
        return user;
    }

    public static ArrayList<Admin> getAllAdmins(Database database) {
        ArrayList<Admin> admins = new ArrayList<>();
        try {
            ResultSet rs = database.getStatement().executeQuery("SELECT * FROM `admins`;");
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setID(rs.getInt("ID"));
                admin.setFirstName(rs.getString("firstName"));
                admin.setLastName(rs.getString("lastName"));
                admin.setEmail(rs.getString("email"));
                admin.setPhoneNumber(rs.getString("phoneNumber"));
                admin.setPassword(rs.getString("password"));
                admins.add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admins;
    }

    public static int getNextAdminID(Database database) {
        int ID = 0;
        ArrayList<Admin> admins = getAllAdmins(database);
        if (admins.size()!=0) {
            int lastRow = admins.size()-1;
            Admin lastAdmin = admins.get(lastRow);
            ID = lastAdmin.getID()+1;
        }
        return ID;
    }

    public static void addAdmin(Admin v, Database database) {
        String insert = "INSERT INTO `admins`(`ID`, `firstName`, `lastName`, `email`,"
                + " `phoneNumber`, `password`) VALUES ('"+v.getID()+"','"+
                v.getFirstName()+"','"+v.getLastName()+"','"+v.getEmail()+"','"+
                v.getPhoneNumber()+"','"+v.getPassword()+"');";
        try {
            database.getStatement().execute(insert);
            System.out.println("User created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
