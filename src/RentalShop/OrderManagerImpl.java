/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RentalShop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DJohnny
 */
public class OrderManagerImpl implements OrderManager {
  Order order=new Order();
  List<Order> orders=new ArrayList<Order>();
  
  public boolean create(Order order) {
    return false;
  }
  public boolean modify(long id,Order order) {
    return false;
  }
  public boolean delete(long id) {
    return false;
  }
  public List<Order> readAll() {
    return orders;
  }
  public Order readByID(long id) {
    return order;
  }
}
