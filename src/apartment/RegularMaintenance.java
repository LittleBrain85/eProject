/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import java.sql.Date;

/**
 *
 * @author Thaichau
 */
public class RegularMaintenance extends Maintenance{
    String pestType;
    String pestControlType;
    String emergencyTestType;
    java.sql.Date emergencyTestDate;
    java.sql.Date pestControlDate;

    public RegularMaintenance() {
    }

    public Date getEmergencyTestDate() {
        return emergencyTestDate;
    }

    public void setEmergencyTestDate(Date emergencyTestDate) {
        this.emergencyTestDate = emergencyTestDate;
    }

    public String getEmergencyTestType() {
        return emergencyTestType;
    }

    public void setEmergencyTestType(String emergencyTestType) {
        this.emergencyTestType = emergencyTestType;
    }

    public Date getPestControlDate() {
        return pestControlDate;
    }

    public void setPestControlDate(Date pestControlDate) {
        this.pestControlDate = pestControlDate;
    }

    public String getPestControlType() {
        return pestControlType;
    }

    public void setPestControlType(String pestControlType) {
        this.pestControlType = pestControlType;
    }

    public String getPestType() {
        return pestType;
    }

    public void setPestType(String pestType) {
        this.pestType = pestType;
    }
    
    
}
