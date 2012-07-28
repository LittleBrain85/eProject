/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import database.DBHandler;
import java.sql.SQLException;
import java.util.Vector;
import userProfiles.Landlord;

/**
 *
 * @author Thaichau
 */
public class ApartmentHandler {

    DBHandler db;
    BuildingHandler bh;

    public ApartmentHandler() {
        db = new DBHandler();
        bh = new BuildingHandler();
    }

    public boolean checkApartmentNumberExist(Apartment apartment) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT apartmentNumber FROM Apartment WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getAptNumber());
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public java.sql.ResultSet getApartmentResultset() throws ClassNotFoundException, SQLException {
        java.sql.Statement st = db.DBConnect().createStatement();
        return st.executeQuery("SELECT * FROM Apartment");
    }

    public Vector getApartmentColumName() throws SQLException, ClassNotFoundException {
        return db.getColumnName(this.getApartmentResultset());
    }

    public Apartment getApartmentByNumber(String number) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM Apartment WHERE apartmentNumber = ?";
        //prepare statement 
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql, java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        //bind param
        ps.setString(1, number);
        //get result set 
        java.sql.ResultSet rs = ps.executeQuery();
        //navigate to first object 
        rs.first();
        //creat new apartment instance
        Apartment apartment = new Apartment();
        apartment.setAptNumber(rs.getString(1));
        apartment.setSize(rs.getDouble(2));
        apartment.setAptType(rs.getString(3));
        apartment.setPrice(rs.getDouble(4));
        //We make building instance to store building name
        Building building = new Building();
        building.setBuildingName(rs.getString(5));
        apartment.setBuilding(building);
        return apartment;
    }

    public Vector getAllApartmetnByBuildingName(Building name) throws ClassNotFoundException, SQLException {
        return bh.getVectorApartmentByBuildingName(name);
    }

    public java.sql.ResultSet getApartmentIncludeAddress() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT B.apartmentNumber, B.aptType,  B.rentalFee, B.size, A.address, B.buildingName "
                + "FROM dbo.Building AS A JOIN dbo.Apartment AS B "
                + "ON A.buildingName = B.buildingName";
        java.sql.Statement st = db.DBConnect().createStatement();
        return st.executeQuery(tsql);
    }

    public Vector getApartmentNumberByLandlordID(String landlordID) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT B.apartmentNumber "
                + "FROM dbo.Building AS A JOIN dbo.Apartment AS B "
                + "ON A.buildingName = B.buildingName WHERE A.landlordID = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlordID);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector aptNum = new Vector();
        while (rs.next()) {
            aptNum.add(rs.getString(1));
        }
        return aptNum;
    }

    public java.sql.ResultSet getAllApartmentResultSet() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM Apartment";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }

    public java.sql.ResultSet getApartmentNotRent() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM dbo.Apartment WHERE apartmentNumber NOT IN (SELECT apartmentNumber FROM dbo.Lease)";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }
    
    public int countApartmentNotRentByLandlordID(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT COUNT(apartmentNumber) AS ApartmentNotRent FROM dbo.Apartment A, dbo.Building B "
                + "WHERE A.buildingName = B.buildingName AND B.landlordID = ? " 
                + "AND apartmentNumber NOT IN (SELECT apartmentNumber FROM dbo.Lease)";
        java.sql.Statement st = db.DBConnect().createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,java.sql.ResultSet.CONCUR_UPDATABLE);
        java.sql.ResultSet rs = st.executeQuery(tsql);
        rs.next();
        int numApartmentByLandlordID = rs.getInt(1);
        return numApartmentByLandlordID;
    }

    public int countApartmentRentByLandlordID(Landlord landlord) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT COUNT(apartmentNumber) AS ApartmentRent FROM dbo.Lease " 
                    + "WHERE apartmentNumber IN( "
                + "SELECT apartmentNumber FROM dbo.Apartment A, dbo.Building B "
                + "WHERE A.buildingName = B.buildingName AND B.landlordID = ?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql,java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,java.sql.ResultSet.CONCUR_UPDATABLE);
        ps.setString(1, landlord.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        rs.next();
        int numApartmentRent = rs.getInt(1);
        return numApartmentRent;
    }
    
    //Count  all apartment
    public int countApartment() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT COUNT(*) AS NumberApartment FROM Apartment";
        java.sql.Statement st = db.DBConnect().createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        java.sql.ResultSet rs = st.executeQuery(tsql);
        rs.next();
        int numApartment = rs.getInt(1);
        return numApartment;
    }

    //Count apartment by landlordID
    public int countApartmetnByLandlordID(Landlord landlord) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT COUNT(*) AS numberOfApartment FROM dbo.Apartment "
                + "WHERE buildingName "
                + "IN (SELECT buildingName FROM dbo.Building WHERE landlordID = 'thaichau')";
        java.sql.Statement st = db.DBConnect().createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        java.sql.ResultSet rs = st.executeQuery(tsql);
        rs.next();
        int numApartment = rs.getInt(1);
        return numApartment;
    }

    public int countBedroom(String aptType) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT COUNT(aptType) AS NumberApartment FROM Apartment WHERE aptType LIKE " + db.quote("%" + aptType + "%");
        java.sql.Statement st = db.DBConnect().createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
        java.sql.ResultSet rs = st.executeQuery(tsql);
        rs.next();
        int apt = rs.getInt(1);
        return apt;
    }


    public Building getMyBuilding(Apartment apartment) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT buildingName FROM dbo.Apartment WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getAptNumber());
        Building building = new Building();
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            building.setBuildingName(rs.getString(1));
        }
        return building;
    }
}