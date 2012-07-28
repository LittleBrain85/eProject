/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Thaichau
 */
public class Encrypt {
    
    public static String enPass(String pass){
        byte[] enCode = org.apache.commons.codec.binary.Base64.encodeBase64(pass.getBytes());
        String str = new String(enCode);
        return str;
    }
    
    public static String dePass(String pass){
        byte[] deCode = org.apache.commons.codec.binary.Base64.decodeBase64(pass);
        String str = new String(deCode);
        return str;
    }
}
