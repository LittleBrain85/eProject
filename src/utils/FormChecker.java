/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Thaichau
 */
public class FormChecker {

    public static boolean checkEmptyTextFields(javax.swing.JTextField... tf) {
        for (javax.swing.JTextField textFields : tf) {
            if (textFields.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkDoubleTextFieldsValue(javax.swing.JTextField... tf) {
        for (javax.swing.JTextField textField : tf) {
            if (Double.valueOf(textField.getText()) < 0.0) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkIntegerTextFieldsValue(javax.swing.JTextField... tf) {
        for (javax.swing.JTextField textField : tf) {
            if (Integer.valueOf(textField.getText()) < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkConboboxEmpty(javax.swing.JComboBox... cb) {
        for (javax.swing.JComboBox combo : cb) {
            if (combo.getSelectedIndex() < 0 || combo.getSelectedItem().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkFormattedTextField(javax.swing.JFormattedTextField... jf) {
        for (javax.swing.JFormattedTextField fortmatted : jf) {
            if (fortmatted.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkTextAreaField(javax.swing.JTextArea... jta) {
        for (javax.swing.JTextArea textArea : jta) {
            if (textArea.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPasswordField(javax.swing.JPasswordField... pw) {
        for (javax.swing.JPasswordField pass : pw) {
            if (String.valueOf(pass.getPassword()).equals("")
                    || String.valueOf(pass.getPassword()).length() < 6) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkMatches(javax.swing.JPasswordField pw1, javax.swing.JPasswordField pw2) throws NoSuchAlgorithmException {
        if (utils.Encrypt.enPass(String.valueOf(pw1.getPassword())).equals(utils.Encrypt.enPass(String.valueOf(pw2.getPassword())))) {
            return true;
        }
        return false;
    }

    public static boolean checkEmail(javax.swing.JTextField tf) {
        return tf.getText().matches("[a-zA-Z0-9\\.\\-]+\\@[a-zA-Z0-9\\.]+\\.[a-zA-Z]{2,3}");
    }

    public static boolean checkZip(javax.swing.JTextField tf) {
        return tf.getText().matches("\\d{5}");
    }

    public static boolean isNum(javax.swing.JTextField... tf) {
        for (javax.swing.JTextField t : tf) {
            if (t.getText().matches("\\d+")) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkStartAndEndDate(java.util.Date start, java.util.Date end) {
        if (start.getTime() == end.getTime()) {
            return true;
        }
        return false;
    }
}
