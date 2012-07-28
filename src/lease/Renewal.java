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
public class Renewal {
    private String renewalID;
    private Date renewalDate;

    public Renewal(String renewalID) {
        this.renewalID = renewalID;
    }

    public String getRenewalID() {
        return renewalID;
    }
    
    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(Date renewalDate) {
        this.renewalDate = renewalDate;
    }
    
    
}
