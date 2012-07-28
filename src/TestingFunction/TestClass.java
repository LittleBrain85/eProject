/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestingFunction;

import database.SettingHandler;
import java.sql.SQLException;
import java.text.ParseException;
import userProfiles.Landlord;

/**
 *
 * @author Thaichau
 */
public class TestClass {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        SettingHandler sh = new SettingHandler();
        Landlord landlord = new Landlord();
        landlord.setID("thaichau");
        sh.getResultSetFakeMailByLandlordID(landlord);
    }
}
