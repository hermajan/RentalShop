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
public interface CarManager {
  public boolean create(Car car);
  public boolean modify(long id,Car car);
  public boolean delete(long id);
  public List<Car> readAll();
  public Car readByID(long id);
  
}
