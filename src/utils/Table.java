/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Thaichau
 */
public class Table {
    
    public static final int FIRST = 0;
    public static final int NEXT = 1;
    public static final int PREV = 2;
    public static final int LAST = 3;

    public static void rowNavigation(javax.swing.JTable tb, int direction) {
        if(direction == Table.FIRST){
            tb.setRowSelectionInterval(0, 0);
        }
        
        if (direction == Table.NEXT) {
            int currentRow = tb.getSelectedRow();
            int totalRow = tb.getRowCount();
            if (totalRow == 0) {
                return;
            }
            if (currentRow < 0) {
                tb.setRowSelectionInterval(0, 0);
            }
            if (currentRow < totalRow - 1) {
                tb.setRowSelectionInterval(currentRow + 1, currentRow + 1);
            }
        }

        if (direction == Table.PREV) {
            int currentRow = tb.getSelectedRow();
            int totalRow = tb.getRowCount();
            if (totalRow == 0) {
                return;
            }
            if (currentRow == 0) {
                return;
            }
            if (currentRow > 0) {
                tb.setRowSelectionInterval(currentRow - 1, currentRow - 1);
            }
        }
        
        if(direction == Table.LAST){
            int totalRow = tb.getRowCount();
            tb.setRowSelectionInterval(totalRow - 1, totalRow - 1);
        }
    }
}
