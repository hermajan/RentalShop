
package rentalshop;

import java.util.List;

/**
 *
 * @author DJohnny
 */
public interface CustomerManager {
  public boolean create(Customer customer);
  public boolean modify(long id,Customer customer);
  public boolean delete(long id);
  public List<Customer> readAll();
  public Customer readByID(long id); 
}
