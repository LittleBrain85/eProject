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
public class Payment {
    private String payID;
    private Date payDate;
    private Double payAmount;
    private String payMethod;
    private Rent rent;
    private static String currentPayID = null;
    
    public Payment(String payID) {
        this.payID = payID;
    }

    public Payment() {
    }

    public static String getCurrentPayID() {
        return currentPayID;
    }

    public static void setCurrentPayID(String currentPayID) {
        Payment.currentPayID = currentPayID;
    }

    
    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPayID() {
        return payID;
    }

    public void setPayID(String payID) {
        this.payID = payID;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }
    
    
}
