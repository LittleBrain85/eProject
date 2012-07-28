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
public class ApartmentChange {
    private String apartmentNumber;
    private Date changeDate;
    private String newApartmentNumber;

    public ApartmentChange() {
    }

    public ApartmentChange(String apartmentNumber, Date changeDate, String newApartmentNumber) {
        this.apartmentNumber = apartmentNumber;
        this.changeDate = changeDate;
        this.newApartmentNumber = newApartmentNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getNewApartmentNumber() {
        return newApartmentNumber;
    }

    public void setNewApartmentNumber(String newApartmentNumber) {
        this.newApartmentNumber = newApartmentNumber;
    }
    
    
}
