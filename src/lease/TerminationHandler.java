/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

import database.DBHandler;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Thaichau
 */
public class TerminationHandler {

    DBHandler db;

    public TerminationHandler() {
        db = new DBHandler();
    }

    public boolean inserTermination(Termination termination) throws ClassNotFoundException, SQLException {
        String tsql = "INSERT INTO Termination VALUES(?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, termination.getTerminationID());
        ps.setDate(2, termination.getLeavingDate());
        ps.setString(3, termination.getLeavingReason());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }
    
    public boolean deleteTermination(Termination termination) throws ClassNotFoundException, SQLException{
        String tsql = "DELETE Termination WHERE terminationID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, termination.getTerminationID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }

    public boolean updateTermination(Termination termination) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE Termination SET leavingDate = ?, leavingReason = ? WHERE terminationID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);        
        ps.setDate(1, termination.getLeavingDate());
        ps.setString(2, termination.getLeavingReason());
        ps.setString(3, termination.getTerminationID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public Vector getAllTerminationID() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT terminationID FROM Termination";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector v = new Vector();
        while(rs.next()){
            v.add(rs.getString(1));
        }
        return v;
    }

    public Vector getAllTermination() throws SQLException, ClassNotFoundException {
        String tsql = "SELECT * FROM Termination";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector v = db.getDataVector(rs);
        return v;
    }
    
    public java.sql.ResultSet getResultsetTermination() throws ClassNotFoundException, SQLException{
        String tsql = "SELECT * FROM Termination";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }
}
