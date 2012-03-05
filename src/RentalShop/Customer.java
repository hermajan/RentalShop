/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RentalShop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DJohnny
 */
public class Customer {
  long ID;
  String name;
  String surname;
  int drivingLicenseNumber;
  int identificationCardNumber;
  BigDecimal doubt;
  
  public Customer() { }

  public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public BigDecimal getDoubt() {
    return doubt;
  }

  public void setDoubt(BigDecimal doubt) {
    this.doubt = doubt;
  }

  public int getDrivingLicenseNumber() {
    return drivingLicenseNumber;
  }

  public void setDrivingLicenseNumber(int drivingLicenseNumber) {
    this.drivingLicenseNumber = drivingLicenseNumber;
  }

  public int getIdentificationCardNumber() {
    return identificationCardNumber;
  }

  public void setIdentificationCardNumber(int identificationCardNumber) {
    this.identificationCardNumber = identificationCardNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }
  
  
  
}
