/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apartment;

import java.util.Vector;

/**
 *
 * @author Thaichau
 */
public class Apartment {
    private String aptNumber;
    private Double size;
    private String aptType;
    private Double price;
    private Building building;
    private Vector leaseList;
    private Vector maintenanceList;
    private Vector inspectionList;

    private static String currentApartmentNumber = "";
    
    public Apartment() {
    }

    public Apartment(String aptNumber, Double size, String aptType, Double price, Building building) {
        this.aptNumber = aptNumber;
        this.size = size;
        this.aptType = aptType;
        this.price = price;
        this.building = building;
    }

    
    public static String getCurrentApartmentNumber() {
        return currentApartmentNumber;
    }

    public static void setCurrentApartmentNumber(String currentApartmentNumber) {
        Apartment.currentApartmentNumber = currentApartmentNumber;
    }
    
    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
    }

    public String getAptType() {
        return aptType;
    }

    public void setAptType(String aptType) {
        this.aptType = aptType;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Vector getInspectionList() {
        return inspectionList;
    }

    public void setInspectionList(Vector inspectionList) {
        this.inspectionList = inspectionList;
    }

    public Vector getLeaseList() {
        return leaseList;
    }

    public void setLeaseList(Vector leaseList) {
        this.leaseList = leaseList;
    }

    public Vector getMaintenanceList() {
        return maintenanceList;
    }

    public void setMaintenanceList(Vector maintenanceList) {
        this.maintenanceList = maintenanceList;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    
}
