
package rentalshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DJohnny
 */
public class CarManagerImpl implements CarManager {
  Car car=new Car();
  List<Car> cars=new ArrayList<Car>();
  private Connection con;
  public static final Logger logger = Logger.getLogger(CarManagerImpl.class.getName());
  
  public CarManagerImpl(Connection con) {
    this.con=con;
  }
  
  @Override
  public boolean create(Car car) throws FailureException {
    if(car==null) { throw new IllegalArgumentException("car is null"); }
    //if(car.getID()!=null) { throw new IllegalArgumentException("car id is already set"); }
    if(car.getProducer()==null) { throw new IllegalArgumentException("car producer is empty"); }
    if(car.getModel()==null) { throw new IllegalArgumentException("car producer is empty"); }
    if(car.getManufactured()==null) { throw new IllegalArgumentException("car manufactured date is empty"); }
    if(car.getPrice()==null) { throw new IllegalArgumentException("car price is empty"); }
        
    PreparedStatement st = null;
    try {
        st = con.prepareStatement("INSERT INTO CARS (PRODUCER,MODEL,SPZ,MANUFACTURED,PRICE) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        st.setString(1,car.getProducer());
        st.setString(2,car.getModel());
        st.setString(3,car.getSpz());
        st.setDate(4,(Date)car.getManufactured());
        st.setBigDecimal(5,car.getPrice());
        int addedRows = st.executeUpdate();
        if (addedRows != 1) { throw new FailureException("Error: More rows inserted when trying to insert car " + car); }            

        ResultSet keyRS = st.getGeneratedKeys();
        car.setID(getKey(keyRS,car));

    } catch (SQLException ex) {
        throw new FailureException("Error when inserting car " + car, ex);
    } finally {
        if (st != null) {
            try { st.close(); }
            catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); }
        }
    }
    return true;
  }
  
  private Long getKey(ResultSet keyRS, Car car) throws FailureException, SQLException {
    if (keyRS.next()) {
        if (keyRS.getMetaData().getColumnCount() != 1) {
            throw new FailureException("Error: Generated key retriving failed when trying to insert car "+car+" - wrong key fields count: "
                    + keyRS.getMetaData().getColumnCount());
        }
        Long result = keyRS.getLong(1);
        if (keyRS.next()) {
            throw new FailureException("Error: Generated key retriving failed when trying to insert car "+car+" - more keys found");
        }
        return result;
    } 
    else {
        throw new FailureException("Error: Generated key retriving failed when trying to insert car "+car+" - no key found");
    }
  }
  
  @Override
  public boolean modify(long id,Car car) {
    return false;
  }
  
  @Override
  public boolean delete(long id) {
    PreparedStatement st = null;
    try {
        st = con.prepareStatement("DELETE FROM CARS WHERE ID=?",Statement.RETURN_GENERATED_KEYS);
        st.setLong(1,id);
        int deletedRows = st.executeUpdate();
        if (deletedRows != 1) { throw new FailureException("Error: More rows deleted when trying to insert car " + car); }
    } catch (SQLException ex) {
        throw new FailureException("Error when deleting car "+car, ex);
    } finally {
        if (st != null) {
            try { st.close(); }
            catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); }
        }
    }
    return true;
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
