package cinema;

import java.util.Scanner;

public class Admin extends User {

    private Scanner scanner;

    public Admin() {
        super();
        scanner = new Scanner(System.in);
    }

    @Override
    public void showList(Database database) {
        System.out.println("1. Add New Movie");
        System.out.println("2. Show Movies");
        System.out.println("3. Update Movie");
        System.out.println("4. delete Movie");
        System.out.println("5. Add Show Time");
        System.out.println("6. Show Show Times");
        System.out.println("7. Update Show Time");
        System.out.println("8. Delete Show Time");
        System.out.println("9. Add New Admin");
        System.out.println("10. Show show bookings");
        System.out.println("11. Quit");
        int i = scanner.nextInt();
        switch(i) {
            case 1:
                MoviesDatabase.addNewMovie(database, scanner);
                showList(database);
                break;
            case 2:
                MoviesDatabase.showMovies(database);
                showList(database);
                break;
            case 3:
                MoviesDatabase.updateMovie(database, scanner);
                showList(database);
                break;
            case 4:
                MoviesDatabase.deleteMovie(database, scanner);
                showList(database);
                break;
            case 5:
                MoviesDatabase.addShowTime(database, scanner);
                showList(database);
                break;
            case 6:
                MoviesDatabase.showShowTimes(database, scanner);
                showList(database);
                break;
            case 7:
                MoviesDatabase.editShowTime(database, scanner);
                showList(database);
                break;
            case 8:
                MoviesDatabase.deleteShow(database, scanner);
                showList(database);
                break;
            case 9:
                createNewAccount(database);
                break;
            case 10:
                BookingsDatabase.showMovieBookings(database, scanner);
                showList(database);
                break;
            case 11:
                System.out.println("Thanks for visiting us!");
                scanner.close();
                break;
        }
    }

    private void createNewAccount(Database database) {
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
        Admin admin = new Admin();
        admin.setID(UsersDatabase.getNextAdminID(database));
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);
        admin.setPassword(password);
        UsersDatabase.addAdmin(admin, database);
        showList(database);
    }

}
