
package rentalshop;

import java.util.List;

public interface CustomerManager {
  public boolean create(Customer customer);
  public boolean modify(Long id,Customer customer);
  public boolean delete(Long id);
  public List<Customer> readAll();
  public Customer readByID(Long id); 
}
