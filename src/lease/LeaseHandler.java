/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

import apartment.Apartment;
import database.DBHandler;
import java.sql.SQLException;
import userProfiles.Tenant;

/**
 *
 * @author Thaichau
 */
public class LeaseHandler {
    DBHandler db;
    public LeaseHandler() {
        db = new DBHandler();
    }
    
    public boolean insertLease(Lease lease) throws ClassNotFoundException, SQLException{
        String tsql = "INSERT INTO Lease VALUES(?,?,?,?,?,?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, lease.getLeaseID());
        ps.setDate(2, utils.ConvertDate.convertToSqlDate(lease.getStartDate()));
        ps.setDate(3, utils.ConvertDate.convertToSqlDate(lease.getEndDate()));
        ps.setDouble(4, lease.getBalance());
        ps.setDouble(5, lease.getSecurityDeposit());
        ps.setDate(6, utils.ConvertDate.convertToSqlDate(lease.getRentalDate()));
        ps.setString(7, lease.getTenant().getID());
        ps.setString(8, lease.getApartment().getAptNumber());
        ps.setString(9, lease.getTermination().getTerminationID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean updateLeaseApartmentNumber(Lease lease) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE Lease SET apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, lease.getApartment().getAptNumber());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public String getApartmentNumberByTenantID(String tenantID) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT apartmentNumber FROM Lease WHERE tenantID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, tenantID);
        java.sql.ResultSet rs = ps.executeQuery();
        Apartment apartment = new Apartment();
        while(rs.next()){
            apartment.setAptNumber(rs.getString(1));
        }
        return apartment.getAptNumber();
    }
}
