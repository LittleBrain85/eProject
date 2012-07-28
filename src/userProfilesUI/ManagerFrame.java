/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfilesUI;

import apartment.*;
import database.DBHandler;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import lease.*;
import maintenanceUI.NotificationTemplate;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import userProfiles.Landlord;
import userProfiles.Manager;
import userProfiles.Tenant;
import userProfilesHandler.LandlordHandler;
import userProfilesHandler.TenantHandler;

/**
 *
 * @author Thaichau
 */
public class ManagerFrame extends javax.swing.JFrame {

    Manager manager;
    ApartmentHandler ap;
    DBHandler db;
    Apartment apartment;
    Building building;
    BuildingHandler bh;
    TenantHandler th;
    Tenant tenant;
    LandlordHandler landlordHandler;
    Lease lease;
    LeaseHandler lh;
    PaymentHandler ph;
    Rent rent;
    Cash_Payment cash;
    Check_Payment check;
    CreditCard_Payment credit;
    String paymentMethod;
    ApartmentChangeHandler ach;
    Termination termination;
    TerminationHandler terHandler;
    JasperReport jasperReport;
    JasperPrint jasperPrint;
    HashMap jasperParameter;
    JRViewer reportViewer;
    MaintenanceHandler mh;
    AccidentalMaintenance acc;
    AccidentalMaintenanceHandler accHandler;

    /**
     * Creates new form Manager
     */
    public ManagerFrame() {
        initComponents();
        manager = new Manager();
//        this.panelRent.setEnabledAt(1, false);
//        this.panelRent.setEnabledAt(2, false);
//        this.panelRent.setEnabledAt(3, false);
//        this.panelRent.setEnabledAt(4, false);
//        this.panelRent.setEnabledAt(5, false);
        ap = new ApartmentHandler();
        db = new DBHandler();
        tenant = new Tenant();
        th = new TenantHandler();
        landlordHandler = new LandlordHandler();
        apartment = new Apartment();
        building = new Building();
        bh = new BuildingHandler();
        lh = new LeaseHandler();
        ph = new PaymentHandler();
        lease = new Lease();
        rent = new Rent();
        cash = new Cash_Payment();
        check = new Check_Payment();
        credit = new CreditCard_Payment();
        ach = new ApartmentChangeHandler();
        termination = new Termination();
        terHandler = new TerminationHandler();
        mh = new MaintenanceHandler();
        acc = new AccidentalMaintenance();
        accHandler = new AccidentalMaintenanceHandler();
        try {
            this.loadApartment();
            this.loadApartmentChange();
            this.loadCashTable();
            this.loadCheckPaymentTable();
            this.loadCreditCardPaymentTable();
            this.loadTerminationTable();
            this.loadLandlordTable();
            this.loadAccidentalMaintenanceTable();
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }

    }

    private void loadApartment() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = ap.getApartmentNotRent();
        db.loadTable(apartmentModel, rs);
    }

    private void setApartmentValueToForm() {
        this.txtApartmentNumberRent.setText(this.tbApartmentRent.getValueAt(this.tbApartmentRent.getSelectedRow(), 0).toString());
        this.txtApartmentSizeRent.setText(this.tbApartmentRent.getValueAt(this.tbApartmentRent.getSelectedRow(), 1).toString());
        this.txtApartmentTypeRent.setText(this.tbApartmentRent.getValueAt(this.tbApartmentRent.getSelectedRow(), 2).toString());
        this.txtFeeRent.setText(this.tbApartmentRent.getValueAt(this.tbApartmentRent.getSelectedRow(), 3).toString());
        this.txtBuildingNameRent.setText(this.tbApartmentRent.getValueAt(this.tbApartmentRent.getSelectedRow(), 4).toString());
    }

    private void loadApartmentChange() throws SQLException, ClassNotFoundException {
        java.sql.ResultSet rs = ach.getAllResultSetApartmentChange();
        db.loadTable(apartmentChangeModelManager, rs);
    }

    private void loadTerminationTable() throws SQLException, ClassNotFoundException {
        java.sql.ResultSet rs = terHandler.getResultsetTermination();
        db.loadTable(this.terModel, rs);
    }

    private void setValueToTerminationForm() throws ParseException {
        this.txtTerminationTerID.setText(this.tbTermination.getValueAt(this.tbTermination.getSelectedRow(), 0).toString());
        this.jdcLeavingDateTer.setDate(utils.ConvertDate.convertStringToDate(this.tbTermination.getValueAt(this.tbTermination.getSelectedRow(), 1).toString()));
        this.txtLeavingReasonTer.setText(this.tbTermination.getValueAt(this.tbTermination.getSelectedRow(), 2).toString());

    }

    private void setApartmentChangValueToForm() throws ParseException {
        this.txtApartmentNumberChange.setText(this.tbApartmentChangeManager.getValueAt(this.tbApartmentChangeManager.getSelectedRow(), 0).toString());
        this.jdcChangeDate.setDate(utils.ConvertDate.convertStringToDate(this.tbApartmentChangeManager.getValueAt(this.tbApartmentChangeManager.getSelectedRow(), 1).toString()));
        this.txtApartmentNumberNew.setText(this.tbApartmentChangeManager.getValueAt(this.tbApartmentChangeManager.getSelectedRow(), 2).toString());
    }

    private void loadCashTable() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = ph.getAllCashPayment();
        db.loadTable(cashModel, rs);
    }

    private void setValueToCashPaymentForm() {
        this.cashPayID.setText(this.tbCashPayment.getValueAt(this.tbCashPayment.getSelectedRow(), 0).toString());
        this.cashPayDate.setText(this.tbCashPayment.getValueAt(this.tbCashPayment.getSelectedRow(), 1).toString());
        this.cashPayAmount.setText(this.tbCashPayment.getValueAt(this.tbCashPayment.getSelectedRow(), 2).toString());
        this.cashPayMethod.setText(this.tbCashPayment.getValueAt(this.tbCashPayment.getSelectedRow(), 3).toString());
    }

    private void loadCheckPaymentTable() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = ph.getAllCheckPayment();
        db.loadTable(checkModel, rs);
    }

    private void setValueToCheckPaymentForm() {
        this.checkPayID.setText(this.tbCheckPayment.getValueAt(this.tbCheckPayment.getSelectedRow(), 0).toString());
        this.checkPayDate.setText(this.tbCheckPayment.getValueAt(this.tbCheckPayment.getSelectedRow(), 1).toString());
        this.checkPayAmount.setText(this.tbCheckPayment.getValueAt(this.tbCheckPayment.getSelectedRow(), 2).toString());
        this.checkPayMethod.setText(this.tbCheckPayment.getValueAt(this.tbCheckPayment.getSelectedRow(), 3).toString());
        this.checkBankName.setText(this.tbCheckPayment.getValueAt(this.tbCheckPayment.getSelectedRow(), 4).toString());
        this.checkCheckNumber.setText(this.tbCheckPayment.getValueAt(this.tbCheckPayment.getSelectedRow(), 5).toString());
    }

    private void loadCreditCardPaymentTable() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = ph.getAllCreditCardPayment();
        db.loadTable(creditCardModel, rs);
    }

    private void setValueToCreditCardForm() {
        this.CreditCardPayID.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 0).toString());
        this.CreditCardPayDate.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 1).toString());
        this.CreditCardPayAmount.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 2).toString());
        this.CreditCardPayMethod.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 3).toString());
        this.CreditCardHolderName.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 4).toString());
        this.CreditCardExperiedDate.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 5).toString());
        this.CreditCardCreditType.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 6).toString());
        this.CreditCardCreditCardNumber.setText(this.tbCreditCardPayment.getValueAt(this.tbCreditCardPayment.getSelectedRow(), 7).toString());
    }

    private void loadLandlordTable() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = landlordHandler.getAllResultSetLandlord();
        db.loadTable(landlordModel, rs);
    }

    private void setValueToLandlordProfile() {
        this.txtLandlordIDManager.setText(this.tbLandlordManager.getValueAt(this.tbLandlordManager.getSelectedRow(), 0).toString());
        this.txtLandlordPasswordManager.setText(this.tbLandlordManager.getValueAt(this.tbLandlordManager.getSelectedRow(), 1).toString());
    }

    private void loadMaintenanceTable() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = mh.getResultSetMaintenance();
        db.loadTable(maintenanceModel, rs);
    }

    private void setValueToMaintenanceForm() {
        this.txtApartmentNo.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 0).toString());
        this.txtMaintenanceDate.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 1).toString());
        this.txtMaintenanceID.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 2).toString());
        this.txtMaintenanceEmail.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 3).toString());
        this.txtMaintenaceSubject.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 4).toString());
        this.txtMantenanceTemplate.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 5).toString());
        this.txtMaintenanceDescription.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 6).toString());
        for (int i = 0; i < this.tbMaintenanceRating.getRowCount(); i++) {
            if (this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 2).toString().equals(this.tbMaintenanceRating.getValueAt(i, 1).toString())) {
                this.tabSubTask.setEnabledAt(3, false);
                return;
            }
        }
        this.tabSubTask.setEnabledAt(3, true);
        this.txtMRApartmentNumber.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 0).toString());
        this.txtMRMaintenanceID.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 2).toString());
        this.txtMRFeedbackDate.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 1).toString());
        this.txtMRProblem.setText(this.tbMaintenanceRequest.getValueAt(this.tbMaintenanceRequest.getSelectedRow(), 4).toString());
    }

    private void loadAccidentalMaintenanceTable() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = accHandler.getResultSetAccidentalMaintenance();
        db.loadTable(this.maintenancRatingModel, rs);
    }

    private void setValueMaintenanceRatingForm() {
        this.txtMRApartmentNumber.setText(this.tbMaintenanceRating.getValueAt(this.tbMaintenanceRating.getSelectedRow(), 0).toString());
        this.txtMRMaintenanceID.setText(this.tbMaintenanceRating.getValueAt(this.tbMaintenanceRating.getSelectedRow(), 1).toString());
        this.txtMRProblem.setText(this.tbMaintenanceRating.getValueAt(this.tbMaintenanceRating.getSelectedRow(), 2).toString());
        this.txtMRFeedbackDate.setText(this.tbMaintenanceRating.getValueAt(this.tbMaintenanceRating.getSelectedRow(), 3).toString());
        this.txtMRRating.setText(this.tbMaintenanceRating.getValueAt(this.tbMaintenanceRating.getSelectedRow(), 4).toString());
    }

    private void resetMaintenanceForm() {
        utils.ResetFields.resetTextFields(
                txtApartmentNo,
                txtMaintenanceDate,
                txtMaintenanceID,
                txtMaintenanceEmail,
                txtMaintenaceSubject,
                txtMantenanceTemplate,
                txtMRApartmentNumber,
                txtMRMaintenanceID,
                txtMRProblem,
                txtMRFeedbackDate,
                txtMRRating);
        this.txtMaintenanceDescription.setText(null);
        try {
            utils.ListenerManager.disableListenerOnTable(this.tbMaintenanceRequest, rowMaintenanceListener);
            this.loadMaintenanceTable();
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbMaintenanceRequest, rowMaintenanceListener);
        }
    }

    private void resetMaintenanceRatingForm() {
        utils.ResetFields.resetTextFields(
                txtMRApartmentNumber, txtMRMaintenanceID, txtMRProblem, txtMRFeedbackDate, txtMRRating);
        utils.ListenerManager.disableListenerOnTable(this.tbMaintenanceRating, rowMaintenancRatingListener);
        try {
            this.loadAccidentalMaintenanceTable();
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbMaintenanceRating, rowMaintenancRatingListener);
            this.btAdd.setEnabled(true);
            this.txtMRApartmentNumber.setEditable(true);
            this.txtMRMaintenanceID.setEditable(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabPane = new javax.swing.JTabbedPane();
        HomePanel = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        panelBuildingPicture = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        TaskPanel = new javax.swing.JPanel();
        tabSubTask = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbApartmentChangeManager = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtApartmentNumberChange = new javax.swing.JTextField();
        txtApartmentNumberNew = new javax.swing.JTextField();
        btnRequestChange = new javax.swing.JButton();
        btnResetChange = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jdcChangeDate = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbCashPayment = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        cashPayID = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        cashPayDate = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        cashPayAmount = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        cashPayMethod = new javax.swing.JTextField();
        cashBtnView = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbCheckPayment = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        checkPayID = new javax.swing.JTextField();
        checkBtnView = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        checkPayDate = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        checkPayAmount = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        checkPayMethod = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        checkBankName = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        checkCheckNumber = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbCreditCardPayment = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        CreditCardPayID = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        CreditCardHolderName = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        CreditCardExperiedDate = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        CreditCardCreditType = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        CreditCardCreditCardNumber = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        CreditCardPayDate = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        CreditCardPayAmount = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        CreditCardPayMethod = new javax.swing.JTextField();
        CreditCardViewDetails = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbMaintenanceRequest = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();
        txtApartmentNo = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        txtMaintenanceDate = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        txtMaintenanceID = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtMaintenanceDescription = new javax.swing.JTextArea();
        jLabel96 = new javax.swing.JLabel();
        txtMaintenanceEmail = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        txtMantenanceTemplate = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        txtMaintenaceSubject = new javax.swing.JTextField();
        btDeleteMaintenance = new javax.swing.JButton();
        btResetMaintenanceRequest = new javax.swing.JButton();
        btTurnOnRatingTab = new javax.swing.JButton();
        btTurnOffRatingTab = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tbMaintenanceRating = new javax.swing.JTable();
        jPanel30 = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        txtMRApartmentNumber = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        txtMRMaintenanceID = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        txtMRProblem = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        txtMRFeedbackDate = new javax.swing.JTextField();
        jLabel103 = new javax.swing.JLabel();
        txtMRRating = new javax.swing.JTextField();
        btAdd = new javax.swing.JButton();
        btUpdateMaintenanceRating = new javax.swing.JButton();
        btDeleteMaintenanceRating = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        txtTerminationTerID = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jdcLeavingDateTer = new com.toedter.calendar.JDateChooser();
        jLabel80 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtLeavingReasonTer = new javax.swing.JTextArea();
        btnInsert = new javax.swing.JButton();
        btnResetTer = new javax.swing.JButton();
        btUpdateTermination = new javax.swing.JButton();
        btDeleteTerminaiton = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbTermination = new javax.swing.JTable();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        txtTemplateName = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        txtSubject = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        taDescription = new javax.swing.JTextArea();
        btAddNotificationTemplate = new javax.swing.JButton();
        cbEmailNotification = new javax.swing.JComboBox();
        btDeleteNotify = new javax.swing.JButton();
        btUpdateNotify = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbNotification = new javax.swing.JTable();
        PaymentPanel = new javax.swing.JPanel();
        tabRenNewApartment = new javax.swing.JTabbedPane();
        panelChoseApartment = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtApartmentNumberRent = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtApartmentSizeRent = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApartmentTypeRent = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFeeRent = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtBuildingNameRent = new javax.swing.JTextField();
        btnRent = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbApartmentRent = new javax.swing.JTable();
        panelTenantRegistration = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtTenantID = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFirstname = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtLastname = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtStateZip = new javax.swing.JTextField();
        tbnRegistration = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        panelCreatelease = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtLeaseID = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnCreateLease = new javax.swing.JButton();
        jdcStartDate = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jdcEndDate = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtSecurityDeposit = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtTenantIDLease = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtApartmentNumberLease = new javax.swing.JTextField();
        jdcRentDate = new com.toedter.calendar.JDateChooser();
        jLabel104 = new javax.swing.JLabel();
        panelPayment = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        cbPaymentMethod = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        txtPayID = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtPayDate = new com.toedter.calendar.JDateChooser();
        jLabel25 = new javax.swing.JLabel();
        txtPayAmount = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        displayPanel = new javax.swing.JPanel();
        CashPayment = new javax.swing.JPanel();
        btnCashPayment = new javax.swing.JButton();
        CheckPayment = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txtBankName = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtCheckNumber = new javax.swing.JTextField();
        btnCheckPayment = new javax.swing.JButton();
        CreditCardPayment = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtHolderName = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cbExpDate = new com.toedter.calendar.JDateChooser();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtCreditCardNumber = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        btnCreditCardPayment = new javax.swing.JButton();
        cbCreditCardType = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        txtRentID = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtRentFeeRent = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtLateFee = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jcdDateToPay = new com.toedter.calendar.JDateChooser();
        jcdDate = new com.toedter.calendar.JDateChooser();
        jLabel39 = new javax.swing.JLabel();
        txtLeaseIDRent = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtPayIDRent = new javax.swing.JTextField();
        btnRentNewApartment = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        panelReport = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        txtFinalTenantID = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtFinalLastname = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        txtFinalFirstname = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txtFinalEmail = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtZipCode = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        txtFinalPhone = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        txtFinalAddress = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        txtFinalLeaseID = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        txtFinalStartDate = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        txtFinalEndDate = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        txtFinalBalance = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        txtFinalSecurityDeposit = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        txtFinalRentalDate = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        txtFinalApartmentNumber = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        txtFinalTerminationID = new javax.swing.JTextField();
        btnFinalPrint = new javax.swing.JButton();
        jLabel77 = new javax.swing.JLabel();
        btnPrintTenantDetails = new javax.swing.JButton();
        btnPrintLeaseDetails = new javax.swing.JButton();
        panelAccount = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        txtLandlordIDManager = new javax.swing.JTextField();
        jLabel87 = new javax.swing.JLabel();
        txtLandlordPasswordManager = new javax.swing.JPasswordField();
        btnLandlordRegistration = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tbLandlordManager = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        txtNumBuildManager = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        txtNumApartmentManager = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        txtNumRentManager = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        txtNumNotRentManager = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taManagerLog = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manager Panel");

        mainTabPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        mainTabPane.setAlignmentY(0.0F);
        mainTabPane.setPreferredSize(new java.awt.Dimension(600, 400));
        mainTabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainTabPaneMouseClicked(evt);
            }
        });

        HomePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(0, 0, 255));
        jLabel86.setText("Aapartment Management System Manager");

        panelBuildingPicture.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Orient_apartment_lon.jpg"))); // NOI18N

        javax.swing.GroupLayout panelBuildingPictureLayout = new javax.swing.GroupLayout(panelBuildingPicture);
        panelBuildingPicture.setLayout(panelBuildingPictureLayout);
        panelBuildingPictureLayout.setHorizontalGroup(
            panelBuildingPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22)
        );
        panelBuildingPictureLayout.setVerticalGroup(
            panelBuildingPictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBuildingPictureLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel22))
        );

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HomePanelLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jLabel86))
                    .addGroup(HomePanelLayout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(panelBuildingPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel86)
                .addGap(72, 72, 72)
                .addComponent(panelBuildingPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );

        mainTabPane.addTab("Home", new javax.swing.ImageIcon(getClass().getResource("/images/home.png")), HomePanel, "Back To Home"); // NOI18N

        tabSubTask.setPreferredSize(new java.awt.Dimension(600, 500));
        tabSubTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabSubTaskMouseClicked(evt);
            }
        });

        rowApartmentChangeManagerListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                try{
                    setApartmentChangValueToForm();
                }catch(java.text.ParseException ex){

                }
            }
        };
        tbApartmentChangeManager.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Apartment Number", "Change Date", "New Apartment Number"
            }
        ));
        apartmentChangeModelManager = (javax.swing.table.DefaultTableModel)this.tbApartmentChangeManager.getModel();
        jScrollPane3.setViewportView(tbApartmentChangeManager);
        tbApartmentChangeManager.getSelectionModel().addListSelectionListener(rowApartmentChangeManagerListener);

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel41.setText("Apartment Number");

        jLabel43.setText("New Apartmentnumber");

        btnRequestChange.setText("Confirm Request");
        btnRequestChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequestChangeActionPerformed(evt);
            }
        });

        btnResetChange.setText("Reset");

        jLabel42.setText("ChangeDate");

        jdcChangeDate.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addComponent(jLabel43))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApartmentNumberChange)
                            .addComponent(txtApartmentNumberNew, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(btnRequestChange)))
                .addGap(33, 33, 33)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(64, 64, 64)
                        .addComponent(jdcChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnResetChange))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel41)
                        .addComponent(txtApartmentNumberChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42))
                    .addComponent(jdcChangeDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtApartmentNumberNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRequestChange)
                    .addComponent(btnResetChange))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(13, 13, 13))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabSubTask.addTab("Process Apartmentchange", jPanel6);

        rowCashListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setValueToCashPaymentForm();
            }
        };
        tbCashPayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Pay ID", "Pay Date", "Pay Amount ", "Pay Method"
            }
        ));
        tbCashPayment.getSelectionModel().addListSelectionListener(rowCashListener);
        jScrollPane4.setViewportView(tbCashPayment);
        cashModel = (javax.swing.table.DefaultTableModel)this.tbCashPayment.getModel();

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel52.setText("PayID");

        jLabel53.setText("Pay Date");

        jLabel54.setText("Pay Amount");

        jLabel55.setText("Pay Method");

        cashBtnView.setText("Views Details");
        cashBtnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashBtnViewActionPerformed(evt);
            }
        });

        jButton2.setText("Delete");

        jButton3.setText("Update");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addComponent(jLabel54))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cashPayID)
                            .addComponent(cashPayAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                    .addComponent(jButton2))
                .addGap(32, 32, 32)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel53)
                            .addComponent(jLabel55))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cashPayDate)
                            .addComponent(cashPayMethod, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                        .addGap(52, 52, 52))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(33, 33, 33)
                        .addComponent(cashBtnView)
                        .addContainerGap())))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(cashPayID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(cashPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(cashPayAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55)
                    .addComponent(cashPayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashBtnView)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel31.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton4.setText("Next >");

        jButton5.setText("<<  First");

        jButton6.setText("Last >>");

        jButton7.setText("< Previous");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jButton5)
                .addGap(64, 64, 64)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton7)
                .addGap(62, 62, 62)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jTabbedPane2.addTab("Cash Payment", jPanel10);

        rowCheckSelection = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setValueToCheckPaymentForm();
            }
        };
        tbCheckPayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pay ID", "Pay Date", "Pay Amount", "Pay Method", "Bank Name", "Check Number"
            }
        ));
        tbCheckPayment.getSelectionModel().addListSelectionListener(rowCheckSelection);
        jScrollPane5.setViewportView(tbCheckPayment);
        checkModel = (javax.swing.table.DefaultTableModel)this.tbCheckPayment.getModel();

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel49.setText("Pay ID");

        checkBtnView.setText("View Details");

        jLabel56.setText("PayDate");

        jLabel57.setText("Pay Amount");

        jLabel58.setText("Pay Method");

        jLabel50.setText("Bank Name");

        jLabel51.setText("Check Number");

        jButton8.setText("Delete");

        jButton9.setText("Update");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel57)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel49)
                                .addComponent(jLabel56))
                            .addComponent(jLabel58))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(checkPayAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(checkPayDate, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkPayID, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkPayMethod))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel50))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkCheckNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                            .addComponent(checkBankName)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(jButton8)
                        .addGap(18, 18, 18)
                        .addComponent(checkBtnView)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(checkPayID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50)
                    .addComponent(checkBankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(checkPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51)
                    .addComponent(checkCheckNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(checkPayAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(checkPayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBtnView)
                    .addComponent(jButton9)
                    .addComponent(jButton8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel32.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton10.setText(" << First");

        jButton11.setBackground(new java.awt.Color(204, 204, 204));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton11.setText("Next >");

        jButton12.setBackground(new java.awt.Color(204, 204, 204));
        jButton12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton12.setText("< Previous");

        jButton13.setText("Last >>");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jButton10)
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton12)
                .addGap(18, 18, 18)
                .addComponent(jButton13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton11)
                    .addComponent(jButton12)
                    .addComponent(jButton13))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jTabbedPane2.addTab("Check Payment", jPanel11);

        rowCreditCardListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                setValueToCreditCardForm();
            }
        };
        tbCreditCardPayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Pay ID", "Pay Date", "Pay Amount", "Pay Method", "Holder Name", "Experied Date", "Credit Card Type", "Credit Card Number"
            }
        ));
        creditCardModel = (javax.swing.table.DefaultTableModel)tbCreditCardPayment.getModel();
        jScrollPane6.setViewportView(tbCreditCardPayment);
        this.tbCreditCardPayment.getSelectionModel().addListSelectionListener(rowCreditCardListener);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel44.setText("Pay ID");

        jLabel45.setText("Holder Name");

        jLabel46.setText("Experied Date");

        jLabel47.setText("Credit Type");

        jLabel48.setText("Credit Card Number");

        jLabel59.setText("Pay Date");

        jLabel60.setText("Pay Amount");

        jLabel61.setText("Pay Method");

        CreditCardViewDetails.setText("View Details");

        jButton14.setText("Delete");

        jButton15.setText("Update");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel60)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45)
                            .addComponent(jLabel61)))
                    .addComponent(jLabel44)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CreditCardPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CreditCardPayID, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CreditCardPayAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CreditCardPayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(40, 40, 40))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(CreditCardCreditType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(CreditCardExperiedDate, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CreditCardCreditCardNumber)))
                    .addComponent(CreditCardHolderName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(211, 211, 211)
                .addComponent(jButton14)
                .addGap(38, 38, 38)
                .addComponent(CreditCardViewDetails)
                .addGap(35, 35, 35)
                .addComponent(jButton15)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(CreditCardPayID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46)
                    .addComponent(CreditCardExperiedDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(CreditCardCreditType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59)
                    .addComponent(CreditCardPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CreditCardCreditCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(CreditCardPayAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CreditCardPayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CreditCardHolderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CreditCardViewDetails)
                    .addComponent(jButton14)
                    .addComponent(jButton15))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Credit Card Payment", jPanel12);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );

        tabSubTask.addTab("Payment Report", jPanel8);

        rowMaintenanceListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setValueToMaintenanceForm();
            }
        };
        tbMaintenanceRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Apartment Number", "Maintenance Date", "Maintenance ID", "Email", "Subject", "Template Name", "Description"
            }
        ));
        jScrollPane14.setViewportView(tbMaintenanceRequest);
        tbMaintenanceRequest.getSelectionModel().addListSelectionListener(rowMaintenanceListener);
        maintenanceModel = (javax.swing.table.DefaultTableModel)this.tbMaintenanceRequest.getModel();

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Maintenance Information Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel88.setText("Apartment No");

        jLabel93.setText("MaintenanceDate");

        jLabel94.setText("Maintenance ID");

        jLabel95.setText("Description");

        txtMaintenanceDescription.setColumns(20);
        txtMaintenanceDescription.setRows(5);
        jScrollPane15.setViewportView(txtMaintenanceDescription);

        jLabel96.setText("Email");

        jLabel97.setText("Template Name");

        jLabel98.setText("Subject");

        btDeleteMaintenance.setText("Delete");
        btDeleteMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteMaintenanceActionPerformed(evt);
            }
        });

        btResetMaintenanceRequest.setText("Reset");
        btResetMaintenanceRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetMaintenanceRequestActionPerformed(evt);
            }
        });

        btTurnOnRatingTab.setText("Turn On Rating Tab");
        btTurnOnRatingTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTurnOnRatingTabActionPerformed(evt);
            }
        });

        btTurnOffRatingTab.setText("Turn Off Rating Tab");
        btTurnOffRatingTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTurnOffRatingTabActionPerformed(evt);
            }
        });

        btRefresh.setText("Refresh");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel88)
                            .addComponent(jLabel94)
                            .addComponent(jLabel98))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApartmentNo, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(txtMaintenanceID)
                            .addComponent(txtMaintenaceSubject))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel93)
                            .addComponent(jLabel96)
                            .addComponent(jLabel97))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtMaintenanceEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(txtMaintenanceDate, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMantenanceTemplate)))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addGap(61, 61, 61)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(btDeleteMaintenance, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btResetMaintenanceRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(btTurnOnRatingTab, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btTurnOffRatingTab)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                                .addComponent(btRefresh))
                            .addComponent(jScrollPane15))))
                .addGap(23, 23, 23))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(txtApartmentNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93)
                    .addComponent(txtMaintenanceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(txtMaintenanceID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel96)
                    .addComponent(txtMaintenanceEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaintenaceSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98)
                    .addComponent(jLabel97)
                    .addComponent(txtMantenanceTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel95))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btDeleteMaintenance)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btResetMaintenanceRequest)
                        .addComponent(btTurnOnRatingTab)
                        .addComponent(btTurnOffRatingTab)
                        .addComponent(btRefresh)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane14))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabSubTask.addTab("Maintenance Request", jPanel9);

        rowMaintenancRatingListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setValueMaintenanceRatingForm();
            }
        };
        tbMaintenanceRating.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Apartment Number", "Maintenance ID", "Problem", "FeedbackDate", "MaintenanceRating"
            }
        ));
        tbMaintenanceRating.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMaintenanceRatingMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tbMaintenanceRating);
        maintenancRatingModel = (javax.swing.table.DefaultTableModel)this.tbMaintenanceRating.getModel();
        this.tbMaintenanceRating.getSelectionModel().addListSelectionListener(rowMaintenancRatingListener);

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Maintenance Rating", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel99.setText("Apartment Number");

        jLabel100.setText("Maintenance ID");

        jLabel101.setText("Problem");

        jLabel102.setText("Feedback Date");

        jLabel103.setText("Maintenance Rating");

        btAdd.setText("Insert");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btUpdateMaintenanceRating.setText("Update");
        btUpdateMaintenanceRating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateMaintenanceRatingActionPerformed(evt);
            }
        });

        btDeleteMaintenanceRating.setText("Delete");

        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel99)
                            .addComponent(jLabel101)
                            .addComponent(jLabel103))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMRProblem)
                            .addComponent(txtMRApartmentNumber)
                            .addComponent(txtMRRating, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                        .addGap(81, 81, 81)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel100)
                            .addComponent(jLabel102))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMRMaintenanceID)
                            .addComponent(txtMRFeedbackDate, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btUpdateMaintenanceRating, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(btDeleteMaintenanceRating, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(jButton1)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(txtMRApartmentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel100)
                    .addComponent(txtMRMaintenanceID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(txtMRProblem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel102)
                    .addComponent(txtMRFeedbackDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(txtMRRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd)
                    .addComponent(btUpdateMaintenanceRating)
                    .addComponent(btDeleteMaintenanceRating)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
        );

        tabSubTask.addTab("Maintenance Rating", jPanel29);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Input Termination"));

        jLabel78.setText("Termintaion ID");

        jLabel79.setText("Leaving Date");

        jdcLeavingDateTer.setDateFormatString("dd/MM/yyyy");

        jLabel80.setText("Leaving Reason");

        txtLeavingReasonTer.setColumns(20);
        txtLeavingReasonTer.setRows(5);
        jScrollPane7.setViewportView(txtLeavingReasonTer);

        btnInsert.setText("Insert");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnResetTer.setText("Reset");

        btUpdateTermination.setText("Update");
        btUpdateTermination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateTerminationActionPerformed(evt);
            }
        });

        btDeleteTerminaiton.setText("Delete");
        btDeleteTerminaiton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteTerminaitonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel78)
                            .addComponent(jLabel79)
                            .addComponent(jLabel80))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTerminationTerID)
                                .addComponent(jdcLeavingDateTer, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(btnInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btUpdateTermination)
                        .addGap(45, 45, 45)
                        .addComponent(btDeleteTerminaiton)
                        .addGap(42, 42, 42)
                        .addComponent(btnResetTer)))
                .addContainerGap(218, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(txtTerminationTerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel79)
                    .addComponent(jdcLeavingDateTer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel80)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnInsert)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnResetTer)
                        .addComponent(btDeleteTerminaiton)
                        .addComponent(btUpdateTermination)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        terRowListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                try{
                    setValueToTerminationForm();
                }catch(java.text.ParseException ex){
                    taManagerLog.append("\n" + ex.getMessage());
                }
            }
        };
        tbTermination.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Termination ID", "Leaving Date", "Leaving Reason"
            }
        ));
        terModel = (javax.swing.table.DefaultTableModel)this.tbTermination.getModel();
        jScrollPane8.setViewportView(tbTermination);
        tbTermination.getSelectionModel().addListSelectionListener(terRowListener);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(296, 296, 296))
        );

        tabSubTask.addTab("Ternination", jPanel16);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Insert Notification Template", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel81.setText("Template Name");

        jLabel82.setText("Description");

        jLabel83.setText("Subject");

        jLabel84.setText("Email");

        taDescription.setColumns(20);
        taDescription.setRows(5);
        jScrollPane11.setViewportView(taDescription);

        btAddNotificationTemplate.setText("Add");
        btAddNotificationTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddNotificationTemplateActionPerformed(evt);
            }
        });

        btDeleteNotify.setText("Delete");

        btUpdateNotify.setText("Update");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel81)
                            .addComponent(jLabel83)
                            .addComponent(jLabel84)
                            .addComponent(jLabel82))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTemplateName, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                .addComponent(txtSubject))
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbEmailNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(btAddNotificationTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btDeleteNotify)
                        .addGap(38, 38, 38)
                        .addComponent(btUpdateNotify)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(txtTemplateName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(cbEmailNotification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel82)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btAddNotificationTemplate)
                    .addComponent(btDeleteNotify)
                    .addComponent(btUpdateNotify))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(jTable1);

        tbNotification.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Template Name", "Subject", "Email", "Descript"
            }
        ));
        jScrollPane12.setViewportView(tbNotification);
        notificationModel = (javax.swing.table.DefaultTableModel)this.tbNotification.getModel();

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane12)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(526, 526, 526)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(3573, 3573, 3573))
        );

        tabSubTask.addTab("Notification", jPanel18);

        javax.swing.GroupLayout TaskPanelLayout = new javax.swing.GroupLayout(TaskPanel);
        TaskPanel.setLayout(TaskPanelLayout);
        TaskPanelLayout.setHorizontalGroup(
            TaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabSubTask, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        TaskPanelLayout.setVerticalGroup(
            TaskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabSubTask, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        mainTabPane.addTab("Task", new javax.swing.ImageIcon(getClass().getResource("/icons/task.png")), TaskPanel); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Apartment Number");

        jLabel2.setText("Size");

        jLabel3.setText("Apartment Type");

        jLabel4.setText("Rental Fee");

        jLabel5.setText("Building Name");

        btnRent.setText("Rent");
        btnRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtApartmentNumberRent)
                    .addComponent(txtApartmentTypeRent)
                    .addComponent(txtBuildingNameRent, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                .addGap(71, 71, 71)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApartmentSizeRent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFeeRent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(325, 325, 325)
                .addComponent(btnRent, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtApartmentNumberRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtApartmentSizeRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtApartmentTypeRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtFeeRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtBuildingNameRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(btnRent)
                .addContainerGap())
        );

        rowApartmentListener = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                setApartmentValueToForm();
            }
        };
        tbApartmentRent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Apartment Number", "Size", "Apartment Type", "Rental Fee", "BuildingName"
            }
        ));
        apartmentModel = (javax.swing.table.DefaultTableModel)this.tbApartmentRent.getModel();
        jScrollPane2.setViewportView(tbApartmentRent);
        tbApartmentRent.getSelectionModel().addListSelectionListener(rowApartmentListener);

        javax.swing.GroupLayout panelChoseApartmentLayout = new javax.swing.GroupLayout(panelChoseApartment);
        panelChoseApartment.setLayout(panelChoseApartmentLayout);
        panelChoseApartmentLayout.setHorizontalGroup(
            panelChoseApartmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChoseApartmentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelChoseApartmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        panelChoseApartmentLayout.setVerticalGroup(
            panelChoseApartmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChoseApartmentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabRenNewApartment.addTab("Chose Apartment", panelChoseApartment);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Tenant ID");

        jLabel7.setText("Password");

        jLabel8.setText("Firstname");

        jLabel9.setText("Lastname");

        jLabel10.setText("Phone");

        jLabel11.setText("Email");

        jLabel12.setText("Address");

        jLabel13.setText("State Zip");

        tbnRegistration.setText("Registration");
        tbnRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnRegistrationActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(54, 54, 54)
                                .addComponent(txtTenantID, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPhone)
                                    .addComponent(txtAddress)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel11))
                                    .addComponent(jLabel8))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFirstname)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtLastname, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtEmail)
                                    .addComponent(txtStateZip, javax.swing.GroupLayout.Alignment.TRAILING)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(267, 267, 267)
                        .addComponent(tbnRegistration)
                        .addGap(31, 31, 31)
                        .addComponent(btnReset)))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTenantID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtStateZip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)
                    .addComponent(tbnRegistration))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout panelTenantRegistrationLayout = new javax.swing.GroupLayout(panelTenantRegistration);
        panelTenantRegistration.setLayout(panelTenantRegistrationLayout);
        panelTenantRegistrationLayout.setHorizontalGroup(
            panelTenantRegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTenantRegistrationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTenantRegistrationLayout.setVerticalGroup(
            panelTenantRegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTenantRegistrationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabRenNewApartment.addTab("Tenant Registration", panelTenantRegistration);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setText("Lease ID");

        jLabel15.setText("Start Date");

        btnCreateLease.setText("Create Lease");
        btnCreateLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateLeaseActionPerformed(evt);
            }
        });

        jdcStartDate.setDateFormatString("dd/MM/yyyy");
        jdcStartDate.setDate(new java.util.Date());

        jLabel16.setText("End Date");

        jdcEndDate.setDateFormatString("dd/MM/yyyy");
        jdcEndDate.setDate(new java.util.Date());

        jLabel17.setText("Balance");

        jLabel18.setText("Security Deposit");

        jLabel19.setText("Rental Date");

        jLabel20.setText("Tenant ID");

        jLabel21.setText("Apartment Number");

        jdcRentDate.setDateFormatString("dd/MM/yyyy");
        jdcRentDate.setDate(new java.util.Date());

        jLabel104.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(255, 0, 0));
        jLabel104.setText("Note: This is username which you will  login to AMS System");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel14))
                                .addGap(63, 63, 63)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtLeaseID)
                                    .addComponent(jdcStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel104))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel21))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBalance)
                                    .addComponent(jdcEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                    .addComponent(txtSecurityDeposit)
                                    .addComponent(txtTenantIDLease)
                                    .addComponent(txtApartmentNumberLease)
                                    .addComponent(jdcRentDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(btnCreateLease)))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLeaseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel104))))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel15)
                    .addComponent(jdcStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jdcEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtSecurityDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19)
                    .addComponent(jdcRentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtTenantIDLease, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtApartmentNumberLease, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addComponent(btnCreateLease)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCreateleaseLayout = new javax.swing.GroupLayout(panelCreatelease);
        panelCreatelease.setLayout(panelCreateleaseLayout);
        panelCreateleaseLayout.setHorizontalGroup(
            panelCreateleaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCreateleaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCreateleaseLayout.setVerticalGroup(
            panelCreateleaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCreateleaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabRenNewApartment.addTab("Create Lease", panelCreatelease);

        panelPayment.setLayout(new java.awt.BorderLayout());

        cbPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash Payment", "Check Payment", "CreditCard Payment" }));
        cbPaymentMethod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbPaymentMethodItemStateChanged(evt);
            }
        });

        jLabel23.setText("Pay ID");

        jLabel24.setText("Pay Date");

        txtPayDate.setDateFormatString("dd/MM/yyyy");
        txtPayDate.setDate(new java.util.Date());

        jLabel25.setText("Pay Amount");

        jLabel26.setText("Pay Method");

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPayAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPayID, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(446, Short.MAX_VALUE))
                    .addGroup(controlPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtPayID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtPayAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        panelPayment.add(controlPanel, java.awt.BorderLayout.PAGE_START);

        displayPanel.setLayout(new java.awt.CardLayout());

        btnCashPayment.setText("Payment");
        btnCashPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashPaymentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CashPaymentLayout = new javax.swing.GroupLayout(CashPayment);
        CashPayment.setLayout(CashPaymentLayout);
        CashPaymentLayout.setHorizontalGroup(
            CashPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CashPaymentLayout.createSequentialGroup()
                .addGap(392, 392, 392)
                .addComponent(btnCashPayment)
                .addContainerGap(328, Short.MAX_VALUE))
        );
        CashPaymentLayout.setVerticalGroup(
            CashPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CashPaymentLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnCashPayment)
                .addContainerGap(254, Short.MAX_VALUE))
        );

        displayPanel.add(CashPayment, "Cash Payment");

        CheckPayment.setPreferredSize(new java.awt.Dimension(934, 200));

        jLabel27.setText("Bank Name");

        jLabel28.setText("Check Number");

        btnCheckPayment.setText("Payment");
        btnCheckPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckPaymentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CheckPaymentLayout = new javax.swing.GroupLayout(CheckPayment);
        CheckPayment.setLayout(CheckPaymentLayout);
        CheckPaymentLayout.setHorizontalGroup(
            CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CheckPaymentLayout.createSequentialGroup()
                .addGroup(CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CheckPaymentLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCheckNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CheckPaymentLayout.createSequentialGroup()
                        .addGap(392, 392, 392)
                        .addComponent(btnCheckPayment)))
                .addContainerGap(328, Short.MAX_VALUE))
        );
        CheckPaymentLayout.setVerticalGroup(
            CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CheckPaymentLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(CheckPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtCheckNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnCheckPayment)
                .addContainerGap(181, Short.MAX_VALUE))
        );

        displayPanel.add(CheckPayment, "Check Payment");

        jLabel29.setText("Holder Name");

        jLabel30.setText("Exp Date");

        cbExpDate.setDate(new java.util.Date());

        jLabel31.setText("Credit Card Type");

        jLabel32.setText("Credit Card Number");

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cclogos.jpg"))); // NOI18N

        btnCreditCardPayment.setText("Payment");
        btnCreditCardPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreditCardPaymentActionPerformed(evt);
            }
        });

        cbCreditCardType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Visa", "Master", "American Express" }));

        javax.swing.GroupLayout CreditCardPaymentLayout = new javax.swing.GroupLayout(CreditCardPayment);
        CreditCardPayment.setLayout(CreditCardPaymentLayout);
        CreditCardPaymentLayout.setHorizontalGroup(
            CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                        .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel29)
                                .addGap(14, 14, 14)
                                .addComponent(txtHolderName, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel31)
                                .addGap(18, 18, 18)
                                .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33)
                                    .addComponent(cbCreditCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(120, 120, 120)
                        .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel30))
                        .addGap(56, 56, 56)
                        .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbExpDate, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCreditCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                        .addGap(382, 382, 382)
                        .addComponent(btnCreditCardPayment)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CreditCardPaymentLayout.setVerticalGroup(
            CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbExpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CreditCardPaymentLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel29))
                    .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHolderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30)))
                .addGap(18, 18, 18)
                .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CreditCardPaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(cbCreditCardType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel32)
                    .addComponent(txtCreditCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addComponent(btnCreditCardPayment)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        displayPanel.add(CreditCardPayment, "CreditCard Payment");

        panelPayment.add(displayPanel, java.awt.BorderLayout.CENTER);

        tabRenNewApartment.addTab("Payment", panelPayment);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setText("RentID");

        jLabel35.setText("Rental Fee");

        jLabel36.setText("Late Fee");

        jLabel37.setText("Date");

        jLabel38.setText("Date To Pay");

        jcdDateToPay.setDateFormatString("dd/MM/yyyy");
        jcdDateToPay.setDate(new java.util.Date());

        jcdDate.setDateFormatString("dd/MM/yyyy");
        jcdDate.setDate(new java.util.Date());

        jLabel39.setText("Lease ID");

        jLabel40.setText("Pay ID");

        btnRentNewApartment.setText("Rent");
        btnRentNewApartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentNewApartmentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel38)
                                    .addGap(18, 18, 18)
                                    .addComponent(jcdDateToPay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel34))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtLateFee, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtRentID, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel40))
                            .addComponent(txtPayIDRent, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel39)
                            .addComponent(jLabel35))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcdDate, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                            .addComponent(txtRentFeeRent)
                            .addComponent(txtLeaseIDRent)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(btnRentNewApartment, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtRentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(txtRentFeeRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel36)
                                .addComponent(txtLateFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jcdDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel37))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel38)
                    .addComponent(jcdDateToPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel39)
                        .addComponent(txtLeaseIDRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txtPayIDRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnRentNewApartment)
                .addContainerGap(268, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabRenNewApartment.addTab("Rent", jPanel7);

        panelReport.setBorder(javax.swing.BorderFactory.createTitledBorder("Final Report"));

        jLabel62.setText("Tenant ID");

        jLabel63.setText("Lastname");

        jLabel64.setText("Firstname");

        jLabel65.setText("Email");

        jLabel66.setText("Zip Code");

        jLabel67.setText("Phone");

        jLabel68.setText("Address");

        jLabel69.setText("Lease ID");

        jLabel70.setText("Start Dat e");

        jLabel71.setText("End Date");

        jLabel72.setText("Balance");

        jLabel73.setText("Security Deposit");

        jLabel74.setText("Rental Date");

        jLabel75.setText("Apartment Number");

        jLabel76.setText("Termination ID");

        btnFinalPrint.setText("CLose");
        btnFinalPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalPrintActionPerformed(evt);
            }
        });

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(0, 0, 255));
        jLabel77.setText("Tenant Contract Details");

        btnPrintTenantDetails.setText("Print Tenant Details");
        btnPrintTenantDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintTenantDetailsActionPerformed(evt);
            }
        });

        btnPrintLeaseDetails.setText("Print Leae Details");
        btnPrintLeaseDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLeaseDetailsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReportLayout.createSequentialGroup()
                .addGap(265, 265, 265)
                .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addGap(263, 263, 263))
            .addGroup(panelReportLayout.createSequentialGroup()
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63)
                            .addComponent(jLabel64)
                            .addComponent(jLabel65)
                            .addComponent(jLabel66)
                            .addComponent(jLabel67)
                            .addComponent(jLabel68))
                        .addGap(52, 52, 52)
                        .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFinalTenantID)
                            .addComponent(txtFinalLastname)
                            .addComponent(txtFinalFirstname)
                            .addComponent(txtFinalEmail)
                            .addComponent(txtZipCode)
                            .addComponent(txtFinalPhone)
                            .addComponent(txtFinalAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(btnPrintTenantDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelReportLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelReportLayout.createSequentialGroup()
                                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel70)
                                    .addComponent(jLabel69)
                                    .addComponent(jLabel71)
                                    .addComponent(jLabel72))
                                .addGap(56, 56, 56)
                                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtFinalLeaseID, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                    .addComponent(txtFinalStartDate)
                                    .addComponent(txtFinalEndDate)
                                    .addComponent(txtFinalBalance)))
                            .addGroup(panelReportLayout.createSequentialGroup()
                                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel73)
                                    .addComponent(jLabel74))
                                .addGap(31, 31, 31)
                                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFinalSecurityDeposit)
                                    .addComponent(txtFinalRentalDate)))
                            .addGroup(panelReportLayout.createSequentialGroup()
                                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel75)
                                    .addComponent(jLabel76))
                                .addGap(18, 18, 18)
                                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFinalTerminationID)
                                    .addComponent(txtFinalApartmentNumber))))
                        .addContainerGap())
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFinalPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrintLeaseDetails)
                        .addGap(120, 120, 120))))
        );
        panelReportLayout.setVerticalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jLabel77)
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(txtFinalTenantID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69)
                    .addComponent(txtFinalLeaseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(txtFinalLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70)
                    .addComponent(txtFinalStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(txtFinalFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71)
                    .addComponent(txtFinalEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(txtFinalEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel72)
                    .addComponent(txtFinalBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(txtZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73)
                    .addComponent(txtFinalSecurityDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(txtFinalPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74)
                    .addComponent(txtFinalRentalDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(txtFinalAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel75)
                    .addComponent(txtFinalApartmentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(txtFinalTerminationID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFinalPrint)
                    .addComponent(btnPrintTenantDetails)
                    .addComponent(btnPrintLeaseDetails))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabRenNewApartment.addTab("Fianal Report", jPanel1);

        javax.swing.GroupLayout PaymentPanelLayout = new javax.swing.GroupLayout(PaymentPanel);
        PaymentPanel.setLayout(PaymentPanelLayout);
        PaymentPanelLayout.setHorizontalGroup(
            PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabRenNewApartment, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        PaymentPanelLayout.setVerticalGroup(
            PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabRenNewApartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        mainTabPane.addTab("Rent", new javax.swing.ImageIcon(getClass().getResource("/icons/1341561520_apartment.png")), PaymentPanel); // NOI18N

        panelAccount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registration new Landlord", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel25.setForeground(new java.awt.Color(51, 51, 51));

        jLabel85.setText("Landlord ID");

        jLabel87.setText("Password");

        btnLandlordRegistration.setText("Register");
        btnLandlordRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLandlordRegistrationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel85)
                    .addComponent(jLabel87))
                .addGap(33, 33, 33)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtLandlordIDManager, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(txtLandlordPasswordManager))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .addComponent(btnLandlordRegistration)
                .addGap(142, 142, 142))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(txtLandlordIDManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(txtLandlordPasswordManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnLandlordRegistration)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        rowlandlordPasswordManager = new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setValueToLandlordProfile();
            }
        };
        tbLandlordManager.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbLandlordManager.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Landlord ID", "Password"
            }
        ));
        landlordModel = (javax.swing.table.DefaultTableModel)this.tbLandlordManager.getModel();
        tbLandlordManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLandlordManagerMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tbLandlordManager);
        tbLandlordManager.getSelectionModel().addListSelectionListener(rowlandlordPasswordManager);

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Landlord Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel89.setText("Number of building");

        jLabel90.setText("Number of Aprtment");

        jLabel91.setText("Rent");

        jLabel92.setText("Not Rent");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel89)
                    .addComponent(jLabel90)
                    .addComponent(jLabel91)
                    .addComponent(jLabel92))
                .addGap(25, 25, 25)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNumBuildManager)
                    .addComponent(txtNumApartmentManager)
                    .addComponent(txtNumRentManager)
                    .addComponent(txtNumNotRentManager, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(txtNumBuildManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(txtNumApartmentManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(txtNumRentManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(txtNumNotRentManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Landlord", jPanel24);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane13.setViewportView(jTable3);

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tenant Information Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane3.addTab("Tenant", jPanel26);

        javax.swing.GroupLayout panelAccountLayout = new javax.swing.GroupLayout(panelAccount);
        panelAccount.setLayout(panelAccountLayout);
        panelAccountLayout.setHorizontalGroup(
            panelAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        panelAccountLayout.setVerticalGroup(
            panelAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        mainTabPane.addTab("Account", new javax.swing.ImageIcon(getClass().getResource("/images/user_male_white_blue_blonde.png")), panelAccount); // NOI18N

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        taManagerLog.setColumns(20);
        taManagerLog.setRows(5);
        jScrollPane1.setViewportView(taManagerLog);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 912, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(
                txtApartmentNumberRent,
                txtApartmentSizeRent,
                txtApartmentTypeRent,
                txtFeeRent,
                txtBuildingNameRent)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a apartment in below table");
            return;
        }
        this.tabRenNewApartment.setSelectedIndex(1);
        this.tabRenNewApartment.setEnabledAt(1, true);
        apartment.setAptNumber(this.txtApartmentNumberRent.getText());
        apartment.setPrice(Double.valueOf(this.txtFeeRent.getText()));
    }//GEN-LAST:event_btnRentActionPerformed

    private void tbnRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnRegistrationActionPerformed
        //Check empty field
        if (!utils.FormChecker.checkEmptyTextFields(
                txtTenantID, txtPassword, txtFirstname, txtLastname, txtPhone, txtEmail, txtAddress, txtStateZip)) {
            javax.swing.JOptionPane.showMessageDialog(this, "All fields cannot empty !");
            return;
        }
        if (!utils.FormChecker.checkEmail(txtEmail)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invaid email address!");
            return;
        }
        //Create Tenant information
        tenant.setID(this.txtTenantID.getText());
        tenant.setPassword(utils.Encrypt.enPass(String.valueOf(this.txtPassword.getPassword())));
        tenant.setFirstname(this.txtFirstname.getText());
        tenant.setLastname(this.txtFirstname.getText());
        tenant.setPhone(this.txtPhone.getText());
        tenant.setEmail(this.txtEmail.getText());
        tenant.setCurrentAddress(this.txtAddress.getText());
        tenant.setCityStateZip(this.txtStateZip.getText());
        try {
            if (th.checkTenantExist(tenant)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Tenant " + tenant.getID() + " has been exist");
                return;
            }
            boolean result = th.insertTenant(tenant);
            if (result) {
                javax.swing.JOptionPane.showMessageDialog(this, "Registration successful!");
                this.tabRenNewApartment.setSelectedIndex(2);
                this.tabRenNewApartment.setEnabledAt(2, true);
                this.txtApartmentNumberLease.setText(apartment.getAptNumber());
                this.taManagerLog.append("Tenant Registration successful!");
                this.txtTenantIDLease.setText(tenant.getID());//set tenant ID to field of the next tab
                this.txtApartmentNumberLease.setText(apartment.getAptNumber());
                //set LeaseID is current time random
                Date leaseIDRandom = new Date();
                long currTime = leaseIDRandom.getTime();
                this.txtLeaseID.setText(String.valueOf(currTime));
            }

        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_tbnRegistrationActionPerformed

    private void btnCreateLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateLeaseActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(txtLeaseID, txtBalance, txtSecurityDeposit)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter complete empty fields");
            return;
        }
        if(utils.FormChecker.checkStartAndEndDate(this.jdcStartDate.getDate(), this.jdcEndDate.getDate())){
            utils.ShowMessage.showMessageDialog(this, "Please choose end date ! End date cannot equals start date");
            return;
        }
        lease.setLeaseID(this.txtLeaseID.getText());
        lease.setStartDate(utils.ConvertDate.convertToSqlDate(this.jdcStartDate.getDate()));
        lease.setEndDate(utils.ConvertDate.convertToSqlDate(this.jdcEndDate.getDate()));
        lease.setBalance(Double.valueOf(this.txtBalance.getText()));
        lease.setSecurityDeposit(Double.valueOf(this.txtSecurityDeposit.getText()));
        lease.setRentalDate(utils.ConvertDate.convertToSqlDate(this.jdcRentDate.getDate()));
        lease.setTenant(tenant);
        lease.setApartment(apartment);
        termination.setTerminationID(this.txtLeaseID.getText());
        termination.setLeavingDate(utils.ConvertDate.convertToSqlDate(this.jdcEndDate.getDate()));
        termination.setLeavingReason("End Of Contract");
        lease.setTermination(termination);
        try {
            boolean terInsert = terHandler.inserTermination(termination);
            if (terInsert) {
                boolean result = lh.insertLease(lease);
                if (result) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Insert new Lease successful!");
                    this.tabRenNewApartment.setSelectedIndex(3);
                    this.tabRenNewApartment.setEnabledAt(3, true);
                    Date payID = new Date();
                    long payIDRandom = payID.getTime();
                    this.txtPayID.setText(String.valueOf(payIDRandom));
                    double payAmount = lease.getSecurityDeposit() + apartment.getPrice();
                    this.txtPayAmount.setText(String.valueOf(payAmount));
                }
            }

        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }


    }//GEN-LAST:event_btnCreateLeaseActionPerformed

    private void cbPaymentMethodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbPaymentMethodItemStateChanged
        java.awt.CardLayout cards = (java.awt.CardLayout) this.displayPanel.getLayout();
        cards.show(displayPanel, (String) evt.getItem());
    }//GEN-LAST:event_cbPaymentMethodItemStateChanged

    private void btnCashPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashPaymentActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(txtPayID, txtPayAmount)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter empty fiels!");
            return;
        }

        //This method process cash payment
        cash.setPayID(this.txtPayID.getText());
        cash.setPayDate(utils.ConvertDate.convertToSqlDate(this.txtPayDate.getDate()));
        cash.setPayAmount(Double.valueOf(this.txtPayAmount.getText()));
        cash.setPayMethod(this.cbPaymentMethod.getSelectedItem().toString());
        try {
            boolean result = ph.insertPayment(cash);
            if (result) {
                ph.insertCashID(cash);
                javax.swing.JOptionPane.showMessageDialog(this, "Process Cash payment successful\n");
                this.taManagerLog.append("Transact Successful...\n");
                this.tabRenNewApartment.setEnabledAt(4, true);
                this.tabRenNewApartment.setSelectedIndex(4);
                this.txtPayIDRent.setText(cash.getPayID());
                this.txtLeaseIDRent.setText(lease.getLeaseID());
                this.txtRentFeeRent.setText(apartment.getPrice().toString());
                this.txtLateFee.setText(cash.getPayAmount().toString());                
                paymentMethod = this.cash.getPayMethod();
                Date rentID = new Date();
                long rentIDRandom = rentID.getTime();
                this.txtRentID.setText(String.valueOf(rentIDRandom));
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_btnCashPaymentActionPerformed

    private void btnCreditCardPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreditCardPaymentActionPerformed
        //check empty form
        credit.setPayID(this.txtPayID.getText());
        credit.setPayAmount(Double.valueOf(this.txtPayAmount.getText()));
        credit.setPayDate(utils.ConvertDate.convertToSqlDate(this.txtPayDate.getDate()));
        credit.setPayMethod(this.cbPaymentMethod.getSelectedItem().toString());
        credit.setHolderName(this.txtHolderName.getText());
        credit.setExpDate(utils.ConvertDate.convertToSqlDate(this.cbExpDate.getDate()));
        credit.setCcType(this.cbCreditCardType.getSelectedItem().toString());
        credit.setCcNumber(this.txtCreditCardNumber.getText());
        try {
            boolean result = ph.insertPayment(credit);
            if (result) {
                ph.insertCreditCardPayment(credit);
                javax.swing.JOptionPane.showMessageDialog(this, "Process payment successful !\n");
                this.tabRenNewApartment.setSelectedIndex(4);
                this.txtPayIDRent.setText(credit.getPayID());
                this.txtLeaseIDRent.setText(lease.getLeaseID());
                this.txtRentFeeRent.setText(apartment.getPrice().toString());
                this.txtLateFee.setText(credit.getPayAmount().toString());
                this.tabRenNewApartment.setEnabledAt(4, true);
                paymentMethod = credit.getPayMethod();
                Date rentID = new Date();
                long rentIDRandom = rentID.getTime();
                this.txtRentID.setText(String.valueOf(rentIDRandom));
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }
        //This method process Credit card payment
    }//GEN-LAST:event_btnCreditCardPaymentActionPerformed

    private void btnCheckPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckPaymentActionPerformed
        check.setPayID(this.txtPayID.getText());
        check.setPayAmount(Double.valueOf(this.txtPayAmount.getText()));
        check.setPayDate(utils.ConvertDate.convertToSqlDate(this.txtPayDate.getDate()));
        check.setPayMethod(this.cbPaymentMethod.getSelectedItem().toString());
        check.setBankName(this.txtBankName.getText());
        check.setCheckNumber(this.txtCheckNumber.getText());
        try {
            boolean result = ph.insertPayment(check);
            if (result) {
                ph.insertCheckPayment(check);
                this.tabRenNewApartment.setEnabledAt(4, true);
                javax.swing.JOptionPane.showMessageDialog(this, "Process Check Payment successful!");
                this.txtRentFeeRent.setText(apartment.getPrice().toString());
                this.txtLateFee.setText(check.getPayAmount().toString());
                this.tabRenNewApartment.setSelectedIndex(4);
                paymentMethod = check.getPayMethod();
                Date rentID = new Date();
                long rentIDRandom = rentID.getTime();
                this.txtRentID.setText(String.valueOf(rentIDRandom));
                this.txtPayIDRent.setText(check.getPayID());
                this.txtLeaseIDRent.setText(lease.getLeaseID());
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }

    }//GEN-LAST:event_btnCheckPaymentActionPerformed

    private void btnRentNewApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentNewApartmentActionPerformed
        rent.setID(this.txtRentID.getText());
        rent.setPrice(Double.valueOf(this.txtRentFeeRent.getText()));
        rent.setLateFee(Double.valueOf(this.txtLateFee.getText()));
        rent.setDate(utils.ConvertDate.convertToSqlDate(this.jdcRentDate.getDate()));
        rent.setDateToPay(utils.ConvertDate.convertToSqlDate(this.txtPayDate.getDate()));
        rent.setLease(lease);
        if (paymentMethod.equals("Cash Payment")) {
            rent.setPay(cash);
        } else if (paymentMethod.equals("Check Payment")) {
            rent.setPay(check);
        } else if (paymentMethod.equals("CreditCard Payment")) {
            rent.setPay(credit);
        }
        try {
            boolean result = ph.insertRent(rent);
            if (result) {
                javax.swing.JOptionPane.showMessageDialog(this, "congratulation ! Rent successful !");
                this.tabRenNewApartment.setSelectedIndex(5);
                //set value to final report form
                this.txtFinalTenantID.setText(tenant.getID());
                this.txtFinalLastname.setText(tenant.getLastname());
                this.txtFinalFirstname.setText(tenant.getFirstname());
                this.txtFinalEmail.setText(tenant.getEmail());
                this.txtZipCode.setText(tenant.getCityStateZip());
                this.txtFinalPhone.setText(tenant.getPhone());
                this.txtFinalAddress.setText(tenant.getCurrentAddress());
                //set value to lease report form
                this.txtFinalLeaseID.setText(lease.getLeaseID());
                this.txtFinalStartDate.setText(lease.getStartDate().toString());
                this.txtFinalEndDate.setText(lease.getEndDate().toString());
                this.txtFinalBalance.setText(lease.getBalance().toString());
                this.txtFinalSecurityDeposit.setText(lease.getSecurityDeposit().toString());
                this.txtFinalRentalDate.setText(lease.getRentalDate().toString());
                this.txtFinalApartmentNumber.setText(lease.getApartment().getAptNumber());
                this.txtFinalTerminationID.setText(lease.getTermination().getTerminationID());
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_btnRentNewApartmentActionPerformed

    private void btnRequestChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRequestChangeActionPerformed
        apartment.setAptNumber(this.txtApartmentNumberNew.getText());
        lease.setApartment(apartment);
        try {
            boolean result = lh.updateLeaseApartmentNumber(lease);
            if (result) {
                javax.swing.JOptionPane.showMessageDialog(this, "Confirm change apartment successful!");
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_btnRequestChangeActionPerformed

    private void btnFinalPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalPrintActionPerformed
    }//GEN-LAST:event_btnFinalPrintActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        termination.setTerminationID(this.txtTerminationTerID.getText());
        termination.setLeavingDate(utils.ConvertDate.convertToSqlDate(this.jdcLeavingDateTer.getDate()));
        termination.setLeavingReason(this.txtLeavingReasonTer.getText());
        try {
            boolean result = terHandler.inserTermination(termination);
            if (result) {
                javax.swing.JOptionPane.showMessageDialog(this, "Insert Termination successful!");
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnPrintTenantDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintTenantDetailsActionPerformed
        jasperParameter = new HashMap();
        jasperParameter.put("tenantIDParam", this.txtTenantID.getText());
        try {
            utils.Resource resource = new utils.Resource();
            jasperPrint = JasperFillManager.fillReport(resource.getInputStream("TenantProfile.jasper"), jasperParameter, db.DBConnect());
            JRViewer viewer = new JRViewer(jasperPrint);
            tabRenNewApartment.addTab("Tenant Details", viewer);
            this.tabRenNewApartment.setSelectedIndex(this.tabRenNewApartment.getTabCount() - 1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPrintTenantDetailsActionPerformed

    private void btnPrintLeaseDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLeaseDetailsActionPerformed
        jasperParameter = new HashMap();
        jasperParameter.put("leaseIDParam", this.txtLeaseID.getText());
        try {
            utils.Resource resource = new utils.Resource();
            jasperPrint = JasperFillManager.fillReport(resource.getInputStream("Lease.jasper"), jasperParameter, db.DBConnect());
            JRViewer viewer = new JRViewer(jasperPrint);
            tabRenNewApartment.addTab("Print Lease Information", viewer);
            this.tabRenNewApartment.setSelectedIndex(this.tabRenNewApartment.getTabCount() - 1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPrintLeaseDetailsActionPerformed

    private void btnLandlordRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLandlordRegistrationActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(this.txtLandlordIDManager, this.txtLandlordPasswordManager)) {
            utils.ShowMessage.showMessageDialog(this, "Pleasae enter empty fields !");
            return;
        }
        if (!utils.FormChecker.checkPasswordField(this.txtLandlordPasswordManager)) {
            utils.ShowMessage.showMessageDialog(this, "Password not less than 6 character !");
            return;
        }
        Landlord landlord = new Landlord();
        landlord.setID(this.txtLandlordIDManager.getText());
        try {
            landlord.setPassword(utils.Encrypt.enPass(String.valueOf(this.txtLandlordPasswordManager.getPassword())));
            if (landlordHandler.checkLandlordExist(landlord)) {
                utils.ShowMessage.showMessageDialog(this, "This landlord has exist!");
                return;
            }
            boolean result = landlordHandler.insertLandlord(landlord);
            if (result) {
                utils.ShowMessage.showMessageDialog(this, "Register new landlord successful !");
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        }
    }//GEN-LAST:event_btnLandlordRegistrationActionPerformed

    private void mainTabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainTabPaneMouseClicked
        if (this.mainTabPane.getSelectedIndex() == 1) {
            try {
                Vector vEmail = landlordHandler.getTenantEmail();
                this.cbEmailNotification.setModel(new javax.swing.DefaultComboBoxModel(vEmail));

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (this.mainTabPane.getSelectedIndex() == 3) {
            try {
                Vector vLandlord = landlordHandler.getAllVectorLandlord();

            } catch (ClassNotFoundException ex) {
                this.taManagerLog.append("" + ex.getMessage());
            } catch (SQLException ex) {
                this.taManagerLog.append("" + ex.getMessage());
            }
        }
    }//GEN-LAST:event_mainTabPaneMouseClicked

    private void btUpdateTerminationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateTerminationActionPerformed
        termination.setTerminationID(this.txtTerminationTerID.getText());
        termination.setLeavingDate(utils.ConvertDate.convertToSqlDate(this.jdcLeavingDateTer.getDate()));
        termination.setLeavingReason(this.txtLeavingReasonTer.getText());
        try {
            boolean result = terHandler.updateTermination(termination);
            if (result) {
                utils.ShowMessage.showMessageDialog(this, "Update Termination successful!");
                utils.ListenerManager.disableListenerOnTable(this.tbTermination, terRowListener);
                this.tbTermination.repaint();
                this.loadTerminationTable();
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbTermination, terRowListener);
        }
    }//GEN-LAST:event_btUpdateTerminationActionPerformed

    private void btDeleteTerminaitonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteTerminaitonActionPerformed
    }//GEN-LAST:event_btDeleteTerminaitonActionPerformed

    private void btAddNotificationTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddNotificationTemplateActionPerformed
        NotificationTemplate note = new NotificationTemplate();
        note.setTemplateName(this.txtTemplateName.getText());
        note.setSubject(this.txtSubject.getText());
        note.setEmailAddress(this.cbEmailNotification.getSelectedItem().toString());
        note.setDesScription(this.taDescription.getText());

    }//GEN-LAST:event_btAddNotificationTemplateActionPerformed

    private void cashBtnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashBtnViewActionPerformed
    }//GEN-LAST:event_cashBtnViewActionPerformed

    private void tbLandlordManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbLandlordManagerMouseClicked
        this.btnLandlordRegistration.setEnabled(false);
    }//GEN-LAST:event_tbLandlordManagerMouseClicked

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(txtMRApartmentNumber, txtMRMaintenanceID, txtMRProblem, txtMRFeedbackDate, txtMRRating)) {
            utils.ShowMessage.showMessageDialog(this, "Please enter empty text fields");
            return;
        }
        try {
            apartment.setAptNumber(this.txtMRApartmentNumber.getText());
            acc.setApartment(apartment);
            acc.setMaintenanceID(this.txtMRMaintenanceID.getText());
            acc.setProblem(this.txtMRProblem.getText());
            acc.setFeedbackDate(utils.ConvertDate.convertToSqlDate(utils.ConvertDate.convertStringToDate(this.txtMaintenanceDate.getText())));
            acc.setRating(Double.valueOf(this.txtMRRating.getText()));
            boolean result = accHandler.insertAccidentalMaintenance(acc);
            if (result) {
                utils.ShowMessage.showMessageDialog(this, "Insert accidental rating successful !");
                utils.ListenerManager.disableListenerOnTable(this.tbMaintenanceRating, rowMaintenancRatingListener);
                this.loadAccidentalMaintenanceTable();
            }
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (ParseException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbMaintenanceRating, rowMaintenancRatingListener);
        }
    }//GEN-LAST:event_btAddActionPerformed

    private void tabSubTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabSubTaskMouseClicked
        if (this.tabSubTask.getSelectedIndex() == 2) {
            if (this.tbMaintenanceRequest.getSelectedRow() < 0) {
                try {
                    this.loadMaintenanceTable();
                } catch (ClassNotFoundException ex) {
                    this.taManagerLog.append("\n" + ex.getMessage());
                } catch (SQLException ex) {
                    this.taManagerLog.append("\n" + ex.getMessage());
                }
            }
        }
        if (this.tabSubTask.getSelectedIndex() == 3) {
        }
    }//GEN-LAST:event_tabSubTaskMouseClicked

    private void tbMaintenanceRatingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMaintenanceRatingMouseClicked
        this.btAdd.setEnabled(false);
        this.txtMRApartmentNumber.setEditable(false);
        this.txtMRMaintenanceID.setEditable(false);
    }//GEN-LAST:event_tbMaintenanceRatingMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.resetMaintenanceRatingForm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btResetMaintenanceRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetMaintenanceRequestActionPerformed
        this.resetMaintenanceForm();
    }//GEN-LAST:event_btResetMaintenanceRequestActionPerformed

    private void btTurnOnRatingTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTurnOnRatingTabActionPerformed
        this.tabSubTask.setEnabledAt(3, true);
    }//GEN-LAST:event_btTurnOnRatingTabActionPerformed

    private void btTurnOffRatingTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTurnOffRatingTabActionPerformed
        this.tabSubTask.setEnabledAt(3, false);
    }//GEN-LAST:event_btTurnOffRatingTabActionPerformed

    private void btUpdateMaintenanceRatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateMaintenanceRatingActionPerformed
        try {
            //update new Acc
            AccidentalMaintenance accUpdate = new AccidentalMaintenance();
            accUpdate.setProblem(this.txtMRProblem.getText());
            accUpdate.setFeedbackDate(utils.ConvertDate.convertToSqlDate(utils.ConvertDate.convertStringToDate(this.txtMRFeedbackDate.getText())));
            accUpdate.setRating(Double.valueOf(this.txtMRRating.getText()));
            apartment.setAptNumber(this.txtMRApartmentNumber.getText());
            accUpdate.setApartment(apartment);
            accUpdate.setMaintenanceID(this.txtMRMaintenanceID.getText());
            boolean result = accHandler.updateAccidentalMaintenance(accUpdate);
            if (result) {
                utils.ShowMessage.showMessageDialog(this, "Update successful !");
                utils.ListenerManager.disableListenerOnTable(this.tbMaintenanceRating, rowMaintenancRatingListener);
                this.loadAccidentalMaintenanceTable();
            }
        } catch (ParseException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbMaintenanceRating, rowMaintenancRatingListener);
        }
    }//GEN-LAST:event_btUpdateMaintenanceRatingActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        utils.ListenerManager.disableListenerOnTable(this.tbMaintenanceRequest, rowMaintenanceListener);
        try {
            this.loadMaintenanceTable();
        } catch (ClassNotFoundException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taManagerLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbMaintenanceRequest, rowMaintenanceListener);
        }
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btDeleteMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteMaintenanceActionPerformed
        Maintenance deleteIt = new Maintenance();
        apartment.setAptNumber(this.txtApartmentNo.getText());
        deleteIt.setApartment(apartment);
        deleteIt.setMaintenanceID(this.txtMaintenanceID.getText());
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure", "Delete Maintenance", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (confirm == javax.swing.JOptionPane.OK_OPTION) {
            try {

                boolean result = mh.deleteMaintenance(deleteIt);
                if (result) {
                    utils.ShowMessage.showMessageDialog(this, "Delete successful!");
                    this.resetMaintenanceForm();
                    this.resetMaintenanceRatingForm();
                }
            } catch (ClassNotFoundException ex) {
                this.taManagerLog.append("\n" + ex.getMessage());
            } catch (SQLException ex) {
                this.taManagerLog.append("\n" + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btDeleteMaintenanceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ManagerFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CashPayment;
    private javax.swing.JPanel CheckPayment;
    private javax.swing.JTextField CreditCardCreditCardNumber;
    private javax.swing.JTextField CreditCardCreditType;
    private javax.swing.JTextField CreditCardExperiedDate;
    private javax.swing.JTextField CreditCardHolderName;
    private javax.swing.JTextField CreditCardPayAmount;
    private javax.swing.JTextField CreditCardPayDate;
    private javax.swing.JTextField CreditCardPayID;
    private javax.swing.JTextField CreditCardPayMethod;
    private javax.swing.JPanel CreditCardPayment;
    private javax.swing.JButton CreditCardViewDetails;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JPanel PaymentPanel;
    private javax.swing.JPanel TaskPanel;
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btAddNotificationTemplate;
    private javax.swing.JButton btDeleteMaintenance;
    private javax.swing.JButton btDeleteMaintenanceRating;
    private javax.swing.JButton btDeleteNotify;
    private javax.swing.JButton btDeleteTerminaiton;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btResetMaintenanceRequest;
    private javax.swing.JButton btTurnOffRatingTab;
    private javax.swing.JButton btTurnOnRatingTab;
    private javax.swing.JButton btUpdateMaintenanceRating;
    private javax.swing.JButton btUpdateNotify;
    private javax.swing.JButton btUpdateTermination;
    private javax.swing.JButton btnCashPayment;
    private javax.swing.JButton btnCheckPayment;
    private javax.swing.JButton btnCreateLease;
    private javax.swing.JButton btnCreditCardPayment;
    private javax.swing.JButton btnFinalPrint;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnLandlordRegistration;
    private javax.swing.JButton btnPrintLeaseDetails;
    private javax.swing.JButton btnPrintTenantDetails;
    private javax.swing.JButton btnRent;
    private javax.swing.JButton btnRentNewApartment;
    private javax.swing.JButton btnRequestChange;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetChange;
    private javax.swing.JButton btnResetTer;
    private javax.swing.JButton cashBtnView;
    private javax.swing.JTextField cashPayAmount;
    private javax.swing.JTextField cashPayDate;
    private javax.swing.JTextField cashPayID;
    private javax.swing.JTextField cashPayMethod;
    private javax.swing.JComboBox cbCreditCardType;
    private javax.swing.JComboBox cbEmailNotification;
    private com.toedter.calendar.JDateChooser cbExpDate;
    private javax.swing.JComboBox cbPaymentMethod;
    private javax.swing.JTextField checkBankName;
    private javax.swing.JButton checkBtnView;
    private javax.swing.JTextField checkCheckNumber;
    private javax.swing.JTextField checkPayAmount;
    private javax.swing.JTextField checkPayDate;
    private javax.swing.JTextField checkPayID;
    private javax.swing.JTextField checkPayMethod;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private com.toedter.calendar.JDateChooser jcdDate;
    private com.toedter.calendar.JDateChooser jcdDateToPay;
    private com.toedter.calendar.JDateChooser jdcChangeDate;
    private com.toedter.calendar.JDateChooser jdcEndDate;
    private com.toedter.calendar.JDateChooser jdcLeavingDateTer;
    private com.toedter.calendar.JDateChooser jdcRentDate;
    private com.toedter.calendar.JDateChooser jdcStartDate;
    private javax.swing.JTabbedPane mainTabPane;
    private javax.swing.JPanel panelAccount;
    private javax.swing.JPanel panelBuildingPicture;
    private javax.swing.JPanel panelChoseApartment;
    private javax.swing.JPanel panelCreatelease;
    private javax.swing.JPanel panelPayment;
    private javax.swing.JPanel panelReport;
    private javax.swing.JPanel panelTenantRegistration;
    private javax.swing.JTextArea taDescription;
    private javax.swing.JTextArea taManagerLog;
    private javax.swing.JTabbedPane tabRenNewApartment;
    private javax.swing.JTabbedPane tabSubTask;
    private javax.swing.event.ListSelectionListener rowApartmentChangeManagerListener;
    private javax.swing.JTable tbApartmentChangeManager;
    private javax.swing.table.DefaultTableModel apartmentChangeModelManager;
    private javax.swing.table.DefaultTableModel apartmentModel;
    private javax.swing.JTable tbApartmentRent;
    private javax.swing.event.ListSelectionListener rowApartmentListener;
    private javax.swing.event.ListSelectionListener rowCashListener;
    private javax.swing.JTable tbCashPayment;
    private javax.swing.table.DefaultTableModel cashModel;
    private javax.swing.table.DefaultTableModel checkModel;
    private javax.swing.JTable tbCheckPayment;
    private javax.swing.event.ListSelectionListener rowCheckSelection;
    private javax.swing.table.DefaultTableModel creditCardModel;
    private javax.swing.JTable tbCreditCardPayment;
    private javax.swing.event.ListSelectionListener rowCreditCardListener;
    private javax.swing.table.DefaultTableModel landlordModel;
    private javax.swing.JTable tbLandlordManager;
    private javax.swing.event.ListSelectionListener rowlandlordPasswordManager;
    private javax.swing.table.DefaultTableModel maintenancRatingModel;
    private javax.swing.JTable tbMaintenanceRating;
    private javax.swing.event.ListSelectionListener rowMaintenancRatingListener;
    private javax.swing.event.ListSelectionListener rowMaintenanceListener;
    private javax.swing.JTable tbMaintenanceRequest;
    private javax.swing.table.DefaultTableModel maintenanceModel;
    private javax.swing.table.DefaultTableModel notificationModel;
    private javax.swing.JTable tbNotification;
    private javax.swing.JTable tbTermination;
    private javax.swing.table.DefaultTableModel terModel;
    private javax.swing.event.ListSelectionListener terRowListener;
    private javax.swing.JButton tbnRegistration;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtApartmentNo;
    private javax.swing.JTextField txtApartmentNumberChange;
    private javax.swing.JTextField txtApartmentNumberLease;
    private javax.swing.JTextField txtApartmentNumberNew;
    private javax.swing.JTextField txtApartmentNumberRent;
    private javax.swing.JTextField txtApartmentSizeRent;
    private javax.swing.JTextField txtApartmentTypeRent;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtBankName;
    private javax.swing.JTextField txtBuildingNameRent;
    private javax.swing.JTextField txtCheckNumber;
    private javax.swing.JTextField txtCreditCardNumber;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFeeRent;
    private javax.swing.JTextField txtFinalAddress;
    private javax.swing.JTextField txtFinalApartmentNumber;
    private javax.swing.JTextField txtFinalBalance;
    private javax.swing.JTextField txtFinalEmail;
    private javax.swing.JTextField txtFinalEndDate;
    private javax.swing.JTextField txtFinalFirstname;
    private javax.swing.JTextField txtFinalLastname;
    private javax.swing.JTextField txtFinalLeaseID;
    private javax.swing.JTextField txtFinalPhone;
    private javax.swing.JTextField txtFinalRentalDate;
    private javax.swing.JTextField txtFinalSecurityDeposit;
    private javax.swing.JTextField txtFinalStartDate;
    private javax.swing.JTextField txtFinalTenantID;
    private javax.swing.JTextField txtFinalTerminationID;
    private javax.swing.JTextField txtFirstname;
    private javax.swing.JTextField txtHolderName;
    private javax.swing.JTextField txtLandlordIDManager;
    private javax.swing.JPasswordField txtLandlordPasswordManager;
    private javax.swing.JTextField txtLastname;
    private javax.swing.JTextField txtLateFee;
    private javax.swing.JTextField txtLeaseID;
    private javax.swing.JTextField txtLeaseIDRent;
    private javax.swing.JTextArea txtLeavingReasonTer;
    private javax.swing.JTextField txtMRApartmentNumber;
    private javax.swing.JTextField txtMRFeedbackDate;
    private javax.swing.JTextField txtMRMaintenanceID;
    private javax.swing.JTextField txtMRProblem;
    private javax.swing.JTextField txtMRRating;
    private javax.swing.JTextField txtMaintenaceSubject;
    private javax.swing.JTextField txtMaintenanceDate;
    private javax.swing.JTextArea txtMaintenanceDescription;
    private javax.swing.JTextField txtMaintenanceEmail;
    private javax.swing.JTextField txtMaintenanceID;
    private javax.swing.JTextField txtMantenanceTemplate;
    private javax.swing.JTextField txtNumApartmentManager;
    private javax.swing.JTextField txtNumBuildManager;
    private javax.swing.JTextField txtNumNotRentManager;
    private javax.swing.JTextField txtNumRentManager;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPayAmount;
    private com.toedter.calendar.JDateChooser txtPayDate;
    private javax.swing.JTextField txtPayID;
    private javax.swing.JTextField txtPayIDRent;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtRentFeeRent;
    private javax.swing.JTextField txtRentID;
    private javax.swing.JTextField txtSecurityDeposit;
    private javax.swing.JTextField txtStateZip;
    private javax.swing.JTextField txtSubject;
    private javax.swing.JTextField txtTemplateName;
    private javax.swing.JTextField txtTenantID;
    private javax.swing.JTextField txtTenantIDLease;
    private javax.swing.JTextField txtTerminationTerID;
    private javax.swing.JTextField txtZipCode;
    // End of variables declaration//GEN-END:variables
}
