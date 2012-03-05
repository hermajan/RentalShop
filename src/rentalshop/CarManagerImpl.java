
package rentalshop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DJohnny
 */
public class CarManagerImpl implements CarManager {
  Car car=new Car();
  List<Car> cars=new ArrayList<Car>();
  
  public boolean create(Car car) {
    return false;
  }
  public boolean modify(long id,Car car) {
    return false;
  }
  public boolean delete(long id) {
    return false;
  }
  public List<Car> readAll() {
    return cars;
  }
  public Car readByID(long id) {
    return car;
  }
}
