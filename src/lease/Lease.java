/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

import apartment.Apartment;
import java.util.Date;
import java.util.Vector;
import userProfiles.Tenant;

/**
 *
 * @author Thaichau
 */
public class Lease {
    private String leaseID;
    private Date startDate;
    private Date endDate;
    private Double balance;
    private Double securityDeposit;
    private Date rentalDate;
    private Tenant tenant;
    private Apartment apartment;
    private Vector renewalList;
    private Termination termination;
    private Vector rentList;
    private static Lease lease;
    private static Lease currentLease = null;
    
    public Lease() {
    }

    public static Lease getCurrentLease() {
        return currentLease;
    }

    public static void setCurrentLease(Lease currentLease) {
        Lease.currentLease = currentLease;
    }

    public String getLeaseID() {
        return leaseID;
    }

    public void setLeaseID(String leaseID) {
        this.leaseID = leaseID;
    }

    public static Lease getLease() {
        return lease;
    }

    public static void setLease(Lease lease) {
        Lease.lease = lease;
    }
    
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Vector getRenewalList() {
        return renewalList;
    }

    public void setRenewalList(Vector renewalList) {
        this.renewalList = renewalList;
    }

    public Vector getRentList() {
        return rentList;
    }

    public void setRentList(Vector rentList) {
        this.rentList = rentList;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }
    
    public void addRenewal(Renewal renewal){
        renewalList.add(renewal);
    }
    
    public void removeRenewal(Renewal renewal){
        renewalList.remove(renewal);
    }
    
}
