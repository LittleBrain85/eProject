/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maintenanceUI;

/**
 *
 * @author Thaichau
 */
public class NotificationTemplate {
    String templateName;
    String desScription;
    String subject;
    String emailAddress;

    public NotificationTemplate() {
    }

    public String getDesScription() {
        return desScription;
    }

    public void setDesScription(String desScription) {
        this.desScription = desScription;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    
}