/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

import java.sql.Date;

/**
 *
 * @author Thaichau
 */
public class Termination {
    private String terminationID;
    private java.sql.Date leavingDate;
    private String leavingReason;
    private Lease lease;
    private SecurityRefund refund;

    public Termination() {
    }

    public String getTerminationID() {
        return terminationID;
    }

    public void setTerminationID(String terminationID) {
        this.terminationID = terminationID;
    }
    
    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }

    public java.sql.Date getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(java.sql.Date leavingDate) {
        this.leavingDate = leavingDate;
    }

    public String getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(String leavingReason) {
        this.leavingReason = leavingReason;
    }

    public SecurityRefund getRefund() {
        return refund;
    }

    public void setRefund(SecurityRefund refund) {
        this.refund = refund;
    }
    
    
}
