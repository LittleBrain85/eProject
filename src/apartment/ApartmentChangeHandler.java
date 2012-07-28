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
public class ApartmentChangeHandler {

    DBHandler db;

    public ApartmentChangeHandler() {
        db = new DBHandler();
    }

    public boolean checkApartmentChangeNumberExist(ApartmentChange apartment) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT apartmentNumber FROM ApartmentChange WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getApartmentNumber());
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean newRecord(ApartmentChange apartment) throws ClassNotFoundException, SQLException {
        String tsql = "INSERT INTO ApartmentChange VALUES(?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getApartmentNumber());
        ps.setDate(2, apartment.getChangeDate());
        ps.setString(3, apartment.getNewApartmentNumber());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean updateRecord(ApartmentChange apartment) throws ClassNotFoundException, SQLException {
        String tsql = "UPDATE ApartmentChange SET changeDate = ?, newApartmentNumber = ? WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setDate(1, apartment.getChangeDate());
        ps.setString(2, apartment.getNewApartmentNumber());
        ps.setString(3, apartment.getApartmentNumber());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteRecord(ApartmentChange apartment) throws ClassNotFoundException, SQLException {
        String tsql = "DELETE ApartmentChange WHERE apartmentNumber = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, apartment.getApartmentNumber());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public java.sql.ResultSet getAllResultSetApartmentChange() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM ApartmentChange";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }

    public java.sql.ResultSet getResultSetApartmentChangeByLandlordID(String landlordID) throws SQLException, ClassNotFoundException {
        String tsql = "SELECT apartmentNumber, changeDate, newApartmentNumber "
                + "FROM ApartmentChange "
                + "WHERE apartmentNumber IN "
                + "( SELECT B.apartmentNumber "
                + "FROM Building AS A JOIN Apartment AS B "
                + "ON A.buildingName = B.buildingName "
                + "WHERE A.landlordID = ? ) ";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, landlordID);
        java.sql.ResultSet rs = ps.executeQuery();
        return rs;
    }
}
