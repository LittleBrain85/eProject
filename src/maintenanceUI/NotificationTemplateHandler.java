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
public class NotificationTemplateHandler {
    DBHandler db;

    public NotificationTemplateHandler() {
        db = new DBHandler();
    }
    
    public boolean insertNotificationTemplate(NotificationTemplate template) throws ClassNotFoundException, SQLException{
        String tsql= "INSERT INTO NotificationTemplate VALUES(?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, template.getTemplateName());
        ps.setString(2, template.getDesScription());
        ps.setString(3, template.getSubject());
        ps.setString(4, template.getEmailAddress());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean deleteNotificationTemplate(NotificationTemplate notify) throws ClassNotFoundException, SQLException{
        String tsql = "DELETE FROM NotificationTemplate WHRE templateName = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, notify.getTemplateName());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public boolean updateNotificationTemplate(NotificationTemplate notify) throws ClassNotFoundException, SQLException{
        String tsql = "UPDATE NotificationTemplate SET description = ?, subject = ?, emailAddress = ? "
                + "WHERE templateName = ?";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, notify.getTemplateName());
        int result = ps.executeUpdate();
        if(result > 0){
            return true;
        }
        return false;
    }
    
    public Vector getVectorTemplateName(NotificationTemplate notify) throws ClassNotFoundException, SQLException{
        String tsql = "SELECT templateName FROM NotificationTemplate";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        Vector vTemplateName = new Vector();
        while(rs.next()){
            vTemplateName.add(rs.getString(1));
        }
        return vTemplateName;
    }
}
