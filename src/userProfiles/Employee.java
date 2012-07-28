/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfiles;

/**
 *
 * @author Thaichau
 */
public class Employee {
    private String ID;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;

    public Employee() {
    }
    
    /**
     * Second constructor
     * @param ID 
     */
    public Employee(String ID) {
        this.ID = ID;
    }

    /**
     * Getter employee ID
     * @return java.lang.String
     */
    public String getID() {
        return ID;
    }
    /**
     * get employee firstname
     * @return java.lang.String
     */
    public String getFirstname() {
        return firstname;
    }
    /**
     * get employee firstname
     * @param firstname java.lang.String
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * get employee lastname
     * @return java.lang.String
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * set employee lastname
     * @param lastname java.lang.String
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * get employee passwords
     * @return java.lang.String
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password for employee
     * @param password java.lang.String<br/>
     * <em>Note:</em><br/>
     * You must have convert password character array to String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get employee phone number
     * @return java.lang.String
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set phone number for employee
     * @param phone java.lang.String
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
