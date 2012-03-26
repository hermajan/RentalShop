
package rentalshop;

import java.util.ArrayList;
import java.util.List;

public class OrderManagerImpl implements OrderManager {
  Order order=new Order();
  List<Order> orders=new ArrayList<Order>();
  
  @Override
  public boolean create(Order order) {
    return false;
  }
  @Override
  public boolean modify(long id,Order order) {
    return false;
  }
  @Override
  public boolean delete(long id) {
    return false;
  }
  @Override
  public List<Order> readAll() {
    return orders;
  }
  @Override
  public Order readByID(long id) {
    return order;
  }
}
