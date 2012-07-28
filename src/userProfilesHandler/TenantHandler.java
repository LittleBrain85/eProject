/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfilesHandler;

import apartment.Apartment;
import apartment.Building;
import database.DBHandler;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import userProfiles.Landlord;
import userProfiles.Tenant;

/**
 *
 * @author Thaichau
 */
public class TenantHandler {

    DBHandler db;
    String oldPassword = "";

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public TenantHandler() {
        db = new DBHandler();
    }

    public boolean checkTenantExist(Tenant tenant) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT tenantID FROM Tenant WHERE tenantID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, tenant.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
    
    public Tenant getTenantById(String ID) throws ClassNotFoundException, SQLException {
        String queryString = "SELECT * FROM Tenant WHERE tenantID = ?";
        PreparedStatement ps = db.DBConnect().prepareStatement(queryString, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        //bind param
        ps.setString(1, ID);
        //get Result set
        ResultSet rs = ps.executeQuery();
        //navigatation to first record
        rs.first();
        //store information to Tenant object
        Tenant tenant = new Tenant(rs.getString(1));
        //ommit password field
        tenant.setFirstname(rs.getString(3));
        tenant.setLastname(rs.getString(4));
        tenant.setPhone(rs.getString(5));
        tenant.setEmail(rs.getString(6));
        tenant.setCurrentAddress(rs.getString(7));
        tenant.setCityStateZip(rs.getString(8));
        return tenant;
    }
    
    public ResultSet getAllTennant() throws ClassNotFoundException, SQLException{
        String queryString = "SELECT * FROM Tenant";
        Statement st = db.DBConnect().createStatement();
        ResultSet rs = st.executeQuery(queryString);
        return rs;
    }
    
    public boolean insertTenant(Tenant tenant) throws ClassNotFoundException, SQLException{
        String sql = "INSERT INTO Tenant VALUES(?,?,?,?,?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(sql);
        ps.setString(1,tenant.getID());
        ps.setString(2,tenant.getPassword());
        ps.setString(3,tenant.getFirstname());
        ps.setString(4,tenant.getLastname());
        ps.setString(5,tenant.getPhone());
        ps.setString(6,tenant.getEmail());
        ps.setString(7,tenant.getCurrentAddress());
        ps.setString(8,tenant.getCityStateZip());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean deleteTenant(Tenant tenant) throws ClassNotFoundException, SQLException{
        String tsql = "DELETE Tenant WHERE tenantID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1,tenant.getID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean updateTenant(Tenant tenant) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE Tenant SET firstname = ?, lastname = ?, phone = ?, email = ?, currentAddress = ?, cityStateZip = ? WHERE tenantID =?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        
        ps.setString(1, tenant.getFirstname());
        ps.setString(2, tenant.getLastname());
        ps.setString(3, tenant.getPhone());
        ps.setString(4, tenant.getEmail());
        ps.setString(5, tenant.getCurrentAddress());
        ps.setString(6, tenant.getCityStateZip()); 
        ps.setString(7, tenant.getID());
        int result = ps.executeUpdate();      
        if(result > 0){
            return true;
        }
        return false;
    }
    public boolean changeTenantPassword(Tenant tenant) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE Tenant SET password = ? WHERE TenantID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, tenant.getPassword());
        ps.setString(2, tenant.getID());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean checkPassword(Tenant tenant) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT password FROM Tenant WHERE tenantID = ? AND password = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, tenant.getID());
        ps.setString(2, tenant.getPassword());
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
    
    public Landlord getMyLandlord(Apartment apartment) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT landlordID FROM Building WHERE buildingName = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getBuilding().getBuildingName());
        Landlord landlord = new Landlord();
        java.sql.ResultSet rs = ps.executeQuery();
        if(rs.next()){
            landlord.setID(rs.getString(1));
        }
        return landlord;
    }

}
