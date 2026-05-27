import java.util.Scanner;

public class UpdateManager {
    private DatabaseManager dbManager;
    
    public UpdateManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Method to update the status of the report
    public void updateStatus(Scanner scanner) {
        System.out.print("Enter StreetLight ID to update: ");
        String keyword = scanner.nextLine();        

        System.out.println("Select new status:");
        System.out.println("1. Pending");
        System.out.println("2. Ongoing");
        System.out.println("3. Repaired");
        System.out.print("Enter Choice: ");
        int choice = Integer.parseInt(scanner.nextLine());
        String status;
        switch (choice) {
            case 1: 
                status = "Pending"; 
                break;
            case 2: 
                status = "Ongoing"; 
                break;
            case 3: 
                status = "Repaired"; 
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (dbManager.updateStatus(keyword, status)) {
            System.out.println("Status updated successfully.");
            System.out.println("Status updated to: " + status);
        } else {
            System.out.println("Failed to update status. Check StreetLight ID.");
        }
    }
}
