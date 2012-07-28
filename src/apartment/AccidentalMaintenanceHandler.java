/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import database.DBHandler;
import java.sql.SQLException;

/**
 *
 * @author Thaichau
 */
public class AccidentalMaintenanceHandler {
    DBHandler db;

    public AccidentalMaintenanceHandler() {
        db = new DBHandler();
    }
    
    public boolean insertAccidentalMaintenance(AccidentalMaintenance am) throws ClassNotFoundException, SQLException{
        String tsql = "INSERT INTO AccidentalMaintenance VALUES(?,?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, am.getApartment().getAptNumber());
        ps.setString(2, am.getMaintenanceID());
        ps.setString(3, am.getProblem());
        ps.setDate(4, am.getFeedbackDate());
        ps.setDouble(5, am.getRating());
        
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean deleteAccidentalMaintenance(AccidentalMaintenance acc) throws ClassNotFoundException, SQLException{
        String tsql = "DELETE AccidentalMaintenance WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, acc.getApartment().getAptNumber());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean updateAccidentalMaintenance(AccidentalMaintenance acc) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE AccidentalMaintenance SET problem = ?, feedbackDate = ?, maintenanceRating = ? WHERE apartmentNumber = ? AND maintenanceID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, acc.getProblem());
        ps.setDate(2, acc.getFeedbackDate());
        ps.setDouble(3, acc.getRating());
        ps.setString(4, acc.getApartment().getAptNumber());
        ps.setString(5, acc.getMaintenanceID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public java.sql.ResultSet getResultSetAccidentalMaintenance() throws ClassNotFoundException, SQLException{
        String tsql = "SELECT * FROM AccidentalMaintenance";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }
}
