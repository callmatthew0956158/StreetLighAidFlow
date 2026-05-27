import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DatabaseManager dbManager = new DatabaseManager();
        LoginManager loginManager = new LoginManager(dbManager);
        UpdateManager updateManager = new UpdateManager(dbManager);
        Scanner scanner = new Scanner(System.in);

        loginManager.startLogin(scanner);   
        while(true) {
            System.out.println();
            System.out.println("=".repeat(70));
            System.out.println("=======--- BARANGAY STREETLIGHTS MONITORING SYSTEM MAIN MENU ---======");
            System.out.println("=".repeat(70));
            System.out.println("1. Add StreetLights Report");
            System.out.println("2. View Streetlight Reports");
            System.out.println("3. Search Streetlight Reports");
            System.out.println("4. Update Maintenance Status");
            System.out.println("5. Generate Maintenance Report");
            System.out.println("6. Delete Report");
            System.out.println("7. Exit");
            System.out.print("Enter Choice (1-7): ");
            String choice = scanner.nextLine();
            
                switch (choice) {
                    case "1":
                        System.out.print("Enter StreetLight ID(Example: SL1000): ");
                        String std = scanner.nextLine();

                        System.out.print("Enter Location: ");
                        String location = scanner.nextLine();

                        System.out.print("Enter Reported By: ");
                        String reportedBy = scanner.nextLine();

                        System.out.print("Enter Problem Description: ");
                        String problem = scanner.nextLine();

                        if (dbManager.addReport(std, location, reportedBy, problem)) {
                            System.out.println("Report added successfully.");
                        } else {
                            System.out.println("Failed to add report.");
                        }
                        break;

                    case "2":
                        System.out.println("====--- VIEW REPORTS ---====");
                        dbManager.viewReports();
                        break;

                    case "3":
                        System.out.print("Enter StreetLight ID to search: ");
                        String keyword = scanner.nextLine();
                        dbManager.searchReport(keyword);
                        break;

                    case "4":
                        System.out.println("====--- CURRENT MAINTENANCE STATUS ---====");
                        dbManager.viewReports();
                        updateManager.updateStatus(scanner);
                        break;

                    case "5":
                        dbManager.generateReport();
                        break;

                    case "6":
                        System.out.println("====--- CURRENT MAINTENANCE STATUS ---====");
                        dbManager.viewReports();
                        System.out.print("\nEnter StreetLight ID to delete: ");
                        String idToDelete = scanner.nextLine();
                        dbManager.deleteReport(idToDelete);
                        break;

                    case "7":
                        System.out.println("Exiting the system....");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
        }   
    }   
}