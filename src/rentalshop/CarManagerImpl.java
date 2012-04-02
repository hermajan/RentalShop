package rentalshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;
        

public class CarManagerImpl implements CarManager {

    private DataSource ds;

    public void isConnected() throws IllegalStateException {
        if (ds == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    public void isValidCar(Car car) throws IllegalArgumentException {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        //if(car.getID()!=null) { throw new IllegalArgumentException("car id is already set"); }
        if (car.getProducer() == null) {
            throw new IllegalArgumentException("car producer is empty");
        }
        if (car.getModel() == null) {
            throw new IllegalArgumentException("car producer is empty");
        }
        if (car.getManufactured() == null) {
            throw new IllegalArgumentException("car manufactured date is empty");
        }
        if (car.getPrice() == null) {
            throw new IllegalArgumentException("car price is empty");
        }
    }

    public void setCon(DataSource ds) {
        this.ds = ds;
    }
    public static final Logger logger = Logger.getLogger(CarManagerImpl.class.getName());

    @Override
    public boolean create(Car car) throws SQLException, FailureException {
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
            st = con.prepareStatement("UPDATE CARS (PRODUCER,MODEL,SPZ,MANUFACTURED,PRICE) VALUES (?,?,?,?,?)");
            st.setString(1, car.getProducer());
            st.setString(2, car.getModel());
            st.setString(3, car.getSpz());
            st.setDate(4, car.getManufactured());
            st.setBigDecimal(5, car.getPrice());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 car updated" + car);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            car.setID(getKey(keyRS, car));
            con.commit();

        } catch (SQLException ex) {
            throw new FailureException("Error when updating car " + car, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);
        }
        return true;
    }

    @Override
    public List<Car> readAll() {
        isConnected();
        List<Car> returnCars = new ArrayList<Car>();
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT ID, MANUFACTURED, MODEL, PRICE, PRODUCER, SPZ FROM CARS");
            boolean execute = st.execute();
            if (!execute) {
                throw new FailureException("Error, when reading cars");
            }
            ResultSet resultSet = st.getResultSet();
            Car returnCar = new Car();
            while (resultSet.next()) {
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
            DatabaseCommon.cleanMe(st, con, false);            
        }
        return returnCars;
    }
    
    @Override
    public Car readByID(Long id) {
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
            throw new FailureException("Error, when reading car with Id:" + id, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);
        }
        return returnCar;
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
        st = con.prepareStatement("DELETE FROM CARS WHERE ID=?",Statement.RETURN_GENERATED_KEYS);
        st.setLong(1,id);
        int deletedRows = st.executeUpdate();
        if (deletedRows != 1) { throw new FailureException("Error: More rows deleted when trying to delete car with ID " + id); }
        con.commit();
    } catch (SQLException ex) {
        throw new FailureException("Error when deleting car with ID: "+id, ex);
    } finally {
        DatabaseCommon.cleanMe(st, con, true);
    }
    return true;
  }
}
