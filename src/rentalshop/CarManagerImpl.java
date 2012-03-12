
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
  
  @Override
  public boolean create(Car car) {
    return true;
  }
  @Override
  public boolean modify(long id,Car car) {
    return false;
  }
  @Override
  public boolean delete(long id) {
    return false;
  }
  @Override
  public List<Car> readAll() {
    return cars;
  }
  @Override
  public Car readByID(long id) {
    return car;
  }
}
