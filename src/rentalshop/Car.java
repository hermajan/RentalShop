
package rentalshop;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author DJohnny
 */
public class Car {
  long ID;
  String producer;
  String model;
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

  
}
