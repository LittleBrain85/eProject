/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfiles;

import java.util.Vector;

/**
 *
 * @author Thaichau
 */
public class Landlord {
    private String ID;
    private String password;
    private Vector Building;

    public Landlord(String ID) {
        this.ID = ID;
    }

    public Landlord() {
        
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Vector getBuilding() {
        return Building;
    }

    public void setBuilding(Vector Building) {
        this.Building = Building;
    }
    
    public void addBuilding(Vector building, String ID){
        building.add(ID);
    }
    
    public void removeBuilding(Vector building, String ID){
        building.remove(ID);
    } 
}
