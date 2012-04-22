
package rentalshop;

import java.math.BigDecimal;
import java.sql.Date;

public class Car {
  long ID;
  String producer;
  String model;
  String spz;
  Date manufactured;
  BigDecimal price;
  
  public Car() {}

  public long getID() {
    return ID;
  }
  public void setID(long ID) {
    this.ID = ID;
  }

  public Date getManufactured() {
    return manufactured;
  }
  public void setManufactured(Date manufactured) {
    this.manufactured = manufactured;
  }

  public String getModel() {
    return model;
  }
  public void setModel(String model) {
    this.model = model;
  }

  public BigDecimal getPrice() {
    return price;
  }
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getProducer() {
    return producer;
  }
  public void setProducer(String producer) {
    this.producer = producer;
  }

  public String getSpz() {
    return spz;
  }
  public void setSpz(String spz) {
    this.spz = spz;
  }

  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Car other = (Car) obj;
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
 
  
  public Car validateAndToCar(StringBuilder errors) {
        Car car = new Car();
        if (getProducer() == null || getProducer().isEmpty()) { car.setProducer(null); }
          else { car.setProducer(getProducer()); }
        if (getModel() == null || getModel().isEmpty()) { car.setModel(null); }
          else { car.setModel(getModel()); }
        if (getSpz() == null || getSpz().isEmpty()) { car.setSpz(null); }
          else { car.setSpz(getSpz()); }
        if (getManufactured() == null || getManufactured().toString().isEmpty()) { car.setManufactured(null); }
          else { car.setManufactured(getManufactured()); }
        if (getPrice() == null || getPrice().toString().isEmpty()) { car.setPrice(null); }
          else { car.setPrice(getPrice()); }
        if (errors.length() > 0) {
            return null;
        } else {
            return car;
        }
  }
}
