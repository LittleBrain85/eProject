/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

import java.util.Date;

/**
 *
 * @author Thaichau
 */
public class Rent {
    private String ID;
    private Double price;
    private Double lateFee;
    private java.util.Date date;
    private java.util.Date dateToPay;
    private Lease lease;
    private Payment pay;
    private static String currentRentID = null;
    public Rent() {
    }

    public Rent(String ID) {
        this.ID = ID;
    }

    public static String getCurrentRentID() {
        return currentRentID;
    }

    public static void setCurrentRentID(String currentRentID) {
        Rent.currentRentID = currentRentID;
    }
        
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateToPay() {
        return dateToPay;
    }

    public void setDateToPay(Date dateToPay) {
        this.dateToPay = dateToPay;
    }

    public Double getLateFee() {
        return lateFee;
    }

    public void setLateFee(Double lateFee) {
        this.lateFee = lateFee;
    }

    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }

    public Payment getPay() {
        return pay;
    }

    public void setPay(Payment pay) {
        this.pay = pay;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    
}
