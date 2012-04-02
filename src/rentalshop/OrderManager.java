
package rentalshop;

import java.util.List;

public interface OrderManager {
  public boolean create(Order order);
  public boolean modify(Long id,Order ord);
  public boolean delete(Long id);
  public List<Order> readAll();
  public Order readByID(Long id);
}
