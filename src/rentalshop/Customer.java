
package rentalshop;

import java.math.BigDecimal;

public class Customer {
  long ID;
  String name;
  String surname;
  int drivingLicenseNumber;
  int identificationCardNumber;
  BigDecimal debts;
  
  public Customer() { }

  public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public BigDecimal getDebts() {
    return debts;
  }

  public void setDebts(BigDecimal debt) {
    this.debts = debt;
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
 
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Customer other = (Customer) obj;
    if (this.ID != other.ID) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (int) (this.ID ^ (this.ID >>> 32));
    return hash;
  }
  
  
}
