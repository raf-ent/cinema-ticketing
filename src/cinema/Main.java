package cinema;

import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static Database database;

    public static void main(String[] args) {

        database = new Database();
        System.out.println("Welcome to Movies Ticket Booking System");
        System.out.println("1. Login");
        System.out.println("2. Create new account");
        scanner = new Scanner(System.in);
        int i = scanner.nextInt();
        switch (i) {
            case 1:
                login();
                break;
            case 2:
                createNewAccount();
                break;
            default:
                System.out.println("Unvalid input!");
                break;
        }

    }

    private static void login() {
        System.out.println("Enter your email: ");
        String email = scanner.next();
        System.out.println("Enter your password: ");
        String password = scanner.next();
        if (UsersDatabase.login(email, password, database)) {
            User user = UsersDatabase.getUser(email, password, database);
            System.out.println("\nWelcome "+user.getFirstName()+" "+user.getLastName());
            user.showList(database);
        } else {
            System.out.println("Incorrect email or password");
            login();
        }
    }

    private static void createNewAccount() {
        System.out.println("Enter your first name: ");
        String firstName = scanner.next();
        System.out.println("Enter your last name: ");
        String lastName = scanner.next();
        System.out.println("Enter your email: ");
        String email = scanner.next();
        System.out.println("Enter your phone number: ");
        String phoneNumber = scanner.next();
        System.out.println("Enter your password: ");
        String password = scanner.next();
        System.out.println("Confirm password: ");
        String confirmPassword = scanner.next();
        while (!password.equals(confirmPassword)) {
            System.out.println("Password doesn't match!");
            System.out.println("Enter your password: ");
            password = scanner.next();
            System.out.println("Confirm password: ");
            confirmPassword = scanner.next();
        }
        while (UsersDatabase.isEmailUsed(email, database)) {
            System.out.println("This email is already used!");
            System.out.println("Enter your email: ");
            email = scanner.next();
        }
        Visitor visitor = new Visitor();
        visitor.setID(UsersDatabase.getNextVisitorID(database));
        visitor.setFirstName(firstName);
        visitor.setLastName(lastName);
        visitor.setEmail(email);
        visitor.setPhoneNumber(phoneNumber);
        visitor.setPassword(password);
        UsersDatabase.addVisitor(visitor, database);
        visitor.showList(database);
    }

}
