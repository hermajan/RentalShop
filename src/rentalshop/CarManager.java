
package rentalshop;

import java.sql.SQLException;
import java.util.List;

public interface CarManager {
  public boolean create(Car car) throws SQLException,FailureException;
  public boolean modify(long id,Car car);
  public boolean delete(long id);
  public List<Car> readAll();
  public Car readByID(long id);
  
}
