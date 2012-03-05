/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RentalShop;

import java.util.List;

/**
 *
 * @author DJohnny
 */
public interface OrderManager {
  public boolean create(Order order);
  public boolean modify(long id,Order order);
  public boolean delete(long id);
  public List<Order> readAll();
  public Order readByID(long id);
}
