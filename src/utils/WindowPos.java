/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Thaichau
 */
public class WindowPos {
    public static void setPostion(java.awt.Window frame){
        java.awt.Dimension screen;
        java.awt.Dimension window;
        
        screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        window = frame.getSize();
        
        int w = screen.width - window.width;
        int h = screen.height - window.height;
        frame.setLocation(w/4,h/4);
        frame.validate();
    }
}
