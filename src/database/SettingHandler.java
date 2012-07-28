/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import userProfiles.Landlord;
import userProfilesHandler.Email;

/**
 *
 * @author Thaichau
 */
public final class SettingHandler {

    java.sql.Connection conn = null;

    public SettingHandler() {
        try {
            conn = Conn();
        } catch (SQLException ex) {
            Logger.getLogger(SettingHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SettingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public java.sql.Connection Conn() throws SQLException, ClassNotFoundException {
        java.lang.Class.forName("org.sqlite.JDBC");
        conn = java.sql.DriverManager.getConnection("jdbc:sqlite:setting.db");
        return conn;
    }

    public void dbClose() throws SQLException {
        this.conn.close();
    }

    public void dbClose(java.sql.Connection conn) throws SQLException {
        conn.close();
    }

    public boolean saveSetting(String user, String pass, String url) throws SQLException, ClassNotFoundException {
        java.sql.Statement st = conn.createStatement();
        int r = st.executeUpdate("DELETE FROM SQLAcc");
        java.sql.PreparedStatement ps = this.conn.prepareStatement("INSERT INTO SQLAcc VALUES(?,?,?);");
        ps.setString(1, user);
        ps.setString(2, pass);
        ps.setString(3, url);
        if (ps.executeUpdate() > 0) {
            return true;
        }
        return false;

    }

    public String[] getData() throws ClassNotFoundException, SQLException {
        java.sql.PreparedStatement ps = this.conn.prepareStatement("SELECT user, pass, url FROM SQLAcc");
        java.sql.ResultSet rs = ps.executeQuery();
        String data[] = new String[3];
        while (rs.next()) {
            for (int i = 0; i < 3; i++) {
                data[i] = rs.getString(i + 1);
            }
        }
        return data;
    }

    public void loadStting(javax.swing.JTextField user, javax.swing.JTextField pass, javax.swing.JTextField url)
            throws ClassNotFoundException, SQLException {
        String[] data = getData();
        user.setText(data[0]);
        pass.setText(data[1]);
        url.setText(data[2]);
    }

    public boolean checkRememberMe(String UserID, int userType) throws SQLException, ClassNotFoundException {
        String tsql = "SELECT UserID, UserType, Password FROM RememberMe WHERE UserID = ? AND UserType = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, UserID);
        ps.setInt(2, userType);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public int getUserType() throws SQLException {
        String tsql = "SELECT UserType FROM RememberMe";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        return rs.getInt(1);
    }

    public boolean saveRememberMe(String UserID, String Password, int UserType) throws SQLException, ClassNotFoundException {
        String tsql = "INSERT INTO RememberMe VALUES(?,?,?)";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, UserID);
        ps.setString(2, Password);
        ps.setInt(3, UserType);
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteRememberMe(String UserID) throws SQLException, ClassNotFoundException {
        String tsql = "DELETE FROM RememberMe WHERE UserID = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, UserID);
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public int loadRememberMe(javax.swing.JTextField user, javax.swing.JPasswordField pass, int usertype) throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM RememberMe WHERE UserType = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setInt(1, usertype);
        java.sql.ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (rs.getString(1).equals("") || rs.getString(2).equals("")) {
                user.setText(null);
                pass.setText(null);
            }
            user.setText(rs.getString(1));
            pass.setText(rs.getString(2));
            return 1;
        }
        return 0;
    }

    public String[] loadRememberMeWithUserType(int userType) throws SQLException {
        String tsql = "SELECT * FROM RememberMe WHERE UserType = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setInt(1, userType);
        java.sql.ResultSet rs = ps.executeQuery();
        String data[] = new String[2];
        while (rs.next()) {
            for (int i = 0; i < 2; i++) {
                data[i] = rs.getString(i + 1);
            }
        }
        return data;
    }

    public boolean saveLogger(String logs, javax.swing.JFrame frame) throws SQLException {
        String tsql = "INSERT INTO Logger(logDate, logContent, Frame) VALUES(?,?,?)";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setDate(1, utils.ConvertDate.convertToSqlDate(new java.util.Date()));
        ps.setString(2, logs);
        ps.setString(3, frame.getName());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public Vector loadLoggerContent(javax.swing.JFrame frame) throws SQLException {
        String tsql = "SELECT logDate, logContent FROM Logger WHERE Frame = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, frame.getName());
        java.sql.ResultSet rs = ps.executeQuery();
        Vector vLogs = new Vector();
        while (rs.next()) {
            vLogs.add(rs.getDate(1));
            vLogs.add(rs.getString(2));            
        }
        return vLogs;
    }

    public java.util.Date loadLoggerDate() throws SQLException {
        String tsql = "SELECT logDate FROM Logger";
        java.sql.Statement st = this.conn.createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        java.util.Date logsDate = rs.getTimestamp(1);
        return logsDate;
    }

    public boolean insertFakeMail(Email email) throws SQLException {
        String tsql = "INSERT INTO Email VALUES(?,?,?,?)";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, email.getTenantid());
        ps.setString(2, email.getLandlordid());
        ps.setString(3, email.getSubject());
        ps.setString(4, email.getContent());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public java.sql.ResultSet getAllResultSetFakeMail() throws SQLException {
        String tsql = "SELECT * FROM Email";
        java.sql.Statement st = this.conn.createStatement();
        java.sql.ResultSet rs = st.executeQuery(tsql);
        return rs;
    }

    public Vector getResultSetFakeMailByLandlordID(Landlord landlord) throws SQLException {
        String tsql = "SELECT * FROM Email WHERE landlordID = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, landlord.getID());
        java.sql.ResultSet rs = ps.executeQuery();
        Vector vEmail = new Vector();
        while (rs.next()) {
            vEmail.add(rs.getString(1));
            vEmail.add(rs.getString(2));
            vEmail.add(rs.getString(3));
            vEmail.add(rs.getString(4));
        }
        return vEmail;
    }

    /**
     * Tracking number of row table
     */
    public boolean saveTrackingTable(javax.swing.JTable table) throws SQLException {
        String tsql = "INSERT INTO TableTracking VALUES (?,?)";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, table.getName());
        ps.setInt(2, table.getRowCount());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean saveCheckBoxState(javax.swing.JCheckBox ck) throws SQLException {
        String tsql = "INSERT INTO CheckBoxState(CheckBoxName) VALUES(?)";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, ck.getName());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean isCheckBoxExist(javax.swing.JCheckBox ck) throws SQLException {
        String tsql = "SELECT CheckBoxName FROM CheckBoxState WHERE CheckBoxName = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, ck.getName());
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean loadCheckBoxStateByName(javax.swing.JCheckBox ck) throws SQLException {
        String tsql = "SELECt State FROM CheckBoxState WHERE CheckBoxName = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, ck.getName());
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean deleteCheckBoxState(javax.swing.JCheckBox ck) throws SQLException {
        String tsql = "DELETE FROM CheckBoxState WHERE CheckBoxName = ?";
        java.sql.PreparedStatement ps = this.conn.prepareStatement(tsql);
        ps.setString(1, ck.getName());
        if (ps.executeUpdate() > 0) {
            return true;
        }
        return false;
    }
}
