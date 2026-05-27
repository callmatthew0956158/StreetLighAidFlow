import java.sql.*;

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

    // LOGIN
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

    // VIEW REPORTS
    public void viewReports() {
        String sql = "SELECT * FROM report";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printHeader();

            while (rs.next()) {
                printRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    public void searchReport(String keyword) {
        String sql = "SELECT * FROM report WHERE streetlight_id LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            printHeader();

            while (rs.next()) {
                printRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE STATUS
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

    // DELETE REPORT
    public boolean deleteReport(String streetlight_id) {
        String sql = "DELETE FROM report WHERE streetlight_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, streetlight_id);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SUMMARY
    public void generateReport() {
        String sql = "SELECT status, COUNT(*) total FROM report GROUP BY status";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- MAINTENANCE SUMMARY ---");
            System.out.printf("%-15s %-10s%n", "Status", "Total");

            while (rs.next()) {
                System.out.printf("%-15s %-10d%n",
                        rs.getString("status"),
                        rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // HEADER
    private void printHeader() {
        System.out.println("\n--- STREETLIGHT REPORTS ---");
        System.out.println("=".repeat(90));

        System.out.printf("%-10s %-15s %-20s %-15s %-20s %-20s%n",
                "ID", "Location", "Problem", "Status", "Reported By", "Date");

        System.out.println("=".repeat(90));
    }

    // ROW
    private void printRow(ResultSet rs) throws SQLException {
        System.out.printf("%-10s %-15s %-20s %-15s %-20s %-20s%n",
                rs.getString("streetlight_id"),
                rs.getString("location"),
                rs.getString("problem"),
                rs.getString("status"),
                rs.getString("reported_by"),
                rs.getTimestamp("date_reported"));
    }
}