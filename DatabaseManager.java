import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final String url = "jdbc:mysql://localhost:3306/reportsdb";
    private final String user = "root";
    private final String password = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

  
    public boolean authenticateAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username=? AND password=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    // ADD REPORT
    
    public boolean addReport(String streetlight_id, String location, String reported_by, String problem) {
        String query = "INSERT INTO report (streetlight_id, location, reported_by, problem, status) VALUES (?, ?, ?, ?, 'Pending')";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, streetlight_id);
            pst.setString(2, location);
            pst.setString(3, reported_by);
            pst.setString(4, problem);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // VIEW ALL REPORTS
    
    public void viewReports() {
        String sql = "SELECT * FROM report";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printHeader();
            boolean found = false;
            while (rs.next()) {
                printRow(rs);
                found = true;
            }
            if (!found) {
                System.out.println("  No reports found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchReport(String keyword) {
        String sql = "SELECT * FROM report WHERE streetlight_id LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            printHeader();
            boolean found = false;
            while (rs.next()) {
                printRow(rs);
                found = true;
            }
            if (!found) {
                System.out.println("  No reports found matching: " + keyword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchReportByDateRange(String dateFrom, String dateTo) {
        String sql = "SELECT * FROM report WHERE DATE(date_reported) BETWEEN ? AND ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dateFrom);
            ps.setString(2, dateTo);
            ResultSet rs = ps.executeQuery();
            printHeader();
            boolean found = false;
            while (rs.next()) {
                printRow(rs);
                found = true;
            }
            if (!found) {
                System.out.println("  No reports found between " + dateFrom + " and " + dateTo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public boolean updateStatus(String id, String status) {
        String sql = "UPDATE report SET status=? WHERE streetlight_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean updateRepairCost(String streetlight_id, double cost) {
        String sql = "UPDATE report SET repair_cost=? WHERE streetlight_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, cost);
            ps.setString(2, streetlight_id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean deleteReport(String streetlight_id) {
        String sql = "DELETE FROM report WHERE streetlight_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, streetlight_id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean assignWorker(String streetlight_id, String workerName, double repairCost) {
        if (!reportExists(streetlight_id)) {
            return false;
        }
        String sql = "UPDATE report SET assigned_worker=?, status='Ongoing', repair_cost=? WHERE streetlight_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, workerName);
            ps.setDouble(2, repairCost);
            ps.setString(3, streetlight_id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewWorkers() {
        String sql = "SELECT * FROM workers ORDER BY worker_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\n--- AVAILABLE WORKERS ---");
            System.out.println("=".repeat(45));
            System.out.printf("%-5s %-20s %-15s%n", "No.", "Worker Name", "Contact");
            System.out.println("=".repeat(45));
            int count = 1;
            boolean found = false;
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s%n",
                        count++,
                        rs.getString("worker_name"),
                        rs.getString("contact"));
                found = true;
            }
            if (!found) {
                System.out.println("  No workers registered yet.");
            }
            System.out.println("=".repeat(45));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    public boolean addWorker(String workerName, String contact) {
        String sql = "INSERT INTO workers (worker_name, contact) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, workerName);
            ps.setString(2, contact);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public List<String> getWorkerNames() {
        List<String> workers = new ArrayList<>();
        String sql = "SELECT worker_name FROM workers ORDER BY worker_name";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                workers.add(rs.getString("worker_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }

   
   
    public boolean reportExists(String streetlight_id) {
        String sql = "SELECT 1 FROM report WHERE streetlight_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, streetlight_id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public void generateReport() {
        String sql = "SELECT status, COUNT(*) total, SUM(repair_cost) total_cost FROM report GROUP BY status";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║          MAINTENANCE SUMMARY              ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.printf("%-15s %-10s %-15s%n", "Status", "Total", "Total Cost");
            System.out.println("-".repeat(42));

            int grandTotal   = 0;
            double grandCost = 0;

            while (rs.next()) {
                int total     = rs.getInt("total");
                double cost   = rs.getDouble("total_cost");
                grandTotal   += total;
                grandCost    += cost;
                System.out.printf("%-15s %-10d PHP %-15.2f%n",
                        rs.getString("status"), total, cost);
            }

            System.out.println("-".repeat(42));
            System.out.printf("%-15s %-10d PHP %-15.2f%n", "GRAND TOTAL", grandTotal, grandCost);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateReportByDateRange(String dateFrom, String dateTo) {
        String sql = "SELECT status, COUNT(*) total, SUM(repair_cost) total_cost FROM report " +
                     "WHERE DATE(date_reported) BETWEEN ? AND ? GROUP BY status";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dateFrom);
            ps.setString(2, dateTo);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║      MAINTENANCE SUMMARY (DATE RANGE)        ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("  From : " + dateFrom);
            System.out.println("  To   : " + dateTo);
            System.out.println("-".repeat(45));
            System.out.printf("%-15s %-10s %-15s%n", "Status", "Total", "Total Cost");
            System.out.println("-".repeat(45));

            int grandTotal   = 0;
            double grandCost = 0;
            boolean found    = false;

            while (rs.next()) {
                int total     = rs.getInt("total");
                double cost   = rs.getDouble("total_cost");
                grandTotal   += total;
                grandCost    += cost;
                System.out.printf("%-15s %-10d PHP %-15.2f%n",
                        rs.getString("status"), total, cost);
                found = true;
            }

            if (!found) {
                System.out.println("  No reports found for this date range.");
            } else {
                System.out.println("-".repeat(45));
                System.out.printf("%-15s %-10d PHP %-15.2f%n", "GRAND TOTAL", grandTotal, grandCost);
                generateProblemBreakdown(dateFrom, dateTo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateProblemBreakdown(String dateFrom, String dateTo) {
        String sql = "SELECT problem, COUNT(*) total, SUM(repair_cost) total_cost FROM report " +
                     "WHERE DATE(date_reported) BETWEEN ? AND ? GROUP BY problem ORDER BY total_cost DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dateFrom);
            ps.setString(2, dateTo);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n  --- Problem Breakdown ---");
            System.out.printf("  %-28s %-10s %-15s%n", "Problem", "Count", "Total Cost");
            System.out.println("  " + "-".repeat(55));

            while (rs.next()) {
                System.out.printf("  %-28s %-10d PHP %-15.2f%n",
                        rs.getString("problem"),
                        rs.getInt("total"),
                        rs.getDouble("total_cost"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // PRINT TABLE HEADER
    private void printHeader() {
        System.out.println("\n--- STREETLIGHT REPORTS ---");
        System.out.println("=".repeat(150));
        System.out.printf("%-10s  %-16s  %-28s  %-12s  %-18s  %-18s  %-22s  %-12s%n",
                "ID", "Location", "Problem", "Status",
                "Reported By", "Assigned To", "Date", "Cost (PHP)");
        System.out.println("=".repeat(150));
    }

   
    // PRINT TABLE ROW
    private void printRow(ResultSet rs) throws SQLException {
        String worker = rs.getString("assigned_worker");
        System.out.printf("%-10s  %-16s  %-28s  %-12s  %-18s  %-18s  %-22s  %-12.2f%n",
                rs.getString("streetlight_id"),
                rs.getString("location"),
                rs.getString("problem"),
                rs.getString("status"),
                rs.getString("reported_by"),
                (worker != null ? worker : "Unassigned"),
                rs.getTimestamp("date_reported"),
                rs.getDouble("repair_cost"));
    }
}