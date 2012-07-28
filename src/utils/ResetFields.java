/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Thaichau
 */
public class ResetFields {
    
    public static void resetTextFields(javax.swing.JTextField... tf){
        for(javax.swing.JTextField textField : tf){
            textField.setText(null);
        }
    }
    
    public static void resetCombobox(javax.swing.JComboBox...cb){
        for(javax.swing.JComboBox combo : cb){
            combo.setSelectedIndex(0);
        }
    }
    
    public static void resetFormattedTextFields(javax.swing.JFormattedTextField...ft){
        for(javax.swing.JFormattedTextField formatted : ft){
            formatted.setText(null);
        }
    }
}
