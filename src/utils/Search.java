/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import database.DBHandler;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thaichau
 */
public class Search {

    public static DBHandler db = new DBHandler();
    public static java.sql.ResultSet resultSet = null;
    public static final String TB_BUILDING = "Building";
    public static final String TB_APARTMENT = "Apartment";

    public static ResultSet getResultSet() {
        return resultSet;
    }

    public static void setResultSet(ResultSet resultSet) {
        Search.resultSet = resultSet;
    }

    
    public static java.sql.ResultSet searchFor(String table, String columnName, String param)
            throws ClassNotFoundException, SQLException {
        if (table.equals(Search.TB_BUILDING)) {
            String tsql = "SELECT *"
                    + " FROM " + Search.TB_BUILDING
                    + " WHERE " + columnName + " LIKE " + db.quote("%" + param + "%");
            java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
            java.sql.ResultSet rs = ps.executeQuery();
            setResultSet(rs);
        }
        if (table.equals(Search.TB_APARTMENT)) {
            String tsql = "SELECT *"
                        + " FROM " + Search.TB_APARTMENT 
                        + " WHERE " + columnName + " LIKE " + db.quote("%" + param + "%");
            java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
            java.sql.ResultSet rs = ps.executeQuery();
            setResultSet(rs);
        }
        return resultSet;
    }
}
