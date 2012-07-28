/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfilesHandler;

/**
 *
 * @author Thaichau
 */
public class Email {
    private String tenantid;
    private String landlordid;
    private String subject;
    private String content;

    public Email() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLandlordid() {
        return landlordid;
    }

    public void setLandlordid(String landlordid) {
        this.landlordid = landlordid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }
    
    
}
