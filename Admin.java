import java.util.Scanner;

public class Admin {
    private DatabaseManager dbManager;
    private static final int MAX_ATTEMPTS = 3;

    public Admin(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean login(Scanner scanner) {
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            int remaining = MAX_ATTEMPTS - attempts;

            System.out.println("\n====== ADMIN LOGIN ======");

            if (attempts > 0) {
                System.out.println("Invalid credentials. Attempts remaining: " + remaining);
            }

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (dbManager.authenticateAdmin(username, password)) {
                System.out.println("Access granted. Welcome, " + username + "!");
                return true;
            }

            attempts++;

            if (attempts == MAX_ATTEMPTS) {
                System.out.println("Access denied. Maximum login attempts reached.");
                System.out.println("The system will now exit.");
                scanner.close();
                System.exit(0); // Lock completely
            }
        }

        return false;
    }
}