/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Thaichau
 */
public class ConvertDate {

    public ConvertDate() {
    }

    public static java.sql.Date convertToSqlDate(java.util.Date date) {
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public static java.util.Date convertStringToDate(String date) throws ParseException {
        java.text.SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date resultDate = sd.parse(date);
        return resultDate;
    }

    public static String convertDateToString(java.util.Date date) throws ParseException {
        java.text.SimpleDateFormat sd = new SimpleDateFormat("ddMMyyyy");
        return sd.format(date);
    }

}
