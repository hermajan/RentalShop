
package rentalshop;

import java.util.ArrayList;
import java.util.List;

public class CustomerManagerImpl {
  Customer customer=new Customer();
  List<Customer> customers=new ArrayList<Customer>();
  
  public boolean create(Customer customer) {
    return false;
  }
  public boolean modify(long id,Customer customer) {
    return false;
  }
  public boolean delete(long id) {
    return false;
  }
  public List<Customer> readAll() {
    return customers;
  }
  public Customer readByID(long id) {
    return customer;
  }
}
