/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lease;

import database.DBHandler;
import java.sql.SQLException;

/**
 *
 * @author Thaichau
 */
public class PaymentHandler {

    DBHandler db;

    public PaymentHandler() {
        db = new DBHandler();
    }

    public boolean insertPayment(Payment payment) throws ClassNotFoundException, SQLException {
        String cashPayment = "INSERT INTO Payment VALUES(?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(cashPayment);
        ps.setString(1, payment.getPayID());
        ps.setDate(2, utils.ConvertDate.convertToSqlDate(payment.getPayDate()));
        ps.setDouble(3, Double.valueOf(payment.getPayAmount()));
        ps.setString(4, payment.getPayMethod());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean insertCashID(Payment payment) throws ClassNotFoundException, SQLException {
        String tsql = "INSERT INTO Cash_Payment VALUES(?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, payment.getPayID());
        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean insertCheckPayment(Check_Payment checkPayment) throws ClassNotFoundException, SQLException {
        String chkPayment = "INSERT INTO Check_Payment VALUES(?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(chkPayment);
        ps.setString(1, checkPayment.getPayID());
        ps.setString(2, checkPayment.getBankName());
        ps.setString(3, checkPayment.getCheckNumber());

        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean insertCreditCardPayment(CreditCard_Payment creditCardPayment) throws ClassNotFoundException, SQLException {
        String creditPayment = "INSERT INTO CreditCard_Payment VALUES(?,?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(creditPayment);
        ps.setString(1, creditCardPayment.getPayID());
        ps.setString(2, creditCardPayment.getHolderName());
        ps.setDate(3, utils.ConvertDate.convertToSqlDate(creditCardPayment.getExpDate()));
        ps.setString(4, creditCardPayment.getCcType());
        ps.setString(5, creditCardPayment.getCcNumber());

        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return true;
    }

    public boolean insertRent(Rent rent) throws ClassNotFoundException, SQLException {
        String tsql = "INSERT INTO Rent VALUES(?,?,?,?,?,?,?)";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        ps.setString(1, rent.getID());
        ps.setDouble(2, Double.valueOf(rent.getPrice()));
        ps.setDouble(3, Double.valueOf(rent.getLateFee()));
        ps.setDate(4, utils.ConvertDate.convertToSqlDate(rent.getDate()));
        ps.setDate(5, utils.ConvertDate.convertToSqlDate(rent.getDateToPay()));
        ps.setString(6, rent.getLease().getLeaseID());
        ps.setString(7, rent.getPay().getPayID());

        int result = ps.executeUpdate();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public java.sql.ResultSet getAllCashPayment() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT * FROM Payment";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        return rs;
    }

    public java.sql.ResultSet getAllCheckPayment() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT A.payID, A.payDate, A.payAmount , A.payMethod, B.bankName, B.checkNumber "
                + "FROM dbo.Payment A, dbo.Check_Payment B "
                + "WHERE A.payID = B.payID";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        return rs;
    }

    public java.sql.ResultSet getAllCreditCardPayment() throws ClassNotFoundException, SQLException {
        String tsql = "SELECT A.payID, A.payDate, A.payAmount, A.payMethod, B.holderName, B.expDate, B.ccType, B.ccNumber "
                + "FROM Payment A, CreditCard_Payment B "
                + "WHERE A.payID = B.payID";
        java.sql.PreparedStatement ps = db.DBConnect().prepareStatement(tsql);
        java.sql.ResultSet rs = ps.executeQuery();
        return rs;
    }
}
