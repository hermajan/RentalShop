
package rentalshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class OrderManagerImpl implements OrderManager {
  Order order=new Order();
  List<Order> orders=new ArrayList<Order>();
  private DataSource ds;

  public void isConnected() throws IllegalStateException {
      if (ds == null) {
          throw new IllegalStateException("DataSource is not set");
      }
  }

  public void isValidOrder(Order ord) throws IllegalArgumentException {
      if(ord == null) {
          throw new IllegalArgumentException("order is null");
      }
      //if(ord.getID()!=null) { throw new IllegalArgumentException("order id is already set"); }
      if(ord.getCar() == null) {
          throw new IllegalArgumentException("car is null");
      }
      if(ord.getCustomer() == null) {
          throw new IllegalArgumentException("customer is null");
      }
      if(ord.getFrom() == null) {
          throw new IllegalArgumentException("from date is null");
      }
      if (ord.getTo() == null) {
          throw new IllegalArgumentException("to date is null");
      }
    }

    public void setCon(DataSource ds) {
        this.ds = ds;
    }
    public static final Logger logger = Logger.getLogger(CarManagerImpl.class.getName());

    @Override
    public boolean create(Order ord) throws FailureException {
        isConnected();
        isValidOrder(ord);
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            st = con.prepareStatement("INSERT INTO ORDERS (FROMO,TOO,CAR,CUSTOMER) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setDate(1, ord.getFrom());
            st.setDate(2, ord.getTo());
            st.setLong(3, ord.getCar().getID());
            st.setLong(4, ord.getCustomer().getID());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 order created when adding" + ord);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            ord.setID(getKey(keyRS, ord));
            con.commit();

        } catch (SQLException ex) {
            throw new FailureException("Error when inserting order " + ord, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);            
        }
        return true;
    }

    private Long getKey(ResultSet keyRS,Order ord) throws FailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new FailureException("Error: Generated key retriving failed when trying to insert order " + ord + " - wrong key fields count: "
                        + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new FailureException("Error: Generated key retriving failed when trying to insert order " + ord + " - more keys found");
            }
            return result;
        } else {
            throw new FailureException("Error: Generated key retriving failed when trying to insert order " + ord + " - no key found");
        }
    }

    @Override
    public boolean modify(Long id, Order ord){
        isConnected();
        isValidOrder(ord);
        
        if (id <= 0 || id == null){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            st = con.prepareStatement("UPDATE ORDERS (FROMO,TO,CAR,CUSTOMER) VALUES (?,?,?,?)");
            st.setDate(1, ord.getFrom());
            st.setDate(2, ord.getTo());
            st.setLong(3, ord.getCar().getID());
            st.setLong(4, ord.getCustomer().getID());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 order updated when added" + ord);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            ord.setID(getKey(keyRS, ord));
            con.commit();

        } catch (SQLException ex) {
            throw new FailureException("Error when updating order " + ord, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);
        }
        return true;
    }

    @Override
    public List<Order> readAll() {
        isConnected();
        List<Order> ords =  new ArrayList<Order>();
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT ID,FROMO,TOO,CAR,CUSTOMER FROM ORDERS");
            boolean execute = st.execute();
            if (!execute) {
                throw new FailureException("Error, when reading orders");
            }
            ResultSet resultSet = st.getResultSet();
            Order ord = new Order();
            while (resultSet.next()) {
                ord.setID(resultSet.getLong("ID"));
                ord.setFrom(resultSet.getDate("FROM"));
                ord.setTo(resultSet.getDate("TO"));
                //ord.setCar(CarManager.readByID(resultSet.getLong("CAR")));
                //ord.setCustomer(resultSet.getLong("CUSTOMER"));
                ords.add(ord); 
           }
        } catch (SQLException ex) {
            throw new FailureException("Error, when reading cars", ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);            
        }
        return ords;
    }
    
    @Override
    public Order readByID(Long id) {
        isConnected();
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }  Order ord = null;
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT ID,FROMO,TOO,CAR,CUSTOMER FROM ORDERS WHERE ID =?", Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();            
            if (resultSet.next()) {
                ord = new Order();
                ord.setID(id);
                ord.setFrom(resultSet.getDate("FROMO"));
                ord.setTo(resultSet.getDate("TOO"));
                Car c=new Car();
                c.setID(resultSet.getLong("CAR"));
                ord.setCar(c);
                Customer cu=new Customer();
                cu.setID(resultSet.getLong("CUSTOMER"));
                ord.setCustomer(cu);
            }            
            if (resultSet.next()) {
                throw new FailureException("Error, id duplicity with ID:" + id);
            }
        } catch (SQLException ex) {
            throw new FailureException("Error, when reading order with Id:" + id, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);
        }
        return ord;
    }
    
    @Override
  public boolean delete(Long id) {
    isConnected();
    if (id <= 0 || id == null){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }
    PreparedStatement st = null;
    Connection con = null;
    try {
        con = ds.getConnection();
        con.setAutoCommit(false);
        st = con.prepareStatement("DELETE FROM ORDERS WHERE ID=?",Statement.RETURN_GENERATED_KEYS);
        st.setLong(1,id);
        int deletedRows = st.executeUpdate();
        if (deletedRows != 1) { throw new FailureException("Error: More rows deleted when trying to delete order with ID: " + id); }
        con.commit();
    } catch (SQLException ex) {
        throw new FailureException("Error when deleting order with ID "+id, ex);
    } finally {
        DatabaseCommon.cleanMe(st, con, true);
    }
    return true;
  }
}
