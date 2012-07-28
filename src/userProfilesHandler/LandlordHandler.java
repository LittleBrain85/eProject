/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfilesHandler;

import database.DBHandler;
import java.sql.SQLException;
import java.util.Vector;
import userProfiles.Landlord;

/**
 *
 * @author Thaichau
 */
public class LandlordHandler {

    DBHandler db;
    public LandlordHandler() {
        db = new DBHandler();
    }
    
    public Vector getAllVectorLandlord() throws ClassNotFoundException, SQLException{
        String tsql = "SELECT * FROM Landlord";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        Vector vLandlord = new Vector();
        while(rs.next()){
            vLandlord.add(rs.getString(1));
            vLandlord.add(rs.getString(2));
        }
        return vLandlord;
    }
    
    public java.sql.ResultSet getAllResultSetLandlord() throws ClassNotFoundException, SQLException{
        String tsql = "SELECT * FROM Landlord";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }
    
    public boolean checkLandlordExist(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT landlordID FROM Landlord WHERE landlordID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlord.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
    
    public boolean insertLandlord(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "INSERT INTO Landlord VALUES(?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlord.getID());
        ps.setString(2, landlord.getPassword());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean checkLandlordPassword(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT password FROM Landlord WHERE landlordID = ? AND password = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlord.getID());
        ps.setString(2, landlord.getPassword());
        java.sql.ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
    
    public boolean changeLandlordPassword(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE Landlord SET password = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlord.getPassword());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    
    public Landlord getLandlordById(String ID) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT * FROM Landlord WHERE landlordID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql, java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        //bind param
        ps.setString(1, ID);
        //get result set
        java.sql.ResultSet rs = ps.executeQuery();
        //navigate to first record
        rs.first();
        //create instance for landlord
        Landlord landlord = new Landlord(ID);
        landlord.setPassword(rs.getString(2));
        return landlord;
    }
    
    public Vector getTenantEmail() throws ClassNotFoundException, SQLException{
        String tsql = "SELECT email FROM Tenant";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector email = new Vector();
        while(rs.next()){
            email.add(rs.getString(1));
        }
        return email;
    }
    
    public java.sql.ResultSet paymentReport(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT D.tenantID, A.balance, A.rentalDate, A.startDate, A.endDate, B.rentalFee, B.apartmentNumber, B.buildingName " 
                + "FROM dbo.Lease A, dbo.Apartment B, dbo.Building C, dbo.Tenant D " 
                + "WHERE A.apartmentNumber = B.apartmentNumber AND B.buildingName = C.buildingName AND A.tenantId = D.tenantID " 
                + "AND C.landlordID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlord.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        return rs;
    }
}
