import java.sql.ResultSet;
import java.sql.SQLException;

public class StreetlightReport {
    private String streetlight_id;
    private String location;
    private String reported_by;
    private String date_reported;
    private String problem;
    private String status;
    private String assigned_worker;
    private double repair_cost;

    public StreetlightReport(String streetlight_id, String location, String reported_by,
                              String date_reported, String problem, String status,
                              String assigned_worker, double repair_cost) {
        this.streetlight_id  = streetlight_id;
        this.location        = location;
        this.reported_by     = reported_by;
        this.date_reported   = date_reported;
        this.problem         = problem;
        this.status          = status;
        this.assigned_worker = assigned_worker;
        this.repair_cost     = repair_cost;
    }

    public static StreetlightReport fromResultSet(ResultSet rs) throws SQLException {
        return new StreetlightReport(
                rs.getString("streetlight_id"),
                rs.getString("location"),
                rs.getString("reported_by"),
                rs.getString("date_reported"),
                rs.getString("problem"),
                rs.getString("status"),
                rs.getString("assigned_worker"),
                rs.getDouble("repair_cost")
        );
    }

  
    // GETTERS
    
    public String getStreetlight_id() { 
        return streetlight_id;
    }
    public String getLocation() { 
        return location; 
    }
    public String getReported_by(){ 
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
    public String getAssigned_worker() { 
        return assigned_worker; 
    }
    public double getRepair_cost() { 
        return repair_cost; 
    }
}