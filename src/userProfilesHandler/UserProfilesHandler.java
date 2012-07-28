/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfilesHandler;

import database.DBHandler;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thaichau
 */
public class UserProfilesHandler {

    private final String EMPLOYEE_ID = "employeeID";
    private final String TENANT_ID = "tenantID";
    private final String LANDLORD_ID = "landlordID";
    private final String EMPLOYEE_PWD = "password";
    private final String LANDLORD_PWD = "password";
    private final String TENANT_PWD = "password";
    private final String TENANT_TBL = "Tenant";
    private final String LANDLORD_TBL = "Landlord";
    private final String EMPLOYEE_TBL = "Employee";
    
    private String ColumnNameID;
    private String ColumnNamePwd;
    private String TableName;
    private DBHandler db;
    private static String currentProfile = "";

    public UserProfilesHandler() {
        db = new DBHandler();
    }

    public String getColumnNameID() {
        return ColumnNameID;
    }

    public void setColumnNameID(String ColumnNameID) {
        this.ColumnNameID = ColumnNameID;
    }

    public String getColumnNamePwd() {
        return ColumnNamePwd;
    }

    public void setColumnNamePwd(String ColumnNamePwd) {
        this.ColumnNamePwd = ColumnNamePwd;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String TableName) {
        this.TableName = TableName;
    }

    public String getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(String cProfile) {
        currentProfile = cProfile;
    }

    public String getEMPLOYEE_ID() {
        return EMPLOYEE_ID;
    }

    public String getEMPLOYEE_PWD() {
        return EMPLOYEE_PWD;
    }

    public String getLANDLORD_ID() {
        return LANDLORD_ID;
    }

    public String getLANDLORD_PWD() {
        return LANDLORD_PWD;
    }

    public String getTENANT_ID() {
        return TENANT_ID;
    }

    public String getTENANT_PWD() {
        return TENANT_PWD;
    }

    public String getEMPLOYEE_TBL() {
        return EMPLOYEE_TBL;
    }

    public String getLANDLORD_TBL() {
        return LANDLORD_TBL;
    }

    public String getTENANT_TBL() {
        return TENANT_TBL;
    }

    public boolean authenticate(String username, String password) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        if (username.equals("") || password.equals("")) {
            return false;
        } else {
            String queryString = "SELECT " + ColumnNameID + "," + ColumnNamePwd
                    + " FROM " + TableName
                    + " WHERE " + ColumnNameID + " = " + db.quote(username) 
                    + " AND " + ColumnNamePwd + " = " + db.quote(utils.Encrypt.enPass(password));
            PreparedStatement ps = db.DBConnect().prepareStatement(queryString);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccessPermission() {
        if (currentProfile.equals("")) {
            return false;
        }
        return true;
    }
}
