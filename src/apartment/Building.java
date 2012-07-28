/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import java.util.Vector;
import userProfiles.Landlord;

/**
 *
 * @author Thaichau
 */
public class Building {
    private String buildingName;
    private String address;
    private String citySateZip;
    private Vector apartment;
    private Landlord landlord;
    private static String currentBuildingName = "";

    public Building() {
    }

    public Building(String buildingName, String address, String citySateZip, Vector apartment, Landlord landlord) {
        this.buildingName = buildingName;
        this.address = address;
        this.citySateZip = citySateZip;
        this.apartment = apartment;
        this.landlord = landlord;
    }

    public Building(String buildingName, String address, String citySateZip, Landlord landlord) {
        this.buildingName = buildingName;
        this.address = address;
        this.citySateZip = citySateZip;
        this.landlord = landlord;
    }

    public static String getCurrentBuildingName() {
        return currentBuildingName;
    }

    public static void setCurrentBuildingName(String currentBuildingName) {
        Building.currentBuildingName = currentBuildingName;
    }        

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Vector getApartment() {
        return apartment;
    }

    public void setApartment(Vector apartment) {
        this.apartment = apartment;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCitySateZip() {
        return citySateZip;
    }

    public void setCitySateZip(String citySateZip) {
        this.citySateZip = citySateZip;
    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }
    
}
