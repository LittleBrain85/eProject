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
public class MaintenanceHandler {
    DBHandler db;

    public MaintenanceHandler() {
        db = new DBHandler();
    }
    
    public boolean insertMaintenance(Maintenance maintenance) throws ClassNotFoundException, SQLException{
        String tsql = "INSERT INTO Maintenance VALUES(?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, maintenance.getApartment().getAptNumber());
        ps.setString(2, maintenance.getMaintenanceID());
        ps.setDate(3, maintenance.getMaintenanceDate());
        ps.setString(4, maintenance.getTemplate().getTemplateName());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean deleteMaintenance(Maintenance maintenance) throws ClassNotFoundException, SQLException{
        String tsql = "DELETE Maintenance WHERE apartmentNumber = ? AND maintenanceID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, maintenance.getApartment().getAptNumber());
        ps.setString(2, maintenance.getMaintenanceID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public java.sql.ResultSet getResultSetMaintenance() throws ClassNotFoundException, SQLException{
        String tsql = "SELECT A.apartmentNumber, A.maintenanceDate, A.maintenanceID, B.emailAddress, B.subject, B.templateName, B.description "
                + "FROM dbo.Maintenance A, dbo.NotificationTemplate B "
                + "WHERE A.templateName = B.templateName";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }
}
