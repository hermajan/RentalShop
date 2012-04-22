
package rentalshop;

import java.sql.SQLException;
import java.util.List;

public interface CarManager {
  public boolean create(Car car) throws FailureException;
  public boolean modify(Long id,Car car);
  public boolean delete(Long id);
  public List<Car> readAll();
  public Car readByID(Long id);
  
}
