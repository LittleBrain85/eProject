/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfiles;

import java.lang.reflect.Method;

/**
 *
 * @author Thaichau
 */
public class Testing {
    public void get(){
        System.out.println("Get Test");
    }
    public static void main(String[] args) throws ClassNotFoundException {
        Class cls = Class.forName("userProfiles.Employee");
        Method[] m = cls.getMethods();
        for(Method mm : m){
            System.out.println(mm.toString());
        }
    }
}
