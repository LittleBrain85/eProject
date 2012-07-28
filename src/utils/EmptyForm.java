/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Thaichau
 */
public class EmptyForm {
    public static void emptyTextFields(javax.swing.JTextField...tf){
        for(javax.swing.JTextField textField : tf){
            textField.setText(null);
        }
    }
    
    public static void emtyPasswordField(javax.swing.JPasswordField pw){
        pw.setText(null);
    }
    
    public static void emtyTextAreaField(javax.swing.JTextArea ta){
        ta.setText(null);
    }
    
    public static void emptyCombobox(javax.swing.JComboBox cb){
        cb.setSelectedItem(null);
    }
}
