
package rentalshop;

import java.util.List;

public interface OrderManager {
  public boolean create(Order order);
  public boolean modify(long id,Order order);
  public boolean delete(long id);
  public List<Order> readAll();
  public Order readByID(long id);
}
