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
public class CreditCard_Payment extends Payment{
    private String holderName;
    private Date expDate;
    private String ccType;
    private String ccNumber;

    public CreditCard_Payment() {
    }

    public CreditCard_Payment(String payID) {
        super(payID);
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcType() {
        return ccType;
    }

    public void setCcType(String ccType) {
        this.ccType = ccType;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
    
    
}