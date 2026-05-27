import java.util.Scanner;

public class Admin {
    private DatabaseManager dbManager;
    
    private static final int MAX_ATTEMPTS = 3;

    public Admin(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

   // Method to login user 
    public boolean login(Scanner scanner) {
        for (int attempts = 0; attempts < MAX_ATTEMPTS; attempts++) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (dbManager.authenticateAdmin(username, password)) {
                System.out.println("Login successful! Welcome, " + username + "!");
                return true;
            }
            
            // Calculates remaining attempts of the user and display message
            int attemp = MAX_ATTEMPTS - (attempts + 1);
            if (attemp > 0) {
                System.out.println("Invalid credentials. Attempts remaining: " + attemp);
            } else {
                System.out.println("Too many failed attempts. Access locked.");
            }
        }
        return false;
    }
}
