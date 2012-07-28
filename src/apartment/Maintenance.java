/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import java.sql.Date;
import maintenanceUI.NotificationTemplate;

/**
 *
 * @author Thaichau
 */
public class Maintenance {
    String maintenanceID;
    java.sql.Date maintenanceDate;
    Apartment apartment;
    NotificationTemplate template;

    public Maintenance() {
        
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceID() {
        return maintenanceID;
    }

    public void setMaintenanceID(String maintenanceID) {
        this.maintenanceID = maintenanceID;
    }

    public NotificationTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NotificationTemplate template) {
        this.template = template;
    }  
    
}
