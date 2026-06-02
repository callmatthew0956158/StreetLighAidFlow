import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DatabaseManager dbManager = new DatabaseManager();
        LoginManager loginManager = new LoginManager(dbManager);
        UpdateManager updateManager = new UpdateManager(dbManager);
        WorkerManager workerManager = new WorkerManager(dbManager);
        Scanner scanner = new Scanner(System.in);

        if (!loginManager.startLogin(scanner)) {
            System.out.println("Access denied. Exiting system.");
            scanner.close();
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("=".repeat(70));
            System.out.println("=======--- BARANGAY STREETLIGHTS MONITORING SYSTEM ---=======");
            System.out.println("=".repeat(70));
            System.out.println(" 1.  Add Streetlight Report");
            System.out.println(" 2.  View Streetlight Reports");
            System.out.println(" 3.  Search Streetlight Reports");
            System.out.println(" 4.  Update Maintenance Status");
            System.out.println(" 5.  Generate Maintenance Report");
            System.out.println(" 6.  Delete Report");
            System.out.println(" 7.  Assign Worker to Report");
            System.out.println(" 8.  Update Repair Cost");
            System.out.println(" 9.  Manage Workers");
            System.out.println(" 10. Exit");
            System.out.println("=".repeat(70));
            System.out.print("Enter Choice (1-10): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    System.out.println("\n====--- ADD NEW REPORT ---====");

                    System.out.print("Enter StreetLight ID: ");
                    String std = scanner.nextLine().trim();

                    System.out.print("Enter Location: ");
                    String location = scanner.nextLine().trim();

                    System.out.print("Enter Reported By: ");
                    String reportedBy = scanner.nextLine().trim();

                    String problem = selectProblem(scanner);

                    if (dbManager.addReport(std, location, reportedBy, problem)) {
                        System.out.println("Report added successfully.");
                        System.out.println("Cost will be recorded when a worker is assigned.");
                    } else {
                        System.out.println("Failed to add report.");
                    }
                    break;

              
                case "2":
                    System.out.println("\n====--- VIEW ALL REPORTS ---====");
                    dbManager.viewReports();
                    break;

                case "3":
                    System.out.println("\n====--- SEARCH REPORTS ---====");
                    System.out.println("Search by:");
                    System.out.println(" 1. Streetlight ID");
                    System.out.println(" 2. Date Range");
                    System.out.print("Enter choice: ");
                    String searchChoice = scanner.nextLine().trim();

                    if (searchChoice.equals("1")) {
                        System.out.print("Enter StreetLight ID to search: ");
                        String keyword = scanner.nextLine().trim();
                        dbManager.searchReport(keyword);

                    } else if (searchChoice.equals("2")) {
                        System.out.print("Enter Start Date (YYYY-MM-DD): ");
                        String dateFrom = scanner.nextLine().trim();
                        System.out.print("Enter End Date   (YYYY-MM-DD): ");
                        String dateTo = scanner.nextLine().trim();
                        dbManager.searchReportByDateRange(dateFrom, dateTo);

                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case "4":
                    System.out.println("\n====--- UPDATE MAINTENANCE STATUS ---====");
                    dbManager.viewReports();
                    updateManager.updateStatus(scanner);
                    break;

                case "5":
                    System.out.println("\n====--- GENERATE REPORT ---====");
                    System.out.println("Report type:");
                    System.out.println(" 1. Overall Summary");
                    System.out.println(" 2. Summary by Date Range");
                    System.out.print("Enter choice: ");
                    String reportChoice = scanner.nextLine().trim();

                    if (reportChoice.equals("1")) {
                        dbManager.generateReport();

                    } else if (reportChoice.equals("2")) {
                        System.out.print("Enter Start Date (YYYY-MM-DD): ");
                        String dateFrom = scanner.nextLine().trim();
                        System.out.print("Enter End Date   (YYYY-MM-DD): ");
                        String dateTo = scanner.nextLine().trim();
                        dbManager.generateReportByDateRange(dateFrom, dateTo);

                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case "6":
                    System.out.println("\n====--- DELETE REPORT ---====");
                    dbManager.viewReports();
                    System.out.print("\nEnter StreetLight ID to delete: ");
                    String idToDelete = scanner.nextLine().trim();

                    System.out.print("Are you sure you want to delete [" + idToDelete + "]? (yes/no): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();

                    if (confirm.equals("yes") || confirm.equals("y")) {
                        if (dbManager.deleteReport(idToDelete)) {
                            System.out.println(" Report [" + idToDelete + "] deleted successfully.");
                        } else {
                            System.out.println("No report found with ID: " + idToDelete);
                        }
                    } else {
                        System.out.println("Delete cancelled.");
                    }
                    break;

                case "7":
                    System.out.println("\n====--- ASSIGN WORKER ---====");
                    dbManager.viewReports();
                    workerManager.assignWorker(scanner);
                    break;

                case "8":
                    System.out.println("\n====--- UPDATE REPAIR COST ---====");
                    dbManager.viewReports();

                    System.out.print("\nEnter StreetLight ID to update cost: ");
                    String costId = scanner.nextLine().trim();

                    if (!dbManager.reportExists(costId)) {
                        System.out.println(" No report found with ID: " + costId);
                        break;
                    }

                    double newCost = 0;
                    while (true) {
                        System.out.print("Enter New Repair Cost (PHP): ");
                        String costInput = scanner.nextLine().trim();
                        try {
                            newCost = Double.parseDouble(costInput);
                            if (newCost < 0) {
                                System.out.println("Cost cannot be negative. Try again.");
                            } else {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid amount. Enter a number (e.g. 500 or 1500.50).");
                        }
                    }

                    if (dbManager.updateRepairCost(costId, newCost)) {
                        System.out.println("Repair cost of [" + costId + "] updated to: PHP " +
                                String.format("%.2f", newCost));
                    } else {
                        System.out.println("Failed to update cost.");
                    }
                    break;

                case "9":
                    manageWorkers(scanner, dbManager);
                    break;

                case "10":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please enter 1-10.");
            }
        }
    }

    private static String selectProblem(Scanner scanner) {
        String[] problems = {
            "Flickering light",
            "No power / Light is off",
            "Broken light bulb",
            "Broken pole",
            "Damaged wiring",
            "Loose or exposed wires",
            "Light stays on during daytime",
            "Water damage / Flooding",
            "Animal damage",
            "Vandalism / Theft",
            "Others"
        };

        System.out.println("\nSelect Problem Description:");
        System.out.println("-".repeat(35));
        for (int i = 0; i < problems.length; i++) {
            System.out.printf("  %d. %s%n", (i + 1), problems[i]);
        }
        System.out.println("-".repeat(35));

        while (true) {
            System.out.print("Enter choice (1-" + problems.length + "): ");
            String input = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(input);
                if (idx >= 1 && idx < problems.length) {
                    return problems[idx - 1];
                } else if (idx == problems.length) {
                    System.out.print("Enter custom problem description: ");
                    String custom = scanner.nextLine().trim();
                    if (custom.isEmpty()) {
                        System.out.println("Problem description cannot be empty. Try again.");
                    } else {
                        return "Others: " + custom;
                    }
                } else {
                    System.out.println("Invalid choice. Please enter 1-" + problems.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1 and " + problems.length);
            }
        }
    }


    private static void manageWorkers(Scanner scanner, DatabaseManager dbManager) {
        while (true) {
            System.out.println("\n====--- MANAGE WORKERS ---====");
            System.out.println("  1. View All Workers");
            System.out.println("  2. Add New Worker");
            System.out.println("  3. Back to Main Menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    dbManager.viewWorkers();
                    break;
                case "2":
                    System.out.print("Enter Worker Name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter Contact Number: ");
                    String contact = scanner.nextLine().trim();
                    if (dbManager.addWorker(name, contact)) {
                        System.out.println("Worker [" + name + "] added successfully.");
                    } else {
                        System.out.println("Failed to add worker.");
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}