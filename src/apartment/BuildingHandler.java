/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import database.DBHandler;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import userProfiles.Landlord;

/**
 *
 * @author Thaichau
 */
public class BuildingHandler {

    DBHandler db;
    java.sql.Connection conn;

    public BuildingHandler() {
        try {
            db = new DBHandler();
            conn = db.DBConnect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BuildingHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BuildingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkBuildingExist(Building building) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT buildingName FROM Building WHERE buildingName = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, building.getBuildingName());
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    /**
     * Add a building </br>
     *
     * @param building
     * @return boolean
     * @throws SQLException
     */
    public boolean addBuilding(Building building) throws SQLException {
        String sql = "INSERT INTO Building VALUES(?,?,?,?)";
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, building.getBuildingName());
        ps.setString(2, building.getAddress());
        ps.setString(3, building.getCitySateZip());
        ps.setString(4, building.getLandlord().getID());//Landlord ID must have set before you add
        int rs = ps.executeUpdate();
        if (rs > 0) {
            return true;
        }
        return false;
    }

    public boolean updateBuilding(Building building) throws ClassNotFoundException, SQLException {
        //Note : landlordID not need update
        String tsql = "UPDATE Building SET address = ?, cityStateZip = ? WHERE buildingName = ?";
        //prepare statment
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        //bind param
        ps.setString(1, building.getAddress());
        ps.setString(2, building.getCitySateZip());
        ps.setString(3, building.getBuildingName());
        int res = ps.executeUpdate();
        if (res > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteBuilding(Building building) throws SQLException, ClassNotFoundException {
        String tsql = "DELETE Building WHERE buildingName = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        //bind param
        ps.setString(1, building.getBuildingName());
        int res = ps.executeUpdate();
        if (res > 0) {
            return true;
        }
        return false;
    }

    public Building getBuildingByLandlordID(String ID) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM Building WHERE landlordID = ?";
        //prepareStatement
        java.sql.PreparedStatement ps = conn.prepareStatement(tsql, java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        //bind param
        ps.setString(1, ID);
        //get Result set
        java.sql.ResultSet rs = ps.executeQuery();
        //navigate to first record
        rs.first();
        //create new Building instance
        Building building = new Building();
        building.setBuildingName(rs.getString(1));
        building.setAddress(rs.getString(2));
        building.setCitySateZip(rs.getString(3));
        return building;
    }

    /**
     * This method return ResultSet for all building by specific landlord
     * @param ID
     * @return java.sql.ResultSet
     * @throws SQLException 
     */
    public java.sql.ResultSet getAllBuildingByLanlordID(String ID) throws SQLException {
        String tsql = "SELECT * FROM Building WHERE landlordID = ?";
        java.sql.PreparedStatement ps = conn.prepareStatement(tsql);
        ps.setString(1, ID);
        return ps.executeQuery();
    }

    /**
     * This method fetch all building name from Building table specified
     * landlordID
     *
     * @param ID
     * @return Vector
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Vector getAllBuildingNameByLandlordID(String ID) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT buildingName FROM Building WHERE landlordID = ?";
        java.sql.PreparedStatement ps = conn.prepareStatement(tsql);
        ps.setString(1, ID);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector v = new Vector();
        while (rs.next()) {
            v.add(rs.getString(1));
        }
        return v;
    }

    /**
     * This method return a vector contain all building by specified landlord
     * @param landlord
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Vector getAllBuildingIdByLandlordID(Landlord landlord) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM Building WHERE landlordID = ?";
        java.sql.PreparedStatement ps = conn.prepareStatement(tsql);
        ps.setString(1, landlord.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        Vector v = new Vector();
        while (rs.next()) {
            v.add(rs.getString(1));
        }
        return v;
    }

    /**
     * This method fetch Apartment by lanlordID return in a Vector
     *
     * @param ID landlordID
     * @return Vector
     * @throws SQLException
     */
    public Vector getVectorApartmentByLandlordID(String ID) throws SQLException {
        String tsql = "SELECT * FROM Apartment WHERE buildingName IN (SELECT buildingName FROM dbo.Building WHERE landlordID = ?)";
        java.sql.PreparedStatement ps = conn.prepareStatement(tsql);
        ps.setString(1, ID);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector v = new Vector();
        while (rs.next()) {
            Vector vList = new Vector();
            vList.add(rs.getString(1));
            vList.add(rs.getDouble(2));
            vList.add(rs.getString(3));
            vList.add(rs.getDouble(4));
            vList.add(rs.getString(5));
            v.add(vList);
        }
        return v;
    }

    /**
     * This method fetch apartment by landlordID return a Result set
     *
     * @param ID
     * @return Result Set Object
     * @throws SQLException
     */
    public java.sql.ResultSet getResultsetApartmentByLandlordID(String ID) throws SQLException {
        String tsql = "SELECT A.apartmentNumber, A.size, A.aptType, A.rentalFee, B.buildingName "
                + "FROM Apartment A JOIN Building B "
                + "ON A.buildingName = B.buildingName "
                + "WHERE B.landlordID = ?";
        java.sql.PreparedStatement ps = conn.prepareStatement(tsql);
        ps.setString(1, ID);
        java.sql.ResultSet rs = ps.executeQuery();
        return rs;
    }

    /**
     * This method fetch apartment by building name return a Vector
     *
     * @param Name
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Vector getVectorApartmentByBuildingName(Building building) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM Apartment WHERE buildingName = ?";
        //prepare statement
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, building.getBuildingName());
        //get Result set
        java.sql.ResultSet rs = ps.executeQuery();
        Vector v = new Vector();
        while (rs.next()) {
            Apartment apartment = new Apartment();
            apartment.setAptNumber(rs.getString(1));
            apartment.setSize(rs.getDouble(2));
            apartment.setAptType(rs.getString(3));
            apartment.setPrice(rs.getDouble(4));
            apartment.setBuilding(building);
            v.add(apartment);
        }
        return v;
    }

    public boolean addApartment(Apartment apartment) throws ClassNotFoundException, SQLException {
        String tsql = "INSERT INTO Apartment VALUES(?,?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getAptNumber().toString());
        ps.setDouble(2, apartment.getSize());
        ps.setString(3, apartment.getAptType());
        ps.setDouble(4, apartment.getPrice());
        ps.setString(5, apartment.getBuilding().getBuildingName());
        int res = ps.executeUpdate();
        if (res > 0) {
            return true;
        }
        return false;
    }
    /**
     * This method count all building
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public int countBuilding() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT COUNT(*) AS NumberOfBuilding FROM dbo.Building";
        java.sql.Statement st = db.DBConnect().createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        java.sql.ResultSet rs = st.executeQuery(tsql);
        rs.next();
        int numBuilding = rs.getInt(1);
        return numBuilding;
    }

    /**
     * This method count building by landlord ID
     * @param landlord
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public int countBuildingByLandlordID(Landlord landlord) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT COUNT(*) AS NumberOfBuilding FROM Building WHERE landlordID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql,java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,java.sql.ResultSet.CONCUR_UPDATABLE);
        ps.setString(1, landlord.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        rs.next();
        int numBuilding = rs.getInt(1);
        return numBuilding;
    }

    //Building add apartment
    public boolean removeApartment(Apartment apartment) throws ClassNotFoundException, SQLException {
        String tsql = "DELETE Apartment WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getAptNumber());
        int res = ps.executeUpdate();
        if (res > 0) {
            return true;
        }
        return false;
    }

    public boolean updateApartment(Apartment apartment) throws ClassNotFoundException, SQLException {
        String tsql = "UPDATE Apartment SET size = ?, aptType = ?, rentalFee = ?, buildingName = ? WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setDouble(1, apartment.getSize());
        ps.setString(2, apartment.getAptType());
        ps.setDouble(3, apartment.getPrice());
        ps.setString(4, apartment.getBuilding().getBuildingName());
        ps.setString(5, apartment.getAptNumber());
        int res = ps.executeUpdate();
        if (res > 0) {
            return true;
        }
        return false;
    }
}
