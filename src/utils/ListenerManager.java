/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Thaichau
 */
public class ListenerManager {

    public static void disableListenerOnTable(javax.swing.JTable table, javax.swing.event.ListSelectionListener listener) {
        table.getSelectionModel().removeListSelectionListener(listener);
    }

    public static void enableListenerOnTable(javax.swing.JTable table, javax.swing.event.ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }
}
