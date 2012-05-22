package rentalshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
        

public class CarManagerImpl implements CarManager {

    private DataSource ds;
    public static final Logger logger = Logger.getLogger(CarManagerImpl.class.getName());
    
    public CarManagerImpl(){
        logger.addHandler(Window.logg);
    }
    
    public void isConnected() throws IllegalStateException {
        if (ds == null) {            
            throw new IllegalStateException("DataSource is not set");
        }
    }

    public void isValidCar(Car car) throws IllegalArgumentException {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        //if(car.getID() != null) { throw new IllegalArgumentException("car id is already set"); }
        if (car.getProducer() == null) { throw new IllegalArgumentException("car producer is empty"); }
        if (car.getModel() == null) { throw new IllegalArgumentException("car model is empty"); }
        if (car.getManufactured() == null) { throw new IllegalArgumentException("car manufactured date is empty"); }
        if (car.getPrice() == null) { throw new IllegalArgumentException("car price is empty"); }
    }

    public void setCon(DataSource ds) {
        this.ds = ds;       
    }
    
    

    @Override
    public boolean create(Car car) throws FailureException {
        logger.log(Level.INFO, "Attempt to add car: {0}", car);
        isConnected();
        isValidCar(car);
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            st = con.prepareStatement("INSERT INTO CARS (PRODUCER,MODEL,SPZ,MANUFACTURED,PRICE) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, car.getProducer());
            st.setString(2, car.getModel());
            st.setString(3, car.getSpz());
            st.setDate(4, car.getManufactured());
            st.setBigDecimal(5, car.getPrice());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 car created " + car);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            car.setID(getKey(keyRS, car));
            con.commit();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "SQL exception when adding car:" + car, ex);
            throw new FailureException("Error when inserting car " + car, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);            
        }
        return true;
    }

    private Long getKey(ResultSet keyRS, Car car) throws FailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new FailureException("Error: Generated key retriving failed when trying to insert car " + car + " - wrong key fields count: "
                        + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new FailureException("Error: Generated key retriving failed when trying to insert car " + car + " - more keys found");
            }
            return result;
        } else {
            throw new FailureException("Error: Generated key retriving failed when trying to insert car " + car + " - no key found");
        }
    }

    @Override
    public boolean modify(Long id, Car car){
        logger.log(Level.INFO, "Attempt to modify car with ID: " + id + car);
        isConnected();
        isValidCar(car);
        
        if (id <= 0 || id == null){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            st = con.prepareStatement("UPDATE CARS SET PRODUCER=?,MODEL=?,SPZ=?,MANUFACTURED=?,PRICE=? WHERE ID=?");
            st.setString(1, car.getProducer());
            st.setString(2, car.getModel());
            st.setString(3, car.getSpz());
            st.setDate(4, car.getManufactured());
            st.setBigDecimal(5, car.getPrice());
            st.setLong(6, id);
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 car updated" + car);
            }

            //ResultSet keyRS = st.getGeneratedKeys();
            //car.setID(getKey(keyRS, car));
            con.commit();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error whena updating car with ID:" + id + car, ex);
            throw new FailureException("Error when updating car " + car, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);
        }
        return true;
    }

    @Override
    public List<Car> readAll() {
        //logger.log(Level.INFO, "Attempt to read all cars");
        isConnected();
        List<Car> returnCars = new ArrayList<Car>();
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT ID, PRODUCER, SPZ, MODEL, MANUFACTURED, PRICE FROM CARS");
            boolean execute = st.execute();
            if (!execute) {
                throw new FailureException("Error, when reading cars");
            }
            ResultSet resultSet = st.getResultSet();
            
            while (resultSet.next()) {
              Car car = new Car();
                car.setID(resultSet.getLong("ID"));
                car.setProducer(resultSet.getString("PRODUCER"));
                car.setModel(resultSet.getString("MODEL"));
                car.setSpz(resultSet.getString("SPZ"));
                car.setManufactured(resultSet.getDate("MANUFACTURED"));
                car.setPrice(resultSet.getBigDecimal("PRICE"));
                returnCars.add(car);
            }

        } catch (SQLException ex) {
            logger.log(Level.INFO, "Error when reading all cars", ex);
            throw new FailureException("Error, when reading cars", ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);            
        }
        return returnCars;
    }
    
    @Override
    public Car readByID(Long id) {
        //logger.log(Level.INFO, "Attempt to read car by ID:" + id);
        isConnected();
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }  Car returnCar = null;
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT MANUFACTURED, MODEL, PRICE, PRODUCER, SPZ FROM CARS WHERE ID =?", Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();            
            if (resultSet.next()) {
                returnCar = new Car();
                returnCar.setID(id);
                returnCar.setManufactured(resultSet.getDate("MANUFACTURED"));
                returnCar.setModel(resultSet.getString("MODEL"));
                returnCar.setPrice(resultSet.getBigDecimal("PRICE"));
                returnCar.setProducer(resultSet.getString("PRODUCER"));
                returnCar.setSpz(resultSet.getString("SPZ"));
            }            
            if (resultSet.next()) {
                throw new FailureException("Error, id duplicity with ID:" + id);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error, when reading car by ID:" + id, ex);
            throw new FailureException("Error, when reading car with Id:" + id, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);
        }
        return returnCar;
    }
    @Override
  public boolean delete(Long id) {
        logger.log(Level.INFO, "Attempt to delete car with ID:", id);
    isConnected();
    if (id <= 0 || id == null){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }
    PreparedStatement st = null;
    Connection con = null;
    try {
        con = ds.getConnection();
        con.setAutoCommit(false);
        st = con.prepareStatement("DELETE FROM CARS WHERE ID=?",Statement.RETURN_GENERATED_KEYS);
        st.setLong(1,id);
        int deletedRows = st.executeUpdate();
        if (deletedRows != 1) { throw new FailureException("Error: More rows deleted when trying to delete car with ID " + id); }
        con.commit();
    } catch (SQLException ex) {
        logger.log(Level.SEVERE, "Error when deleting car with ID:"+id,ex);
        throw new FailureException("Error when deleting car with ID: "+id, ex);
    } finally {
        DatabaseCommon.cleanMe(st, con, true);
    }
    return true;
  }
}
