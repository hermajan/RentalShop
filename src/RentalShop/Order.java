/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RentalShop;

import java.util.Date;

/**
 *
 * @author DJohnny
 */
public class Order {
  long ID;
  Date from;
  Date to;
  Car car;
  Customer customer;
  
  public Order() {}

  public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public Car getCar() {
    return car;
  }

  public void setCar(Car car) {
    this.car = car;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Date getFrom() {
    return from;
  }

  public void setFrom(Date from) {
    this.from = from;
  }

  public Date getTo() {
    return to;
  }

  public void setTo(Date to) {
    this.to = to;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Order other = (Order) obj;
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
