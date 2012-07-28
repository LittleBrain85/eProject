/*
 * To change this template, choose Tool | Templates
 * and open the template in the editor.
 */
package userProfilesUI;

import apartment.*;
import database.DBHandler;
import database.SettingHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import userProfiles.Landlord;
import userProfilesHandler.LandlordHandler;
import userProfilesHandler.UserProfilesHandler;
import utils.Search;
import utils.Table;

/**
 *
 * @author Thaichau
 */
public class LandlordFrame extends javax.swing.JFrame {

    DBHandler db;
    BuildingHandler bh;
    Landlord landlord;
    LandlordHandler lh;
    UserProfilesHandler uh;
    String curr;
    ApartmentChangeHandler ach;
    ApartmentHandler ah;

    /**
     * Creates new form Landlord
     */
    public LandlordFrame() {
        initComponents();
        db = new DBHandler();
        bh = new BuildingHandler();
        ah = new ApartmentHandler();
        ach = new ApartmentChangeHandler();
        uh = new UserProfilesHandler();
        curr = uh.getCurrentProfile();
        landlord = new Landlord();
        lh = new LandlordHandler();
        try {
            landlord = lh.getLandlordById(curr);
            this.txtLandlordIDProfile.setText(landlord.getID());
            this.txtLandlordPasswordProfile.setText(utils.Encrypt.dePass(landlord.getPassword()));
            int totalBuilding = bh.countBuildingByLandlordID(landlord);
            this.txtTotalBuilding.setText(String.valueOf(totalBuilding));
            int totalApartment = ah.countApartmetnByLandlordID(landlord);
            this.txtTotalApartment.setText(String.valueOf(totalApartment));
            int numOfOneBedRoom = ah.countBedroom("One");
            int numOfTwoBedRoom = ah.countBedroom("Two");
            int numOfThreeBedRoom = ah.countBedroom("Three");
            this.txtOneBedRoom.setText(String.valueOf(numOfOneBedRoom));
            this.txtTwoBedroom.setText(String.valueOf(numOfTwoBedRoom));
            this.txtThreeBedroom.setText(String.valueOf(numOfThreeBedRoom));
            int hasRent = ah.countApartmentRentByLandlordID(landlord);
            this.txtApartmentHasRent.setText(String.valueOf(hasRent));
            this.txtApartmentNotRent.setText(String.valueOf(totalApartment - hasRent));
            SettingHandler loadLogs = new SettingHandler();
            Vector vLogs = loadLogs.loadLoggerContent(this);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.txtLandlordID.setText(curr);
        try {
            this.loadBuildingTable();
            this.loadTableApartment();
            this.loadApartmentChange();
            this.loadPaymentReport();
            this.loadLandlordEmail();
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (java.sql.SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        }
        this.btnDeleteBuilding.setEnabled(false);
        this.btnUpdateBuilding.setEnabled(false);
        this.btnAptDel.setEnabled(false);
        this.btnAptUpd.setEnabled(false);
        this.btnApartmentChangeDel.setEnabled(false);
        this.btnApartmentChangeUpd.setEnabled(false);
    }

    private void setValueToBuildingTextFields(javax.swing.JTextField... tf) {
        for (int i = 0; i < tf.length; i++) {
            tf[i].setText(this.tbBuilding.getValueAt(this.tbBuilding.getSelectedRow(), i).toString());
        }
        this.btnDeleteBuilding.setEnabled(true);
        this.btnUpdateBuilding.setEnabled(true);
        this.txtBuildingName.setEditable(false);
    }

    private void saveValueToApartmentTextField(javax.swing.JTextField... tf) {
        for (int i = 0; i < tf.length; i++) {
            tf[i].setText(this.tbApartment.getValueAt(this.tbApartment.getSelectedRow(), i).toString());
        }
    }

    private void saveValueToApartmentCombobox(javax.swing.JComboBox cb) {
        cb.setSelectedItem(this.tbApartment.getValueAt(this.tbApartment.getSelectedRow(), 4).toString());
        this.btnAptDel.setEnabled(true);
        this.btnAptUpd.setEnabled(true);
        this.txtApartmentNum.setEditable(false);
    }

    private void setValueToApartmentChangeCombo() {
        this.cbAptChangeNumber.setSelectedItem(this.tbApartmentChange.getValueAt(this.tbApartmentChange.getSelectedRow(), 0).toString());
        this.cbNewApartment.setSelectedItem(this.tbApartmentChange.getValueAt(this.tbApartmentChange.getSelectedRow(), 2).toString());
    }

    private void setDateToDateChangeApartment(com.toedter.calendar.JDateChooser jdc) throws ParseException {
        jdc.setDate(utils.ConvertDate.convertStringToDate(this.tbApartmentChange.getValueAt(this.tbApartmentChange.getSelectedRow(), 1).toString()));
    }

    private void loadBuildingTable() throws SQLException {
        java.sql.ResultSet rs = bh.getAllBuildingByLanlordID(curr);
        db = new DBHandler();
        db.loadTable(tbBuildingModel, rs);
    }

    private void loadTableApartment() throws SQLException {
        java.sql.ResultSet rs = bh.getResultsetApartmentByLandlordID(curr);
        db = new DBHandler();
        db.loadTable(tbApartmentModel, rs);
    }

    private void loadApartmentChange() throws SQLException, ClassNotFoundException {
        java.sql.ResultSet rs = ach.getResultSetApartmentChangeByLandlordID(curr);
        db = new DBHandler();
        db.loadTable(apartmentChangeModel, rs);
    }
    //SQLite Table

    private void loadLandlordEmail() throws SQLException {
        SettingHandler emailHandler = new SettingHandler();
        Vector vEmail = emailHandler.getResultSetFakeMailByLandlordID(landlord);
        this.emailLandlordModel.addRow(vEmail);
    }

    private void resetBuildingForm() {
        utils.EmptyForm.emptyTextFields(this.txtBuildingName, this.txtAddress, this.txtZipCode);
        this.txtBuildingName.setEditable(true);
    }

    private void resetApartmentForm() throws ClassNotFoundException, SQLException {
        utils.EmptyForm.emptyTextFields(this.txtApartmentNum, this.txtApartmentSize, this.txtApartmenType, this.txtApartmentRentalFee);
        Vector bName = bh.getAllBuildingNameByLandlordID(curr);
        this.cbBudildingName.setSelectedItem(new javax.swing.DefaultComboBoxModel(bName));
        this.txtApartmentNum.setEditable(true);
    }

    private void resetApartmentChangeForm() throws ClassNotFoundException, SQLException {
        this.txtDateChange.setDate(new java.util.Date());
        Vector bName = bh.getAllBuildingNameByLandlordID(curr);
        this.cbBudildingName.setSelectedItem(new javax.swing.DefaultComboBoxModel(bName));
        this.cbNewApartment.setSelectedItem(new javax.swing.DefaultComboBoxModel(bName));
        this.btnChange.setEnabled(true);
        this.cbAptChangeNumber.setEnabled(true);
    }

    private void loadPaymentReport() throws ClassNotFoundException, SQLException {
        java.sql.ResultSet rs = lh.paymentReport(landlord);
        db.loadTable(paymentReportModel, rs);
    }

    private void setValueToPaymentForm() {
        this.txtTenantIDReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 0).toString());
        this.txtBalanceReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 1).toString());
        this.txtRentalDate.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 2).toString());
        this.txtEndDateReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 3).toString());
        this.txtStartDateReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 4).toString());
        this.txtRentalFeeReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 5).toString());
        this.txtApartmentReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 6).toString());
        this.txtbuildingNameReport.setText(this.tbPaymentReport.getValueAt(this.tbPaymentReport.getSelectedRow(), 7).toString());
    }

    private void writeContentFile(String filename, String content) throws FileNotFoundException, IOException {
        java.io.FileWriter out = new java.io.FileWriter(filename);
        out.write(content);
        out.close();
    }

    private void setValueToLandlordEmail() {
        this.txtFromLandlordEmail.setText(this.tbLandlordEmail.getValueAt(this.tbLandlordEmail.getSelectedRow(), 0).toString());
        this.txtToLandlordEmail.setText(this.tbLandlordEmail.getValueAt(this.tbLandlordEmail.getSelectedRow(), 1).toString());
        this.txtSubjectLandlordEmail.setText(this.tbLandlordEmail.getValueAt(this.tbLandlordEmail.getSelectedRow(), 2).toString());
        this.taContentLandlordEmail.setText(this.tbLandlordEmail.getValueAt(this.tbLandlordEmail.getSelectedRow(), 3).toString());
    }

    private void saveLogs() throws SQLException {
        if (landlordSaveLogs.isSelected()) {
            SettingHandler saveLogsNow = new SettingHandler();
            saveLogsNow.saveLogger(this.taLandlordLog.getText(), this);
            saveLogsNow.dbClose();
        }
    }
    /*
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buildingName = new javax.swing.JPopupMenu();
        Cut = new javax.swing.JMenuItem();
        Copy = new javax.swing.JMenuItem();
        Paste = new javax.swing.JMenuItem();
        Delete = new javax.swing.JMenuItem();
        sepMenu = new javax.swing.JPopupMenu.Separator();
        SelectAll = new javax.swing.JMenuItem();
        DeleteAll = new javax.swing.JMenuItem();
        addressContextMenu = new javax.swing.JPopupMenu();
        Cut1 = new javax.swing.JMenuItem();
        Copy1 = new javax.swing.JMenuItem();
        Paste1 = new javax.swing.JMenuItem();
        Delete1 = new javax.swing.JMenuItem();
        sepMenu1 = new javax.swing.JPopupMenu.Separator();
        SelectAll1 = new javax.swing.JMenuItem();
        DeleteAll1 = new javax.swing.JMenuItem();
        ZipContextMenu = new javax.swing.JPopupMenu();
        Cut2 = new javax.swing.JMenuItem();
        Copy2 = new javax.swing.JMenuItem();
        Paste2 = new javax.swing.JMenuItem();
        Delete2 = new javax.swing.JMenuItem();
        sepMenu2 = new javax.swing.JPopupMenu.Separator();
        SelectAll2 = new javax.swing.JMenuItem();
        DeleteAll2 = new javax.swing.JMenuItem();
        dlgChangePassword = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtOldPassword = new javax.swing.JPasswordField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btChangePasswordLandlord = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtNewPassword = new javax.swing.JPasswordField();
        txtConfirmNewPassword = new javax.swing.JPasswordField();
        dlgLogger = new javax.swing.JDialog();
        jScrollPane7 = new javax.swing.JScrollPane();
        taLoggerViewer = new javax.swing.JTextArea();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        itemSave = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        saveLogs = new javax.swing.JFileChooser();
        dlgOptions = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        landlordSaveLogs = new javax.swing.JCheckBox();
        tabMainLandlord = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtLandlordIDProfile = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btChangePasswordProfile = new javax.swing.JButton();
        txtLandlordPasswordProfile = new javax.swing.JPasswordField();
        chkShowPasswordLandlord = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtTotalBuilding = new javax.swing.JTextField();
        btViewDetailBuilding = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtTotalApartment = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtOneBedRoom = new javax.swing.JTextField();
        txtTwoBedroom = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtThreeBedroom = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtApartmentHasRent = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtApartmentNotRent = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        addNewBuildingPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuildingName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLandlordID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtZipCode = new javax.swing.JTextField();
        btnAddBuilding = new javax.swing.JButton();
        btnUpdateBuilding = new javax.swing.JButton();
        btnDeleteBuilding = new javax.swing.JButton();
        btnResetBuilding = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBuilding = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        cbBuildingSearchType = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        txtBuildingSearchText = new javax.swing.JTextField();
        btBuildingSearch = new javax.swing.JButton();
        btRefreshBuildingTable = new javax.swing.JButton();
        btNextBuilding = new javax.swing.JButton();
        btPreviousBuilding = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        addNewApartmentPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtApartmentNum = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtApartmentSize = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtApartmenType = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtApartmentRentalFee = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbBudildingName = new javax.swing.JComboBox();
        btnAptAdd = new javax.swing.JButton();
        btnAptUpd = new javax.swing.JButton();
        btnAptDel = new javax.swing.JButton();
        btnAptReset = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbApartment = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtApartmentSearchText = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cbApartmentSearchType = new javax.swing.JComboBox();
        btSearchApartment = new javax.swing.JButton();
        btnRefeshApartment = new javax.swing.JButton();
        btPreviousApartment = new javax.swing.JButton();
        btNextApartment = new javax.swing.JButton();
        btFirstApartment = new javax.swing.JButton();
        btLastApartment = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        changeApartmentPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnChange = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        txtDateChange = new com.toedter.calendar.JDateChooser();
        cbAptChangeNumber = new javax.swing.JComboBox();
        btnApartmentChangeUpd = new javax.swing.JButton();
        btnApartmentChangeDel = new javax.swing.JButton();
        btnResetApartmentChange = new javax.swing.JButton();
        cbNewApartment = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbApartmentChange = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        txtTenantIDReport = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtBalanceReport = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtRentalDate = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtEndDateReport = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtRentalFeeReport = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtApartmentReport = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtbuildingNameReport = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbPaymentReport = new javax.swing.JTable();
        jLabel38 = new javax.swing.JLabel();
        txtStartDateReport = new javax.swing.JTextField();
        btNextReportPayment = new javax.swing.JButton();
        btPreviousReportPayment = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbLandlordEmail = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        txtFromLandlordEmail = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtToLandlordEmail = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtSubjectLandlordEmail = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        taContentLandlordEmail = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnHomeExit = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnChangePass = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btViewLogs = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btOption = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jScrollPane2 = new javax.swing.JScrollPane();
        taLandlordLog = new javax.swing.JTextArea();
        this.setLocation(150, 10);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        Cut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        Cut.setText("Cut");
        Cut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CutActionPerformed(evt);
            }
        });
        buildingName.add(Cut);

        Copy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        Copy.setText("Copy");
        Copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CopyActionPerformed(evt);
            }
        });
        buildingName.add(Copy);

        Paste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        Paste.setText("Paste");
        Paste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasteActionPerformed(evt);
            }
        });
        buildingName.add(Paste);

        Delete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        buildingName.add(Delete);
        buildingName.add(sepMenu);

        SelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        SelectAll.setText("Select All");
        SelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectAllActionPerformed(evt);
            }
        });
        buildingName.add(SelectAll);

        DeleteAll.setText("Delete All");
        DeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAllActionPerformed(evt);
            }
        });
        buildingName.add(DeleteAll);

        Cut1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        Cut1.setText("Cut");
        Cut1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cut1ActionPerformed(evt);
            }
        });
        addressContextMenu.add(Cut1);

        Copy1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        Copy1.setText("Copy");
        Copy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Copy1ActionPerformed(evt);
            }
        });
        addressContextMenu.add(Copy1);

        Paste1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        Paste1.setText("Paste");
        Paste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Paste1ActionPerformed(evt);
            }
        });
        addressContextMenu.add(Paste1);

        Delete1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        Delete1.setText("Delete");
        Delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Delete1ActionPerformed(evt);
            }
        });
        addressContextMenu.add(Delete1);
        addressContextMenu.add(sepMenu1);

        SelectAll1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        SelectAll1.setText("Select All");
        SelectAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectAll1ActionPerformed(evt);
            }
        });
        addressContextMenu.add(SelectAll1);

        DeleteAll1.setText("Delete All");
        DeleteAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAll1ActionPerformed(evt);
            }
        });
        addressContextMenu.add(DeleteAll1);

        Cut2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        Cut2.setText("Cut");
        Cut2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cut2ActionPerformed(evt);
            }
        });
        ZipContextMenu.add(Cut2);

        Copy2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        Copy2.setText("Copy");
        Copy2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Copy2ActionPerformed(evt);
            }
        });
        ZipContextMenu.add(Copy2);

        Paste2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        Paste2.setText("Paste");
        Paste2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Paste2ActionPerformed(evt);
            }
        });
        ZipContextMenu.add(Paste2);

        Delete2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        Delete2.setText("Delete");
        Delete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Delete2ActionPerformed(evt);
            }
        });
        ZipContextMenu.add(Delete2);
        ZipContextMenu.add(sepMenu2);

        SelectAll2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        SelectAll2.setText("Select All");
        SelectAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectAll2ActionPerformed(evt);
            }
        });
        ZipContextMenu.add(SelectAll2);

        DeleteAll2.setText("Delete All");
        DeleteAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAll2ActionPerformed(evt);
            }
        });
        ZipContextMenu.add(DeleteAll2);

        dlgChangePassword.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgChangePassword.setTitle("Change Your Password");
        dlgChangePassword.setLocation(300, 150);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Change Password "));

        jLabel15.setText("Old Password");

        jLabel16.setText("New Password");

        jLabel17.setText("Confirm New Password");

        btChangePasswordLandlord.setText("Change");
        btChangePasswordLandlord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btChangePasswordLandlordActionPerformed(evt);
            }
        });

        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(txtConfirmNewPassword))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtOldPassword)
                            .addComponent(txtNewPassword))))
                .addGap(27, 27, 27))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(btChangePasswordLandlord)
                .addGap(29, 29, 29)
                .addComponent(jButton2)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtOldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtConfirmNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btChangePasswordLandlord)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout dlgChangePasswordLayout = new javax.swing.GroupLayout(dlgChangePassword.getContentPane());
        dlgChangePassword.getContentPane().setLayout(dlgChangePasswordLayout);
        dlgChangePasswordLayout.setHorizontalGroup(
            dlgChangePasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgChangePasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dlgChangePasswordLayout.setVerticalGroup(
            dlgChangePasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgChangePasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        dlgLogger.setLocation(250, 150);
        dlgLogger.setTitle("Log Viewer");

        taLoggerViewer.setColumns(20);
        taLoggerViewer.setRows(5);
        jScrollPane7.setViewportView(taLoggerViewer);

        jMenu3.setText("File");

        itemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        itemSave.setText("Exit");
        itemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSaveActionPerformed(evt);
            }
        });
        jMenu3.add(itemSave);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Save");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar2.add(jMenu3);

        dlgLogger.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout dlgLoggerLayout = new javax.swing.GroupLayout(dlgLogger.getContentPane());
        dlgLogger.getContentPane().setLayout(dlgLoggerLayout);
        dlgLoggerLayout.setHorizontalGroup(
            dlgLoggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
        );
        dlgLoggerLayout.setVerticalGroup(
            dlgLoggerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
        );

        saveLogs.setCurrentDirectory(new java.io.File("C:\\"));
            saveLogs.setDialogTitle("Save Logger");
            saveLogs.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Document", ".txt") );

            dlgOptions.setLocation(350, 150);
            dlgOptions.setTitle("Landlord Oprtions");

            landlordSaveLogs.setText("Save Log before exit system");
            landlordSaveLogs.setName("LandlordSaveLogs");
            landlordSaveLogs.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    landlordSaveLogsItemStateChanged(evt);
                }
            });

            javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
            jPanel16.setLayout(jPanel16Layout);
            jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(23, 23, 23)
                    .addComponent(landlordSaveLogs)
                    .addContainerGap(209, Short.MAX_VALUE))
            );
            jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(landlordSaveLogs)
                    .addContainerGap(135, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("System", jPanel16);

            javax.swing.GroupLayout dlgOptionsLayout = new javax.swing.GroupLayout(dlgOptions.getContentPane());
            dlgOptions.getContentPane().setLayout(dlgOptionsLayout);
            dlgOptionsLayout.setHorizontalGroup(
                dlgOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
            );
            dlgOptionsLayout.setVerticalGroup(
                dlgOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
            );

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setTitle("Landlord Panel");
            setName("LandlordFrame");

            tabMainLandlord.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            tabMainLandlord.setTabPlacement(javax.swing.JTabbedPane.LEFT);
            tabMainLandlord.setToolTipText("Landlord Panel");
            tabMainLandlord.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tabMainLandlordMouseClicked(evt);
                }
            });

            jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Information"));

            jLabel20.setText("Landlord ID");

            txtLandlordIDProfile.setEditable(false);

            jLabel21.setText("Password");

            btChangePasswordProfile.setText("Change Password");
            btChangePasswordProfile.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btChangePasswordProfileActionPerformed(evt);
                }
            });

            txtLandlordPasswordProfile.setEditable(false);

            chkShowPasswordLandlord.setText("Show Password");
            chkShowPasswordLandlord.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    chkShowPasswordLandlordItemStateChanged(evt);
                }
            });

            javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
            jPanel11.setLayout(jPanel11Layout);
            jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(42, 42, 42)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtLandlordIDProfile, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                                .addComponent(txtLandlordPasswordProfile))
                            .addContainerGap(32, Short.MAX_VALUE))
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addComponent(btChangePasswordProfile)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chkShowPasswordLandlord)
                            .addGap(19, 19, 19))))
            );
            jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(txtLandlordIDProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(txtLandlordPasswordProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btChangePasswordProfile)
                        .addComponent(chkShowPasswordLandlord))
                    .addContainerGap())
            );

            jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Building Information"));

            jLabel22.setText("Total Building");

            txtTotalBuilding.setEditable(false);
            txtTotalBuilding.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            btViewDetailBuilding.setText("View Details Building");
            btViewDetailBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btViewDetailBuildingActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
            jPanel12.setLayout(jPanel12Layout);
            jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel22)
                    .addGap(32, 32, 32)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btViewDetailBuilding)
                        .addComponent(txtTotalBuilding, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(28, Short.MAX_VALUE))
            );
            jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(txtTotalBuilding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(33, 33, 33)
                    .addComponent(btViewDetailBuilding)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Apartment Information"));
            jPanel13.setForeground(new java.awt.Color(51, 153, 0));

            jLabel23.setText("Total Apartment");

            txtTotalApartment.setEditable(false);
            txtTotalApartment.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel24.setText("Total Apartment One-Bedroom");

            txtOneBedRoom.setEditable(false);
            txtOneBedRoom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            txtTwoBedroom.setEditable(false);
            txtTwoBedroom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel25.setText("Total Apartment Two-Bedroom");

            jLabel26.setText("Total Apartment Three-Bedroom");

            txtThreeBedroom.setEditable(false);
            txtThreeBedroom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jButton6.setText("View All Apartment");
            jButton6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton6ActionPerformed(evt);
                }
            });

            jLabel27.setText("Number of apartment has been rental");

            txtApartmentHasRent.setEditable(false);
            txtApartmentHasRent.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel28.setText("Not Rental");

            txtApartmentNotRent.setEditable(false);
            txtApartmentNotRent.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
            jPanel13.setLayout(jPanel13Layout);
            jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel23)
                                .addComponent(jLabel24)
                                .addComponent(jLabel25)
                                .addComponent(jLabel26)
                                .addComponent(jLabel27))
                            .addGap(61, 61, 61)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTotalApartment)
                                .addComponent(txtTwoBedroom)
                                .addComponent(txtOneBedRoom)
                                .addComponent(txtThreeBedroom)
                                .addComponent(txtApartmentHasRent, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28)
                            .addGap(18, 18, 18)
                            .addComponent(txtApartmentNotRent, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addGap(286, 286, 286)
                            .addComponent(jButton6)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(txtTotalApartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(txtOneBedRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTwoBedroom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(txtThreeBedroom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel27)
                        .addComponent(txtApartmentHasRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28)
                        .addComponent(txtApartmentNotRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                    .addComponent(jButton6)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
            jPanel10.setLayout(jPanel10Layout);
            jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            tabMainLandlord.addTab("          Home", new javax.swing.ImageIcon(getClass().getResource("/images/home.png")), jPanel3); // NOI18N

            addNewBuildingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bulding Information"));
            addNewBuildingPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    addNewBuildingPanelMouseClicked(evt);
                }
            });

            jLabel1.setText("Building Name");

            txtBuildingName.setComponentPopupMenu(buildingName);

            jLabel2.setText("Address");

            txtAddress.setComponentPopupMenu(addressContextMenu);

            jLabel3.setText("Landlord ID");

            txtLandlordID.setEditable(false);

            jLabel4.setText("Zip Code");

            txtZipCode.setComponentPopupMenu(ZipContextMenu);
            txtZipCode.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtZipCodeActionPerformed(evt);
                }
            });

            btnAddBuilding.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
            btnAddBuilding.setText("Add");
            btnAddBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnAddBuildingActionPerformed(evt);
                }
            });

            btnUpdateBuilding.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update.png"))); // NOI18N
            btnUpdateBuilding.setText("Update");
            btnUpdateBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnUpdateBuildingActionPerformed(evt);
                }
            });

            btnDeleteBuilding.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.gif"))); // NOI18N
            btnDeleteBuilding.setText("Delete");
            btnDeleteBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnDeleteBuildingActionPerformed(evt);
                }
            });

            btnResetBuilding.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reset.png"))); // NOI18N
            btnResetBuilding.setText("Reset");
            btnResetBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnResetBuildingActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout addNewBuildingPanelLayout = new javax.swing.GroupLayout(addNewBuildingPanel);
            addNewBuildingPanel.setLayout(addNewBuildingPanelLayout);
            addNewBuildingPanelLayout.setHorizontalGroup(
                addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewBuildingPanelLayout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtBuildingName)
                        .addComponent(txtZipCode, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3))
                    .addGap(66, 66, 66)
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtLandlordID, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(23, 23, 23))
                .addGroup(addNewBuildingPanelLayout.createSequentialGroup()
                    .addGap(135, 135, 135)
                    .addComponent(btnAddBuilding, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(46, 46, 46)
                    .addComponent(btnUpdateBuilding)
                    .addGap(47, 47, 47)
                    .addComponent(btnDeleteBuilding)
                    .addGap(40, 40, 40)
                    .addComponent(btnResetBuilding)
                    .addContainerGap(128, Short.MAX_VALUE))
            );
            addNewBuildingPanelLayout.setVerticalGroup(
                addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewBuildingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtBuildingName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGap(18, 18, 18)
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtZipCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(txtLandlordID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGap(27, 27, 27)
                    .addGroup(addNewBuildingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAddBuilding)
                        .addComponent(btnUpdateBuilding)
                        .addComponent(btnDeleteBuilding)
                        .addComponent(btnResetBuilding))
                    .addContainerGap(19, Short.MAX_VALUE))
            );

            tbBuilding.getTableHeader().setReorderingAllowed(false);
            rowBuildingListener = new javax.swing.event.ListSelectionListener() {
                @Override
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    setValueToBuildingTextFields(txtBuildingName,txtAddress,txtZipCode,txtLandlordID);
                }
            };
            tbBuilding.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String [] {
                    "Building Name", "Address", "ZipCode", "LandlordID"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            tbBuilding.getSelectionModel().addListSelectionListener(rowBuildingListener);
            tbBuilding.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tbBuildingMouseClicked(evt);
                }
            });
            jScrollPane1.setViewportView(tbBuilding);
            tbBuildingModel = (DefaultTableModel) this.tbBuilding.getModel();

            jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel18.setText("Search");

            cbBuildingSearchType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Building Name", "Address", "Zip Code", "Landlord ID" }));

            jLabel19.setText("By");

            btBuildingSearch.setText("Search");
            btBuildingSearch.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btBuildingSearchActionPerformed(evt);
                }
            });

            btRefreshBuildingTable.setText("Refesh Table");
            btRefreshBuildingTable.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btRefreshBuildingTableActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
            jPanel8.setLayout(jPanel8Layout);
            jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel18)
                    .addGap(18, 18, 18)
                    .addComponent(txtBuildingSearchText, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel19)
                    .addGap(18, 18, 18)
                    .addComponent(cbBuildingSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(32, 32, 32)
                    .addComponent(btBuildingSearch)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btRefreshBuildingTable)
                    .addContainerGap())
            );
            jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(cbBuildingSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)
                        .addComponent(txtBuildingSearchText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btBuildingSearch)
                        .addComponent(btRefreshBuildingTable))
                    .addContainerGap(12, Short.MAX_VALUE))
            );

            btNextBuilding.setText("Next");
            btNextBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btNextBuildingActionPerformed(evt);
                }
            });

            btPreviousBuilding.setText("Previous");
            btPreviousBuilding.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btPreviousBuildingActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addNewBuildingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1)
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(268, 268, 268)
                            .addComponent(btPreviousBuilding)
                            .addGap(29, 29, 29)
                            .addComponent(btNextBuilding, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addNewBuildingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btNextBuilding)
                        .addComponent(btPreviousBuilding))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            tabMainLandlord.addTab("Add New Buidling", new javax.swing.ImageIcon(getClass().getResource("/icons/office-building.png")), jPanel1); // NOI18N

            addNewApartmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Apartment Information"));
            addNewApartmentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    addNewApartmentPanelMouseClicked(evt);
                }
            });

            jLabel5.setText("Apartment Number");

            jLabel6.setText("Size");

            jLabel7.setText("Apartment Type");

            jLabel8.setText("Rental Fee");

            jLabel9.setText("Building Name");

            btnAptAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
            btnAptAdd.setText("Add");
            btnAptAdd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnAptAddActionPerformed(evt);
                }
            });

            btnAptUpd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update.png"))); // NOI18N
            btnAptUpd.setText("Update");
            btnAptUpd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnAptUpdActionPerformed(evt);
                }
            });

            btnAptDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.gif"))); // NOI18N
            btnAptDel.setText("Delete");
            btnAptDel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnAptDelActionPerformed(evt);
                }
            });

            btnAptReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reset.png"))); // NOI18N
            btnAptReset.setText("Reset");
            btnAptReset.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnAptResetActionPerformed(evt);
                }
            });

            jLabel13.setText("m2");

            jLabel14.setText("USD");

            javax.swing.GroupLayout addNewApartmentPanelLayout = new javax.swing.GroupLayout(addNewApartmentPanel);
            addNewApartmentPanel.setLayout(addNewApartmentPanelLayout);
            addNewApartmentPanelLayout.setHorizontalGroup(
                addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewApartmentPanelLayout.createSequentialGroup()
                    .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addNewApartmentPanelLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jLabel9))
                            .addGap(18, 18, 18)
                            .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtApartmentNum)
                                .addComponent(txtApartmenType)
                                .addComponent(cbBudildingName, 0, 205, Short.MAX_VALUE))
                            .addGap(47, 47, 47)
                            .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel8))
                            .addGap(18, 18, 18)
                            .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtApartmentSize, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addComponent(txtApartmentRentalFee))
                            .addGap(18, 18, 18)
                            .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)))
                        .addGroup(addNewApartmentPanelLayout.createSequentialGroup()
                            .addGap(142, 142, 142)
                            .addComponent(btnAptAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(42, 42, 42)
                            .addComponent(btnAptUpd)
                            .addGap(44, 44, 44)
                            .addComponent(btnAptDel)
                            .addGap(42, 42, 42)
                            .addComponent(btnAptReset)))
                    .addContainerGap(42, Short.MAX_VALUE))
            );
            addNewApartmentPanelLayout.setVerticalGroup(
                addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(addNewApartmentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtApartmentNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(txtApartmentSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGap(18, 18, 18)
                    .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtApartmenType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(txtApartmentRentalFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14))
                    .addGap(18, 18, 18)
                    .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(cbBudildingName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                    .addGroup(addNewApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAptAdd)
                        .addComponent(btnAptUpd)
                        .addComponent(btnAptDel)
                        .addComponent(btnAptReset))
                    .addGap(27, 27, 27))
            );

            tbApartment.getTableHeader().setReorderingAllowed(false);
            RowApartmentListener = new javax.swing.event.ListSelectionListener() {
                @Override
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    saveValueToApartmentTextField(txtApartmentNum,txtApartmentSize,txtApartmenType,txtApartmentRentalFee);
                    saveValueToApartmentCombobox(cbBudildingName);
                }
            };
            tbApartment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            tbApartment.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String [] {
                    "ApartmentNumber", "Size", "Type", "Rental Fee", "BuildingName"
                }
            ));
            tbApartmentModel = (javax.swing.table.DefaultTableModel)this.tbApartment.getModel();
            tbApartment.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tbApartmentMouseClicked(evt);
                }
            });
            jScrollPane4.setViewportView(tbApartment);
            tbApartment.getSelectionModel().addListSelectionListener(RowApartmentListener);

            jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabel29.setText("Search");

            jLabel30.setText("By");

            cbApartmentSearchType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Apartment Number", "Size", "Type", "Price", "Building Name" }));

            btSearchApartment.setText("Search");
            btSearchApartment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btSearchApartmentActionPerformed(evt);
                }
            });

            btnRefeshApartment.setText("Refesh Table");
            btnRefeshApartment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnRefeshApartmentActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
            jPanel9.setLayout(jPanel9Layout);
            jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel29)
                    .addGap(18, 18, 18)
                    .addComponent(txtApartmentSearchText, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel30)
                    .addGap(18, 18, 18)
                    .addComponent(cbApartmentSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btSearchApartment)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                    .addComponent(btnRefeshApartment)
                    .addContainerGap())
            );
            jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29)
                        .addComponent(txtApartmentSearchText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30)
                        .addComponent(cbApartmentSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btSearchApartment)
                        .addComponent(btnRefeshApartment))
                    .addContainerGap(16, Short.MAX_VALUE))
            );

            btPreviousApartment.setText("Previous");
            btPreviousApartment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btPreviousApartmentActionPerformed(evt);
                }
            });

            btNextApartment.setText("Next");
            btNextApartment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btNextApartmentActionPerformed(evt);
                }
            });

            btFirstApartment.setText("First");
            btFirstApartment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btFirstApartmentActionPerformed(evt);
                }
            });

            btLastApartment.setText("Last");
            btLastApartment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btLastApartmentActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addNewApartmentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane4)
                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(145, 145, 145)
                            .addComponent(btFirstApartment, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(43, 43, 43)
                            .addComponent(btPreviousApartment)
                            .addGap(40, 40, 40)
                            .addComponent(btNextApartment, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(44, 44, 44)
                            .addComponent(btLastApartment, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addNewApartmentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(5, 5, 5)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btPreviousApartment)
                        .addComponent(btNextApartment)
                        .addComponent(btFirstApartment)
                        .addComponent(btLastApartment))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            tabMainLandlord.addTab("Add New Aaprtment", new javax.swing.ImageIcon(getClass().getResource("/icons/apartment.png")), jPanel2); // NOI18N

            changeApartmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Request Change Apartment Information"));
            changeApartmentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    changeApartmentPanelMouseClicked(evt);
                }
            });

            jLabel10.setText("Apartment Number");

            jLabel11.setText("Change Date");

            jLabel12.setText("New Apartment Number");

            btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Confirm.png"))); // NOI18N
            btnChange.setText("Confirm Change");
            btnChange.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnChangeActionPerformed(evt);
                }
            });

            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/viewDetails.png"))); // NOI18N
            jButton8.setText("View Details Tenant");

            txtDateChange.setDateFormatString("dd/MM/yyyy");

            btnApartmentChangeUpd.setText("Update");
            btnApartmentChangeUpd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnApartmentChangeUpdActionPerformed(evt);
                }
            });

            btnApartmentChangeDel.setText("Delete");
            btnApartmentChangeDel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnApartmentChangeDelActionPerformed(evt);
                }
            });

            btnResetApartmentChange.setText("Reset");
            btnResetApartmentChange.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnResetApartmentChangeActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout changeApartmentPanelLayout = new javax.swing.GroupLayout(changeApartmentPanel);
            changeApartmentPanel.setLayout(changeApartmentPanelLayout);
            changeApartmentPanelLayout.setHorizontalGroup(
                changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(changeApartmentPanelLayout.createSequentialGroup()
                    .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(changeApartmentPanelLayout.createSequentialGroup()
                            .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(changeApartmentPanelLayout.createSequentialGroup()
                                    .addGap(28, 28, 28)
                                    .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel10))
                                    .addGap(52, 52, 52))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changeApartmentPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)))
                            .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtDateChange, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbAptChangeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbNewApartment, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(changeApartmentPanelLayout.createSequentialGroup()
                            .addGap(74, 74, 74)
                            .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(btnApartmentChangeUpd)
                            .addGap(35, 35, 35)
                            .addComponent(btnApartmentChangeDel)
                            .addGap(31, 31, 31)
                            .addComponent(jButton8)
                            .addGap(26, 26, 26)
                            .addComponent(btnResetApartmentChange)))
                    .addContainerGap(45, Short.MAX_VALUE))
            );
            changeApartmentPanelLayout.setVerticalGroup(
                changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(changeApartmentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(cbAptChangeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addComponent(txtDateChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12)
                        .addComponent(cbNewApartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                    .addGroup(changeApartmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChange)
                        .addComponent(jButton8)
                        .addComponent(btnApartmentChangeUpd)
                        .addComponent(btnApartmentChangeDel)
                        .addComponent(btnResetApartmentChange))
                    .addContainerGap())
            );

            tbApartmentChange.getTableHeader().setReorderingAllowed(false);
            rowApartmentChangeLsitener = new javax.swing.event.ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    setValueToApartmentChangeCombo();
                    btnApartmentChangeDel.setEnabled(true);
                    btnApartmentChangeUpd.setEnabled(true);
                    cbAptChangeNumber.setEnabled(false);
                    btnChange.setEnabled(false);
                    try{
                        setDateToDateChangeApartment(txtDateChange);
                    }catch(java.text.ParseException ex){

                    }
                }
            };
            tbApartmentChange.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            tbApartmentChange.setModel(new javax.swing.table.DefaultTableModel(
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
            apartmentChangeModel = (javax.swing.table.DefaultTableModel)this.tbApartmentChange.getModel();
            tbApartmentChange.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tbApartmentChangeMouseClicked(evt);
                }
            });
            jScrollPane5.setViewportView(tbApartmentChange);
            tbApartmentChange.getSelectionModel().addListSelectionListener(rowApartmentChangeLsitener);

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(changeApartmentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane5))
                    .addContainerGap())
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(changeApartmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(13, Short.MAX_VALUE))
            );

            tabMainLandlord.addTab("Change Apartment", new javax.swing.ImageIcon(getClass().getResource("/icons/change_apartment.png")), jPanel4); // NOI18N

            jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Report"));

            jLabel31.setText("Tenant ID");

            jLabel32.setText("Balance");

            jLabel33.setText("Rental Date");

            jLabel34.setText("End Date");

            jLabel35.setText("Rental Fee");

            jLabel36.setText("Apartment No");

            jLabel37.setText("Building Name");

            rowPaymentReport = new javax.swing.event.ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    setValueToPaymentForm();
                }
            };
            tbPaymentReport.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null}
                },
                new String [] {
                    "Tenant ID", "Balance", "Rental Date", "End Date", "Start Date", "Rental Fee", "Apartment No", "Building Name"
                }
            ));
            tbPaymentReport.getSelectionModel().addListSelectionListener(rowPaymentReport);
            jScrollPane3.setViewportView(tbPaymentReport);
            paymentReportModel = (javax.swing.table.DefaultTableModel)this.tbPaymentReport.getModel();
            tbPaymentReport.getTableHeader().setReorderingAllowed(false);

            jLabel38.setText("Start Date");

            btNextReportPayment.setText("Next");
            btNextReportPayment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btNextReportPaymentActionPerformed(evt);
                }
            });

            btPreviousReportPayment.setText("Previous");
            btPreviousReportPayment.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btPreviousReportPaymentActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
            jPanel14.setLayout(jPanel14Layout);
            jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel31)
                                .addComponent(jLabel33)
                                .addComponent(jLabel35)
                                .addComponent(jLabel38))
                            .addGap(25, 25, 25)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtStartDateReport, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                                .addComponent(txtTenantIDReport)
                                .addComponent(txtRentalDate)
                                .addComponent(txtRentalFeeReport))
                            .addGap(75, 75, 75)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel32)
                                        .addComponent(jLabel34))
                                    .addGap(41, 41, 41)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtEndDateReport, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                        .addComponent(txtBalanceReport)))
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel36)
                                        .addComponent(jLabel37))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtbuildingNameReport, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtApartmentReport)))))
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGap(245, 245, 245)
                            .addComponent(btNextReportPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(51, 51, 51)
                            .addComponent(btPreviousReportPayment)))
                    .addContainerGap(48, Short.MAX_VALUE))
            );
            jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(txtTenantIDReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel32)
                        .addComponent(txtBalanceReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33)
                        .addComponent(txtRentalDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(txtEndDateReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36)
                        .addComponent(txtApartmentReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel38)
                        .addComponent(txtStartDateReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(21, 21, 21)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(txtbuildingNameReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)
                        .addComponent(txtRentalFeeReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btNextReportPayment)
                        .addComponent(btPreviousReportPayment))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            tabMainLandlord.addTab("Payment Report", new javax.swing.ImageIcon(getClass().getResource("/icons/Payment.png")), jPanel5); // NOI18N

            jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Email"));

            rowEmailLandlordListener = new javax.swing.event.ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    setValueToLandlordEmail();
                }
            };
            tbLandlordEmail.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "From", "To", "Subject", "Content"
                }
            ));
            emailLandlordModel = (javax.swing.table.DefaultTableModel)this.tbLandlordEmail.getModel();
            jScrollPane6.setViewportView(tbLandlordEmail);
            tbLandlordEmail.getSelectionModel().addListSelectionListener(rowEmailLandlordListener);
            tbLandlordEmail.getTableHeader().setReorderingAllowed(false);

            jLabel39.setText("From");

            jLabel40.setText("To");

            jLabel41.setText("Subject");

            jLabel42.setText("Content");

            jButton1.setText("Delete");

            jScrollPane9.setViewportView(taContentLandlordEmail);

            javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
            jPanel15.setLayout(jPanel15Layout);
            jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel41)
                        .addComponent(jLabel40)
                        .addComponent(jLabel39)
                        .addComponent(jLabel42))
                    .addGap(31, 31, 31)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtFromLandlordEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSubjectLandlordEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                            .addComponent(txtToLandlordEmail))
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(36, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addGap(323, 323, 323))
            );
            jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFromLandlordEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel39))
                    .addGap(11, 11, 11)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtToLandlordEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel40))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSubjectLandlordEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel41))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel42)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(jButton1)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
            jPanel6.setLayout(jPanel6Layout);
            jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            tabMainLandlord.addTab("                Mail", new javax.swing.ImageIcon(getClass().getResource("/icons/mail.png")), jPanel6); // NOI18N

            btnHomeExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1343457660_exit.png"))); // NOI18N
            btnHomeExit.setText("Exit");
            btnHomeExit.setToolTipText("Exit Main Windows");
            btnHomeExit.setFocusable(false);
            btnHomeExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnHomeExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            btnHomeExit.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnHomeExitActionPerformed(evt);
                }
            });
            jToolBar1.add(btnHomeExit);
            jToolBar1.add(jSeparator1);

            btnChangePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1343463408_preferences-desktop-cryptography.png"))); // NOI18N
            btnChangePass.setText("Change Password");
            btnChangePass.setToolTipText("Change Your Password");
            btnChangePass.setFocusable(false);
            btnChangePass.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btnChangePass.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            btnChangePass.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnChangePassActionPerformed(evt);
                }
            });
            jToolBar1.add(btnChangePass);
            jToolBar1.add(jSeparator2);

            btViewLogs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1343456827_file_extension_log.png"))); // NOI18N
            btViewLogs.setText("Logs");
            btViewLogs.setToolTipText("View Logger");
            btViewLogs.setFocusable(false);
            btViewLogs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btViewLogs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            btViewLogs.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btViewLogsActionPerformed(evt);
                }
            });
            jToolBar1.add(btViewLogs);
            jToolBar1.add(jSeparator3);

            btOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1343456923_advanced.png"))); // NOI18N
            btOption.setText("Options");
            btOption.setFocusable(false);
            btOption.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btOption.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            btOption.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btOptionActionPerformed(evt);
                }
            });
            jToolBar1.add(btOption);
            jToolBar1.add(jSeparator4);

            taLandlordListener = new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    try{
                        if(landlordSaveLogs.isSelected()){
                            saveLogs();
                        }
                    }catch(java.sql.SQLException ex){
                        taLandlordLog.append("" + ex.getMessage());
                    }
                }
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {

                }
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    try{
                        saveLogs();
                    }catch(java.sql.SQLException ex){
                        taLandlordLog.append("" + ex.getMessage());
                    }
                }
            };
            taLandlordLog.setColumns(20);
            taLandlordLog.setRows(5);
            jScrollPane2.setViewportView(taLandlordLog);
            taLandlordLog.getDocument().addDocumentListener(taLandlordListener);

            jMenu1.setText("File");
            jMenuBar1.add(jMenu1);

            jMenu2.setText("Edit");
            jMenuBar1.add(jMenu2);

            setJMenuBar(jMenuBar1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2)
                .addComponent(tabMainLandlord)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(tabMainLandlord, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
            );

            tabMainLandlord.getAccessibleContext().setAccessibleName("");

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void btnHomeExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnHomeExitActionPerformed

    private void btnAddBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBuildingActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(this.txtBuildingName, this.txtAddress, this.txtZipCode)) {
            utils.ShowMessage.showMessageDialog(this, "Please enter empty fields");
            return;
        }
        if (!utils.FormChecker.checkZip(this.txtZipCode)) {
            utils.ShowMessage.showMessageDialog(this, "Zip code cannot less than 5 character");
            return;
        }
        landlord.setID(this.txtLandlordID.getText());
        Building building = new Building(
                this.txtBuildingName.getText(),
                this.txtAddress.getText(),
                this.txtZipCode.getText(),
                landlord);
        try {
            bh.addBuilding(building);
            javax.swing.JOptionPane.showMessageDialog(this, "Add new Building successful !");
            this.taLandlordLog.append("Add building successful...\n");
            utils.ListenerManager.disableListenerOnTable(this.tbBuilding, rowBuildingListener);
            this.tbBuilding.repaint();
            this.resetBuildingForm();
        } catch (SQLException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        } finally {
            try {
                this.loadBuildingTable();
                Vector cbBuidlingName = bh.getAllBuildingNameByLandlordID(curr);
                this.cbBudildingName.setModel(new javax.swing.DefaultComboBoxModel(cbBuidlingName));
            } catch (ClassNotFoundException ex) {
                this.taLandlordLog.append("\n" + ex.getMessage());
            } catch (SQLException ex) {
                this.taLandlordLog.append("\n" + ex.getMessage());
            }
            utils.ListenerManager.enableListenerOnTable(this.tbBuilding, rowBuildingListener);
        }
    }//GEN-LAST:event_btnAddBuildingActionPerformed

    private void btnAptAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAptAddActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(txtApartmentNum, txtApartmentSize, txtApartmenType, txtApartmentRentalFee)) {
            utils.ShowMessage.showMessageDialog(this, "Please enter empty text fields");
            return;
        }
        if (!utils.FormChecker.isNum(txtApartmentSize, txtApartmentRentalFee)) {
            utils.ShowMessage.showMessageDialog(this, "Apartment size and Rental Fee must be digits value");
            return;
        }
        if (!utils.FormChecker.checkDoubleTextFieldsValue(txtApartmentSize, txtApartmentRentalFee)) {
            utils.ShowMessage.showMessageDialog(this, "Apartment size and Rental Fee must be greater than zero");
            return;
        }
        //initial building
        Building building = new Building();
        building.setBuildingName(this.cbBudildingName.getSelectedItem().toString());
        Apartment apartment = new Apartment(
                this.txtApartmentNum.getText(),
                Double.valueOf(this.txtApartmentSize.getText()),
                this.txtApartmenType.getText(),
                Double.valueOf(this.txtApartmentRentalFee.getText()),
                building);
        try {
            boolean result = bh.addApartment(apartment);
            if (result) {
                javax.swing.JOptionPane.showMessageDialog(this, "Add Apartment successful!");
                this.taLandlordLog.append("Add Apartment successful...\n");
                utils.ListenerManager.disableListenerOnTable(this.tbApartment, RowApartmentListener);
                this.tbApartment.repaint();
            }

        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadTableApartment();
            } catch (SQLException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            utils.ListenerManager.enableListenerOnTable(this.tbApartment, RowApartmentListener);
        }
    }//GEN-LAST:event_btnAptAddActionPerformed

    private void CutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CutActionPerformed
        this.txtBuildingName.cut();
    }//GEN-LAST:event_CutActionPerformed

    private void CopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CopyActionPerformed
        this.txtBuildingName.copy();
    }//GEN-LAST:event_CopyActionPerformed

    private void PasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasteActionPerformed
        this.txtBuildingName.paste();
    }//GEN-LAST:event_PasteActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        this.txtBuildingName.replaceSelection(null);
    }//GEN-LAST:event_DeleteActionPerformed

    private void SelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectAllActionPerformed
        this.txtBuildingName.selectAll();
    }//GEN-LAST:event_SelectAllActionPerformed

    private void DeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAllActionPerformed
        this.txtBuildingName.setText(null);
    }//GEN-LAST:event_DeleteAllActionPerformed

    private void Cut1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cut1ActionPerformed
        this.txtAddress.cut();
    }//GEN-LAST:event_Cut1ActionPerformed

    private void Copy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Copy1ActionPerformed
        this.txtAddress.copy();
    }//GEN-LAST:event_Copy1ActionPerformed

    private void Paste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Paste1ActionPerformed
        this.txtAddress.paste();
    }//GEN-LAST:event_Paste1ActionPerformed

    private void Delete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete1ActionPerformed
        this.txtAddress.replaceSelection(null);
    }//GEN-LAST:event_Delete1ActionPerformed

    private void SelectAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectAll1ActionPerformed
        this.txtAddress.selectAll();
    }//GEN-LAST:event_SelectAll1ActionPerformed

    private void DeleteAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAll1ActionPerformed
        this.txtAddress.setText(null);
    }//GEN-LAST:event_DeleteAll1ActionPerformed

    private void Cut2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cut2ActionPerformed
        this.txtZipCode.cut();
    }//GEN-LAST:event_Cut2ActionPerformed

    private void Copy2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Copy2ActionPerformed
        this.txtZipCode.copy();
    }//GEN-LAST:event_Copy2ActionPerformed

    private void Paste2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Paste2ActionPerformed
        this.txtZipCode.paste();
    }//GEN-LAST:event_Paste2ActionPerformed

    private void Delete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete2ActionPerformed
        this.txtZipCode.replaceSelection(null);
    }//GEN-LAST:event_Delete2ActionPerformed

    private void SelectAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectAll2ActionPerformed
        this.txtZipCode.selectAll();
    }//GEN-LAST:event_SelectAll2ActionPerformed

    private void DeleteAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAll2ActionPerformed
        this.txtZipCode.setText(null);
    }//GEN-LAST:event_DeleteAll2ActionPerformed

    private void txtZipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtZipCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtZipCodeActionPerformed

    private void btnUpdateBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateBuildingActionPerformed
        landlord.setID(this.txtLandlordID.getText());
        Building building = new Building(
                this.txtBuildingName.getText(),
                this.txtAddress.getText(),
                this.txtZipCode.getText(),
                landlord);
        try {
            bh.updateBuilding(building);
            javax.swing.JOptionPane.showMessageDialog(this, "Update building successful !");
            this.taLandlordLog.append("Update Building successful...\n");
            this.tbBuilding.getSelectionModel().removeListSelectionListener(rowBuildingListener);
            this.tbBuilding.repaint();
            this.resetBuildingForm();
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadBuildingTable();
            } catch (SQLException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            this.tbBuilding.getSelectionModel().addListSelectionListener(rowBuildingListener);
        }
    }//GEN-LAST:event_btnUpdateBuildingActionPerformed

    private void btnDeleteBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBuildingActionPerformed
        landlord.setID(this.txtLandlordID.getText());
        Building building = new Building(
                this.txtBuildingName.getText(),
                this.txtAddress.getText(),
                this.txtZipCode.getText(),
                landlord);
        try {
            bh.deleteBuilding(building);
            javax.swing.JOptionPane.showMessageDialog(this, "Delete Building successful !");
            this.taLandlordLog.append("Delete Building successful...\n");
            this.tbBuilding.getSelectionModel().removeListSelectionListener(rowBuildingListener);
            this.tbBuilding.repaint();
            this.resetBuildingForm();
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadBuildingTable();
            } catch (SQLException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            this.tbBuilding.getSelectionModel().addListSelectionListener(rowBuildingListener);
        }
    }//GEN-LAST:event_btnDeleteBuildingActionPerformed

    private void addNewBuildingPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewBuildingPanelMouseClicked
        this.btnDeleteBuilding.setEnabled(false);
        this.btnUpdateBuilding.setEnabled(false);
        this.resetBuildingForm();
    }//GEN-LAST:event_addNewBuildingPanelMouseClicked

    private void btnResetBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBuildingActionPerformed
        this.resetBuildingForm();
        this.btnDeleteBuilding.setEnabled(false);
        this.btnUpdateBuilding.setEnabled(false);
    }//GEN-LAST:event_btnResetBuildingActionPerformed

    private void tabMainLandlordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMainLandlordMouseClicked
        if (this.tabMainLandlord.getSelectedIndex() == 0) {
        }
        if (this.tabMainLandlord.getSelectedIndex() == 2) {
            try {
                Vector comboBuildingName = bh.getAllBuildingNameByLandlordID(curr);
                this.cbBudildingName.setModel(new javax.swing.DefaultComboBoxModel(comboBuildingName));
            } catch (ClassNotFoundException ex) {
                this.taLandlordLog.append(ex.getMessage());
            } catch (SQLException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
        }
        if (this.tabMainLandlord.getSelectedIndex() == 3) {
            Vector cbAptNumModel;
            Vector cbNewPartment;
            try {
                cbAptNumModel = ah.getApartmentNumberByLandlordID(curr);
                java.sql.ResultSet rs = ah.getApartmentNotRent();
                cbNewPartment = new Vector();
                while (rs.next()) {
                    cbNewPartment.add(rs.getString(1));
                }
                this.cbAptChangeNumber.setModel(new javax.swing.DefaultComboBoxModel(cbAptNumModel));
                this.cbNewApartment.setModel(new javax.swing.DefaultComboBoxModel(cbNewPartment));
                this.txtDateChange.setDate(new java.util.Date());
            } catch (ClassNotFoundException ex) {
                this.taLandlordLog.append("Cannot Load SQL Driver");
            } catch (SQLException ex) {
                this.taLandlordLog.append("Cannot Load Apartment Number");
            }
        }

    }//GEN-LAST:event_tabMainLandlordMouseClicked

    private void addNewApartmentPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewApartmentPanelMouseClicked
        this.btnAptDel.setEnabled(false);
        this.btnAptUpd.setEnabled(false);
        try {
            this.resetApartmentForm();
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_addNewApartmentPanelMouseClicked

    private void btnAptResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAptResetActionPerformed
        try {
            this.resetApartmentForm();
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_btnAptResetActionPerformed

    private void btnAptUpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAptUpdActionPerformed
        Building building = new Building();
        building.setBuildingName(this.cbBudildingName.getSelectedItem().toString());
        Apartment apartment = new Apartment(
                this.txtApartmentNum.getText(),
                Double.valueOf(this.txtApartmentSize.getText()),
                this.txtApartmenType.getText(),
                Double.valueOf(this.txtApartmentRentalFee.getText()),
                building);
        try {
            boolean result = bh.updateApartment(apartment);
            if (result) {
                javax.swing.JOptionPane.showMessageDialog(this, "Update  Apartment Successful !");
                this.taLandlordLog.append("Update Apartment successful...\n");
                this.tbApartment.getSelectionModel().removeListSelectionListener(RowApartmentListener);
                this.tbApartment.repaint();
            }

        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadTableApartment();
            } catch (SQLException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            this.tbApartment.getSelectionModel().addListSelectionListener(RowApartmentListener);
        }
    }//GEN-LAST:event_btnAptUpdActionPerformed

    private void btnAptDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAptDelActionPerformed
        Building building = new Building();
        building.setBuildingName(this.cbBudildingName.getSelectedItem().toString());
        Apartment apartment = new Apartment(
                this.txtApartmentNum.getText(),
                Double.valueOf(this.txtApartmentSize.getText()),
                this.txtApartmenType.getText(),
                Double.valueOf(this.txtApartmentRentalFee.getText()),
                building);
        try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you Sure?", "Confirm Delete Apartment", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                bh.removeApartment(apartment);
                this.taLandlordLog.append("Delete Apartment successful...\n");
                this.tbApartment.getSelectionModel().removeListSelectionListener(RowApartmentListener);
                this.tbApartment.repaint();
            }

        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadTableApartment();
            } catch (SQLException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            this.tbApartment.getSelectionModel().addListSelectionListener(RowApartmentListener);
        }
    }//GEN-LAST:event_btnAptDelActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        try {
            ApartmentChange apartmentChange = new ApartmentChange();
            apartmentChange.setApartmentNumber(this.cbAptChangeNumber.getSelectedItem().toString());
            apartmentChange.setChangeDate(utils.ConvertDate.convertToSqlDate(this.txtDateChange.getDate()));
            apartmentChange.setNewApartmentNumber(this.cbNewApartment.getSelectedItem().toString());
            boolean isExist = ach.checkApartmentChangeNumberExist(apartmentChange);
            try {
                if (isExist) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Apartment you need to change has exist");
                    return;
                }
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure ?", "Confirm Change Apartment", javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    boolean result = ach.newRecord(apartmentChange);
                    if (result) {
                        this.taLandlordLog.append("Record new apartment change successful...\n");
                        this.tbApartmentChange.getSelectionModel().removeListSelectionListener(rowApartmentChangeLsitener);
                    }
                }
            } catch (ClassNotFoundException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
            } finally {
                try {
                    if (isExist) {
                        return;
                    }
                    this.loadApartmentChange();
                    this.tbApartmentChange.getSelectionModel().addListSelectionListener(rowApartmentChangeLsitener);
                } catch (SQLException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
                }

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnChangeActionPerformed

    private void changeApartmentPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changeApartmentPanelMouseClicked
        this.btnApartmentChangeDel.setEnabled(false);
        this.btnApartmentChangeUpd.setEnabled(false);
    }//GEN-LAST:event_changeApartmentPanelMouseClicked

    private void btnApartmentChangeUpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApartmentChangeUpdActionPerformed
        ApartmentChange apartmentChange = new ApartmentChange();
        apartmentChange.setApartmentNumber(this.cbAptChangeNumber.getSelectedItem().toString());
        apartmentChange.setChangeDate(utils.ConvertDate.convertToSqlDate(this.txtDateChange.getDate()));
        apartmentChange.setNewApartmentNumber(this.cbNewApartment.getSelectedItem().toString());
        try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure ?", "Confirm Update Apartment", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                boolean result = ach.updateRecord(apartmentChange);
                if (result) {
                    this.taLandlordLog.append("Update apartment change successful...\n");
                    this.tbApartmentChange.getSelectionModel().removeListSelectionListener(rowApartmentChangeLsitener);
                }
            }
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadApartmentChange();
            } catch (SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (ClassNotFoundException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            this.tbApartmentChange.getSelectionModel().addListSelectionListener(rowApartmentChangeLsitener);
        }
    }//GEN-LAST:event_btnApartmentChangeUpdActionPerformed

    private void btnResetApartmentChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetApartmentChangeActionPerformed
        try {
            this.resetApartmentChangeForm();
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        }
    }//GEN-LAST:event_btnResetApartmentChangeActionPerformed

    private void tbApartmentChangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbApartmentChangeMouseClicked
        this.btnApartmentChangeUpd.setEnabled(true);
        this.btnApartmentChangeDel.setEnabled(true);
    }//GEN-LAST:event_tbApartmentChangeMouseClicked

    private void btnChangePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePassActionPerformed
        this.dlgChangePassword.setVisible(true);
        this.dlgChangePassword.pack();
    }//GEN-LAST:event_btnChangePassActionPerformed

    private void btnApartmentChangeDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApartmentChangeDelActionPerformed
        ApartmentChange apartmentChange = new ApartmentChange();
        apartmentChange.setApartmentNumber(this.cbAptChangeNumber.getSelectedItem().toString());
        try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are You Sure  ?", "Cofirm delete apartment change", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                boolean result = ach.deleteRecord(apartmentChange);
                if (result) {
                    this.taLandlordLog.append("Delete apartment change successful...\n");
                    this.tbApartmentChange.getSelectionModel().removeListSelectionListener(rowApartmentChangeLsitener);
                }
            }
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append(ex.getMessage());
        } finally {
            try {
                this.loadApartmentChange();
            } catch (SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (ClassNotFoundException ex) {
                this.taLandlordLog.append(ex.getMessage());
            }
            this.tbApartmentChange.getSelectionModel().addListSelectionListener(rowApartmentChangeLsitener);
        }
    }//GEN-LAST:event_btnApartmentChangeDelActionPerformed

    private void tbBuildingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBuildingMouseClicked
        this.btnUpdateBuilding.setEnabled(true);
        this.btnDeleteBuilding.setEnabled(true);
        this.setValueToBuildingTextFields(txtBuildingName, txtAddress, txtZipCode, txtLandlordID);
    }//GEN-LAST:event_tbBuildingMouseClicked

    private void tbApartmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbApartmentMouseClicked
        this.btnAptUpd.setEnabled(true);
        this.btnAptDel.setEnabled(true);
        this.saveValueToApartmentTextField(txtApartmentNum, txtApartmentSize, txtApartmenType, txtApartmentRentalFee);
        this.saveValueToApartmentCombobox(cbBudildingName);
    }//GEN-LAST:event_tbApartmentMouseClicked

    private void btRefreshBuildingTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshBuildingTableActionPerformed
        utils.ListenerManager.disableListenerOnTable(this.tbBuilding, rowBuildingListener);
        this.tbBuilding.repaint();
        try {
            this.loadBuildingTable();
        } catch (SQLException ex) {
            this.taLandlordLog.append("" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbBuilding, rowBuildingListener);
        }
    }//GEN-LAST:event_btRefreshBuildingTableActionPerformed

    private void btnRefeshApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefeshApartmentActionPerformed
        utils.ListenerManager.disableListenerOnTable(tbApartment, RowApartmentListener);
        this.tbApartment.repaint();
        try {
            this.loadTableApartment();
        } catch (SQLException ex) {
            this.taLandlordLog.append("" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(tbApartment, RowApartmentListener);
        }
    }//GEN-LAST:event_btnRefeshApartmentActionPerformed

    private void btViewDetailBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btViewDetailBuildingActionPerformed
        this.tabMainLandlord.setSelectedIndex(1);
    }//GEN-LAST:event_btViewDetailBuildingActionPerformed

    private void btChangePasswordProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChangePasswordProfileActionPerformed
        this.dlgChangePassword.setVisible(true);
        this.dlgChangePassword.pack();
    }//GEN-LAST:event_btChangePasswordProfileActionPerformed

    private void btChangePasswordLandlordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btChangePasswordLandlordActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(txtOldPassword, txtNewPassword, txtConfirmNewPassword)) {
            utils.ShowMessage.showMessageDialog(this, "Please enter empty fields");
            return;
        }
        if (!utils.FormChecker.checkPasswordField(txtOldPassword, txtNewPassword, txtConfirmNewPassword)) {
            utils.ShowMessage.showMessageDialog(this, "Password cannot less than 6 character");
            return;
        }
        try {
            if (!utils.FormChecker.checkMatches(txtNewPassword, txtConfirmNewPassword)) {
                utils.ShowMessage.showMessageDialog(this, "Password not match !");
                return;
            }
        } catch (NoSuchAlgorithmException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        }
        try {
            boolean hasExist = lh.changeLandlordPassword(landlord);
            if (hasExist) {
                Landlord newLandlord = new Landlord();
                newLandlord.setPassword(utils.Encrypt.enPass(String.valueOf(this.txtNewPassword.getPassword())));
                boolean updatePassword = lh.changeLandlordPassword(newLandlord);
                if (updatePassword) {
                    utils.ShowMessage.showMessageDialog(this, "Change Password successful !\nNow..Restart Landlord Panel and Login again !");
                    this.dlgChangePassword.dispose();
                    this.dispose();
                    LoginFrame login = new LoginFrame();
                    login.setVisible(true);
                }
            }
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        }
    }//GEN-LAST:event_btChangePasswordLandlordActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dlgChangePassword.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void chkShowPasswordLandlordItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkShowPasswordLandlordItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            this.txtLandlordPasswordProfile.setEchoChar((char) 0);
        }
        if (evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED) {
            this.txtLandlordPasswordProfile.setEchoChar('*');
        }
    }//GEN-LAST:event_chkShowPasswordLandlordItemStateChanged

    private void btNextReportPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextReportPaymentActionPerformed
        utils.Table.rowNavigation(this.tbPaymentReport, Table.NEXT);
    }//GEN-LAST:event_btNextReportPaymentActionPerformed

    private void btPreviousReportPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPreviousReportPaymentActionPerformed
        utils.Table.rowNavigation(this.tbPaymentReport, Table.PREV);
    }//GEN-LAST:event_btPreviousReportPaymentActionPerformed

    private void btPreviousBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPreviousBuildingActionPerformed
        utils.Table.rowNavigation(this.tbBuilding, Table.PREV);
    }//GEN-LAST:event_btPreviousBuildingActionPerformed

    private void btNextBuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextBuildingActionPerformed
        utils.Table.rowNavigation(this.tbBuilding, Table.NEXT);
    }//GEN-LAST:event_btNextBuildingActionPerformed

    private void btPreviousApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPreviousApartmentActionPerformed
        utils.Table.rowNavigation(this.tbApartment, Table.PREV);
    }//GEN-LAST:event_btPreviousApartmentActionPerformed

    private void btNextApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextApartmentActionPerformed
        utils.Table.rowNavigation(this.tbApartment, Table.NEXT);
    }//GEN-LAST:event_btNextApartmentActionPerformed

    private void btFirstApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFirstApartmentActionPerformed
        utils.Table.rowNavigation(this.tbApartment, Table.FIRST);
    }//GEN-LAST:event_btFirstApartmentActionPerformed

    private void btLastApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLastApartmentActionPerformed
        utils.Table.rowNavigation(this.tbApartment, Table.LAST);
    }//GEN-LAST:event_btLastApartmentActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.tabMainLandlord.setSelectedIndex(2);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btBuildingSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuildingSearchActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(this.txtBuildingSearchText)) {
            utils.ShowMessage.showMessageDialog(this, "Please enter keyword !");
            return;
        }
        try {
            int columName = this.cbBuildingSearchType.getSelectedIndex();
            utils.ListenerManager.disableListenerOnTable(this.tbBuilding, rowBuildingListener);
            String searchText = this.txtBuildingSearchText.getText();
            switch (columName) {
                case 0:
                    java.sql.ResultSet rs1 = utils.Search.searchFor(Search.TB_BUILDING, "buildingName", searchText);
                    if (rs1.isBeforeFirst()) {
                        db.loadTable(this.tbBuildingModel, rs1);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbBuilding.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbBuilding.repaint();
                        db.loadTable(this.tbBuildingModel, rs1);
                    }
                    break;
                case 1:
                    java.sql.ResultSet rs2 = utils.Search.searchFor(Search.TB_BUILDING, "address", searchText);
                    if (rs2.isBeforeFirst()) {
                        db.loadTable(this.tbBuildingModel, rs2);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbBuilding.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbBuilding.repaint();
                        db.loadTable(this.tbBuildingModel, rs2);
                    }
                    break;
                case 2:
                    java.sql.ResultSet rs3 = utils.Search.searchFor(Search.TB_BUILDING, "cityStateZip", searchText);
                    if (rs3.isBeforeFirst()) {
                        db.loadTable(this.tbBuildingModel, rs3);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbBuilding.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbBuilding.repaint();
                        db.loadTable(this.tbBuildingModel, rs3);
                    }
                    break;
                case 3:
                    java.sql.ResultSet rs4 = utils.Search.searchFor(Search.TB_BUILDING, "landlordID", searchText);
                    if (rs4.isBeforeFirst()) {
                        db.loadTable(this.tbBuildingModel, rs4);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbBuilding.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbBuilding.repaint();
                        db.loadTable(this.tbBuildingModel, rs4);
                    }
                    break;
            }
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(this.tbBuilding, rowBuildingListener);
        }
    }//GEN-LAST:event_btBuildingSearchActionPerformed

    private void btSearchApartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchApartmentActionPerformed
        if (!utils.FormChecker.checkEmptyTextFields(this.txtApartmentSearchText)) {
            utils.ShowMessage.showMessageDialog(this, "Please enter keyword !");
            return;
        }
        try {
            int aptType = this.cbApartmentSearchType.getSelectedIndex();
            utils.ListenerManager.disableListenerOnTable(this.tbApartment, RowApartmentListener);
            String searchText = this.txtApartmentSearchText.getText();
            switch (aptType) {
                case 0:
                    java.sql.ResultSet rs1 = Search.searchFor(Search.TB_APARTMENT, "apartmentNumber", searchText);
                    if (rs1.isBeforeFirst()) {
                        db.loadTable(tbApartmentModel, rs1);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbApartment.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbApartment.repaint();
                        db.loadTable(tbApartmentModel, rs1);
                    }
                    break;
                case 1:
                    java.sql.ResultSet rs2 = Search.searchFor(Search.TB_APARTMENT, "size", searchText);
                    if (rs2.isBeforeFirst()) {
                        db.loadTable(tbApartmentModel, rs2);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbApartment.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbApartment.repaint();
                        db.loadTable(tbApartmentModel, rs2);
                    }
                    break;
                case 2:
                    java.sql.ResultSet rs3 = Search.searchFor(Search.TB_APARTMENT, "aptType", searchText);
                    if (rs3.isBeforeFirst()) {
                        db.loadTable(tbApartmentModel, rs3);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbApartment.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbApartment.repaint();
                        db.loadTable(tbApartmentModel, rs3);
                    }
                    break;
                case 3:
                    java.sql.ResultSet rs4 = Search.searchFor(Search.TB_APARTMENT, "rentalFee", searchText);
                    if (rs4.isBeforeFirst()) {
                        db.loadTable(tbApartmentModel, rs4);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbApartment.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbApartment.repaint();
                        db.loadTable(tbApartmentModel, rs4);
                    }
                    break;
                case 4:
                    java.sql.ResultSet rs5 = Search.searchFor(Search.TB_APARTMENT, "buildingName", searchText);
                    if (rs5.isBeforeFirst()) {
                        db.loadTable(tbApartmentModel, rs5);
                        this.taLandlordLog.append("Found " + db.quote(String.valueOf(this.tbApartment.getRowCount())) + " row with keyword " + db.quote(searchText) + "\n");
                    } else {
                        this.taLandlordLog.append("Not Found any result with keyword " + db.quote(searchText) + "\n");
                        this.tbApartment.repaint();
                        db.loadTable(tbApartmentModel, rs5);
                    }
                    break;
                default:
                    break;
            }
        } catch (ClassNotFoundException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        } catch (SQLException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        } finally {
            utils.ListenerManager.enableListenerOnTable(tbApartment, RowApartmentListener);
        }
    }//GEN-LAST:event_btSearchApartmentActionPerformed

    private void btViewLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btViewLogsActionPerformed
        this.dlgLogger.setVisible(true);
        this.dlgLogger.pack();
    }//GEN-LAST:event_btViewLogsActionPerformed

    private void itemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSaveActionPerformed
        this.dlgLogger.dispose();
    }//GEN-LAST:event_itemSaveActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int result = this.saveLogs.showSaveDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                String fileName = this.saveLogs.getSelectedFile().getName();
                String pathName = this.saveLogs.getSelectedFile().getAbsolutePath();
                StringBuilder saveFileName = new StringBuilder(pathName);
                SettingHandler saveDate = new SettingHandler();
                saveFileName.insert(pathName.indexOf(fileName), db.formatDate2(saveDate.loadLoggerDate()) + "-");
                try {
                    this.writeContentFile(saveFileName.toString() + ".txt", this.taLoggerViewer.getText());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(LandlordFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void btOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOptionActionPerformed
        this.dlgOptions.setVisible(true);
        this.dlgOptions.pack();
        SettingHandler checkBoxIsCheck = new SettingHandler();
        try {
            boolean loadCheckBoxState = checkBoxIsCheck.loadCheckBoxStateByName(landlordSaveLogs);
            this.landlordSaveLogs.setSelected(loadCheckBoxState);
            checkBoxIsCheck.dbClose();
        } catch (SQLException ex) {
            this.taLandlordLog.append("\n" + ex.getMessage());
        }
    }//GEN-LAST:event_btOptionActionPerformed

    private void landlordSaveLogsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_landlordSaveLogsItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            try {
                SettingHandler saveLogsCheckBoxState = new SettingHandler();
                SettingHandler checkExist = new SettingHandler();
                if (checkExist.isCheckBoxExist(landlordSaveLogs)) {
                    checkExist.dbClose();
                    return;
                }
                saveLogsCheckBoxState.saveCheckBoxState(landlordSaveLogs);
                saveLogsCheckBoxState.dbClose();
            } catch (SQLException ex) {
                this.taLandlordLog.append("\n" + ex.getMessage());
            }
        }
        if (evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED) {
            SettingHandler deleteIt = new SettingHandler();
            try {
                deleteIt.deleteCheckBoxState(this.landlordSaveLogs);
                deleteIt.dbClose();
            } catch (SQLException ex) {
                this.taLandlordLog.append("\n" + ex.getMessage());
            }
        }
    }//GEN-LAST:event_landlordSaveLogsItemStateChanged

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
            java.util.logging.Logger.getLogger(LandlordFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LandlordFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LandlordFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LandlordFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new LandlordFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Copy;
    private javax.swing.JMenuItem Copy1;
    private javax.swing.JMenuItem Copy2;
    private javax.swing.JMenuItem Cut;
    private javax.swing.JMenuItem Cut1;
    private javax.swing.JMenuItem Cut2;
    private javax.swing.JMenuItem Delete;
    private javax.swing.JMenuItem Delete1;
    private javax.swing.JMenuItem Delete2;
    private javax.swing.JMenuItem DeleteAll;
    private javax.swing.JMenuItem DeleteAll1;
    private javax.swing.JMenuItem DeleteAll2;
    private javax.swing.JMenuItem Paste;
    private javax.swing.JMenuItem Paste1;
    private javax.swing.JMenuItem Paste2;
    private javax.swing.JMenuItem SelectAll;
    private javax.swing.JMenuItem SelectAll1;
    private javax.swing.JMenuItem SelectAll2;
    private javax.swing.JPopupMenu ZipContextMenu;
    private javax.swing.JPanel addNewApartmentPanel;
    private javax.swing.JPanel addNewBuildingPanel;
    private javax.swing.JPopupMenu addressContextMenu;
    private javax.swing.JButton btBuildingSearch;
    private javax.swing.JButton btChangePasswordLandlord;
    private javax.swing.JButton btChangePasswordProfile;
    private javax.swing.JButton btFirstApartment;
    private javax.swing.JButton btLastApartment;
    private javax.swing.JButton btNextApartment;
    private javax.swing.JButton btNextBuilding;
    private javax.swing.JButton btNextReportPayment;
    private javax.swing.JButton btOption;
    private javax.swing.JButton btPreviousApartment;
    private javax.swing.JButton btPreviousBuilding;
    private javax.swing.JButton btPreviousReportPayment;
    private javax.swing.JButton btRefreshBuildingTable;
    private javax.swing.JButton btSearchApartment;
    private javax.swing.JButton btViewDetailBuilding;
    private javax.swing.JButton btViewLogs;
    private javax.swing.JButton btnAddBuilding;
    private javax.swing.JButton btnApartmentChangeDel;
    private javax.swing.JButton btnApartmentChangeUpd;
    private javax.swing.JButton btnAptAdd;
    private javax.swing.JButton btnAptDel;
    private javax.swing.JButton btnAptReset;
    private javax.swing.JButton btnAptUpd;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnChangePass;
    private javax.swing.JButton btnDeleteBuilding;
    private javax.swing.JButton btnHomeExit;
    private javax.swing.JButton btnRefeshApartment;
    private javax.swing.JButton btnResetApartmentChange;
    private javax.swing.JButton btnResetBuilding;
    private javax.swing.JButton btnUpdateBuilding;
    private javax.swing.JPopupMenu buildingName;
    private javax.swing.JComboBox cbApartmentSearchType;
    private javax.swing.JComboBox cbAptChangeNumber;
    private javax.swing.JComboBox cbBudildingName;
    private javax.swing.JComboBox cbBuildingSearchType;
    private javax.swing.JComboBox cbNewApartment;
    private javax.swing.JPanel changeApartmentPanel;
    private javax.swing.JCheckBox chkShowPasswordLandlord;
    private javax.swing.JDialog dlgChangePassword;
    private javax.swing.JDialog dlgLogger;
    private javax.swing.JDialog dlgOptions;
    private javax.swing.JMenuItem itemSave;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JCheckBox landlordSaveLogs;
    private javax.swing.JFileChooser saveLogs;
    private javax.swing.JPopupMenu.Separator sepMenu;
    private javax.swing.JPopupMenu.Separator sepMenu1;
    private javax.swing.JPopupMenu.Separator sepMenu2;
    private javax.swing.JEditorPane taContentLandlordEmail;
    private javax.swing.event.DocumentListener taLandlordListener;
    private javax.swing.JTextArea taLandlordLog;
    private javax.swing.JTextArea taLoggerViewer;
    private javax.swing.JTabbedPane tabMainLandlord;
    javax.swing.event.ListSelectionListener RowApartmentListener;
    private javax.swing.JTable tbApartment;
    private javax.swing.table.DefaultTableModel tbApartmentModel;
    private javax.swing.table.DefaultTableModel apartmentChangeModel;
    private javax.swing.JTable tbApartmentChange;
    private javax.swing.event.ListSelectionListener rowApartmentChangeLsitener;
    private javax.swing.event.ListSelectionListener rowBuildingListener;
    private javax.swing.JTable tbBuilding;
    private DefaultTableModel tbBuildingModel;
    private javax.swing.table.DefaultTableModel emailLandlordModel;
    private javax.swing.JTable tbLandlordEmail;
    private javax.swing.event.ListSelectionListener rowEmailLandlordListener;
    private javax.swing.table.DefaultTableModel paymentReportModel;
    private javax.swing.JTable tbPaymentReport;
    private javax.swing.event.ListSelectionListener rowPaymentReport;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtApartmenType;
    private javax.swing.JTextField txtApartmentHasRent;
    private javax.swing.JTextField txtApartmentNotRent;
    private javax.swing.JTextField txtApartmentNum;
    private javax.swing.JTextField txtApartmentRentalFee;
    private javax.swing.JTextField txtApartmentReport;
    private javax.swing.JTextField txtApartmentSearchText;
    private javax.swing.JTextField txtApartmentSize;
    private javax.swing.JTextField txtBalanceReport;
    private javax.swing.JTextField txtBuildingName;
    private javax.swing.JTextField txtBuildingSearchText;
    private javax.swing.JPasswordField txtConfirmNewPassword;
    private com.toedter.calendar.JDateChooser txtDateChange;
    private javax.swing.JTextField txtEndDateReport;
    private javax.swing.JTextField txtFromLandlordEmail;
    private javax.swing.JTextField txtLandlordID;
    private javax.swing.JTextField txtLandlordIDProfile;
    private javax.swing.JPasswordField txtLandlordPasswordProfile;
    private javax.swing.JPasswordField txtNewPassword;
    private javax.swing.JPasswordField txtOldPassword;
    private javax.swing.JTextField txtOneBedRoom;
    private javax.swing.JTextField txtRentalDate;
    private javax.swing.JTextField txtRentalFeeReport;
    private javax.swing.JTextField txtStartDateReport;
    private javax.swing.JTextField txtSubjectLandlordEmail;
    private javax.swing.JTextField txtTenantIDReport;
    private javax.swing.JTextField txtThreeBedroom;
    private javax.swing.JTextField txtToLandlordEmail;
    private javax.swing.JTextField txtTotalApartment;
    private javax.swing.JTextField txtTotalBuilding;
    private javax.swing.JTextField txtTwoBedroom;
    private javax.swing.JTextField txtZipCode;
    private javax.swing.JTextField txtbuildingNameReport;
    // End of variables declaration//GEN-END:variables
}
