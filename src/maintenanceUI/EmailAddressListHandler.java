/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenanceUI;

import database.DBHandler;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Thaichau
 */
public class EmailAddressListHandler {

    DBHandler db;
    public EmailAddressListHandler() {
        db = new DBHandler();
    }
    
    public Vector getEmailAdressList() throws ClassNotFoundException, SQLException{
        String emailListquery = "SELECT email FROM Tenant";
        java.sql.Statement st = db.DBConnect().createStatement();
        java.sql.ResultSet rs = st.executeQuery(emailListquery);
        java.util.Vector v = new java.util.Vector();
        while(rs.next()){
            v.add(rs.getString(1));
        }
        return v;
    }
}
