
package rentalshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarManagerImpl implements CarManager {
  Car car=new Car();
  List<Car> cars=new ArrayList<Car>();
  private Connection con;
  public static final Logger logger = Logger.getLogger(CarManagerImpl.class.getName());
  
  public CarManagerImpl(Connection con) {
    this.con=con;
  }
  
  @Override
  public boolean create(Car car) throws SQLException, FailureException {
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
        st.setDate(4,car.getManufactured());
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
            catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); 
              throw new FailureException("Error when closing database", ex);
            }
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
      if(car==null) { throw new IllegalArgumentException("car is null"); }
    //if(car.getID()!=null) { throw new IllegalArgumentException("car id is already set"); }
    if(car.getProducer()==null) { throw new IllegalArgumentException("car producer is empty"); }
    if(car.getModel()==null) { throw new IllegalArgumentException("car producer is empty"); }
    if(car.getManufactured()==null) { throw new IllegalArgumentException("car manufactured date is empty"); }
    if(car.getPrice()==null) { throw new IllegalArgumentException("car price is empty"); }
        
    PreparedStatement st = null;
    try {
        st = con.prepareStatement("UPDATE CARS (PRODUCER,MODEL,SPZ,MANUFACTURED,PRICE) VALUES (?,?,?,?,?) WHERE ID =" + id,Statement.RETURN_GENERATED_KEYS);
        st.setString(1,car.getProducer());
        st.setString(2,car.getModel());
        st.setString(3,car.getSpz());
        st.setDate(4,car.getManufactured());
        st.setBigDecimal(5,car.getPrice());
        int addedRows = st.executeUpdate();
        if (addedRows != 1) { throw new FailureException("Error: More rows updated when trying to update car " + car); } 
        

    } catch (SQLException ex) {
        throw new FailureException("Error when updating car " + car, ex);
    } finally {
        if (st != null) {
            try { st.close(); }
            catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); 
              throw new FailureException("Error when closing database", ex);
            }
        }
    }
    return true;
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
            catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); 
              throw new FailureException("Error when closing database", ex);
            }
        }
    }
    return true;
  }
  
  @Override
  public List<Car> readAll() {
      ArrayList<Car> returnCars= new ArrayList<Car>();
      Car returnCar = new Car();
        try {
            Statement st = con.createStatement();
            boolean execute = st.execute("SELECT * FROM CARS");
            if (!execute) throw new FailureException("Error, when reading cars");
            ResultSet resultSet = st.getResultSet();            
            while(resultSet.next()){
                returnCar.setID(resultSet.getLong("ID"));   
                returnCar.setManufactured(resultSet.getDate("MANUFACTURED"));
                returnCar.setModel(resultSet.getString("MODEL"));
                returnCar.setPrice(resultSet.getBigDecimal("PRICE"));
                returnCar.setProducer(resultSet.getString("PRODUCER"));
                returnCar.setSpz(resultSet.getString("SPZ"));
                returnCars.add(returnCar);
            }
            
        } catch (SQLException ex) {
            throw new FailureException("Error, when reading cars", ex);
        } finally {
            if (con != null) {
              try { con.close(); }
              catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); 
                throw new FailureException("Error when closing database", ex);
              }
            }
        }
        return returnCars;    
  }
  
  @Override
  public Car readByID(long id){
      Car returnCar= new Car();
        try {
            PreparedStatement st = con.prepareStatement("SELECT * FROM CARS WHERE ID =?",Statement.RETURN_GENERATED_KEYS);
            st.setLong(1,id);
            int deletedRows = st.executeUpdate();
            if (deletedRows!=1) throw new FailureException("Error, when reading car with Id:" + id);
            ResultSet resultSet = st.getResultSet();            
            if (resultSet.next()){
                returnCar.setID(id);   
                returnCar.setManufactured(resultSet.getDate("MANUFACTURED"));
                returnCar.setModel(resultSet.getString("MODEL"));
                returnCar.setPrice(resultSet.getBigDecimal("PRICE"));
                returnCar.setProducer(resultSet.getString("PRODUCER"));
                returnCar.setSpz(resultSet.getString("SPZ"));
            }
            if (resultSet.next()){
               throw new FailureException("Error, id duplicity with ID:" + id);
            }
        } catch (SQLException ex) {
            throw new FailureException("Error, when reading car with Id:" + id, ex);
        } finally {
            if (con != null) {
            try { con.close(); }
            catch (SQLException ex) { logger.log(Level.SEVERE, null, ex); }
            }
        }
        return returnCar;
  }
}
