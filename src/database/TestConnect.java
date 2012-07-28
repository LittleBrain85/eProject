/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thaichau
 */
public class TestConnect {
    public static void main(String[] args) {
        try {
            java.sql.Connection conn;
            DBHandler db = new DBHandler();
            conn = db.DBConnect();
            java.sql.Statement st = conn.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT * FROM Tenant");
            while(rs.next()){
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestConnect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
