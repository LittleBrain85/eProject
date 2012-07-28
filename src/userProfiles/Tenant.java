/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfiles;

import lease.Lease;

/**
 *
 * @author Thaichau
 */
public class Tenant {

    private String ID;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String currentAddress;
    private String cityStateZip;
    private Lease currentLease;

    public Tenant(String ID) {
        this.ID = ID;
    }

    public Tenant() {
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    
    public String getID() {
        return ID;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Lease getCurrentLease() {
        return currentLease;
    }

    
    public void setCurrentLease(Lease currentLease) {
        this.currentLease = currentLease;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }
    
    
}
