import java.util.Scanner;

public class UpdateManager {
    private DatabaseManager dbManager;

    public UpdateManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // UPDATE STATUS OF A REPORT
    public void updateStatus(Scanner scanner) {
        System.out.print("\nEnter StreetLight ID to update: ");
        String keyword = scanner.nextLine().trim();

        if (!dbManager.reportExists(keyword)) {
            System.out.println("No report found with ID: " + keyword);
            return;
        }

        System.out.println("\nSelect new status:");
        System.out.println("  1. Pending");
        System.out.println("  2. Ongoing");
        System.out.println("  3. Repaired");
        System.out.print("Enter Choice: ");

        String input = scanner.nextLine().trim();
        int choice;

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1-3).");
            return;
        }

        String status;
        switch (choice) {
            case 1: status = "Pending";  break;
            case 2: status = "Ongoing";  break;
            case 3: status = "Repaired"; break;
            default:
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                return;
        }

        if (dbManager.updateStatus(keyword, status)) {
            System.out.println("Status of [" + keyword + "] updated to: " + status);
        } else {
            System.out.println(" Failed to update status. Please try again.");
        }
    }
}