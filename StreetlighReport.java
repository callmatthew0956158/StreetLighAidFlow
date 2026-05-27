import java.sql.ResultSet;
import java.sql.SQLException;

public class StreetlighReport {
    private String streetlight_id;
    private String location;    
    private String reported_by;
    private String date_reported;
    private String problem;
    private String status;

    public StreetlighReport(String streetlight_id, String location, String reported_by, String date_reported, String problem, String status) {
        this.streetlight_id = streetlight_id;
        this.location = location;
        this.reported_by = reported_by;
        this.date_reported = date_reported;
        this.problem = problem;
        this.status = status;
    }

    // Factory to create from a JDBC ResultSet row
    public static StreetlighReport fromResultSet(ResultSet rs) throws SQLException {
        return new StreetlighReport(
                rs.getString("streetlight_id"),
                rs.getString("location"),
                rs.getString("reported_by"),
                rs.getString("date_reported"),
                rs.getString("problem"),
                rs.getString("status")
        );
    }

    public String getStreetlight_id() {
        return streetlight_id;
    }

    public String getLocation() {
        return location;
    }

    public String getReported_by() {
        return reported_by;
    }

    public String getDate_reported() {
        return date_reported;
    }

    public String getProblem() {
        return problem;
    }

    public String getStatus() {
        return status;
    }

}