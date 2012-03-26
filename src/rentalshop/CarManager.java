
package rentalshop;

import java.util.List;

/**
 *
 * @author DJohnny
 */
public interface CarManager {
  public boolean create(Car car) throws FailureException;
  public boolean modify(long id,Car car);
  public boolean delete(long id);
  public List<Car> readAll();
  public Car readByID(long id);
  
}
