/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Thaichau
 */
public class Resource {

    public Resource() {
    }

    public InputStream getInputStream(String reportFileName) {
        URL url = getClass().getResource("/report/" + reportFileName);
        InputStream input;
        try {
            input = url.openStream();
            return input;
        } catch (IOException ex) {
            return null;
        }
    }
}
