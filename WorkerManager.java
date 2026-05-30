import java.util.List;
import java.util.Scanner;

public class WorkerManager {
    private DatabaseManager dbManager;

    public WorkerManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

   
    public void assignWorker(Scanner scanner) {

        // Step 1: Ask for streetlight ID
        System.out.print("\nEnter StreetLight ID to assign a worker: ");
        String streetlightId = scanner.nextLine().trim();

        if (!dbManager.reportExists(streetlightId)) {
            System.out.println(" No report found with ID: " + streetlightId);
            return;
        }

      
        List<String> workers = dbManager.getWorkerNames();

        if (workers.isEmpty()) {
            System.out.println(" No workers registered yet.");
            System.out.println("  Please add workers first using Manage Workers (Option 9).");
            return;
        }

        System.out.println("\nAvailable Workers:");
        System.out.println("-".repeat(35));
        for (int i = 0; i < workers.size(); i++) {
            System.out.printf("  %d. %s%n", (i + 1), workers.get(i));
        }
        System.out.println("-".repeat(35));


        String selectedWorker = null;
        while (selectedWorker == null) {
            System.out.print("Select worker (1-" + workers.size() + "): ");
            String input = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(input);
                if (idx >= 1 && idx <= workers.size()) {
                    selectedWorker = workers.get(idx - 1);
                } else {
                    System.out.println("Invalid choice. Enter 1-" + workers.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

  
        double repairCost = 0.00;
        while (true) {
            System.out.print("Enter Repair Cost (PHP): ");
            String costInput = scanner.nextLine().trim();
            try {
                repairCost = Double.parseDouble(costInput);
                if (repairCost < 0) {
                    System.out.println("Cost cannot be negative. Try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Enter a number (e.g. 500 or 1500.50).");
            }
        }

    
        if (dbManager.assignWorker(streetlightId, selectedWorker, repairCost)) {
            System.out.println("\n✔ Worker assigned successfully!");
            System.out.println("  Streetlight ID  : " + streetlightId);
            System.out.println("  Assigned Worker : " + selectedWorker);
            System.out.println("  Repair Cost     : PHP " + String.format("%.2f", repairCost));
            System.out.println("  Status updated  : Ongoing");
        } else {
            System.out.println("✘ Failed to assign worker. Please try again.");
        }
    }
}