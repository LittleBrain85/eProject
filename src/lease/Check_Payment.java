/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

/**
 *
 * @author Thaichau
 */
public class Check_Payment extends Payment{
    private String bankName;
    private String checkNumber;

    public Check_Payment() {
    }

    public Check_Payment(String payID) {
        super(payID);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    
}
