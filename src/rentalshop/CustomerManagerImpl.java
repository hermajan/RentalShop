package rentalshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;
        

public class CustomerManagerImpl implements CustomerManager {

    private DataSource ds;

    public void isConnected() throws IllegalStateException {
        if (ds == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    public void isValidCustomer(Customer cust) throws IllegalArgumentException {
        if (cust == null) {
            throw new IllegalArgumentException("customer is null");
        }
        //if(car.getID()!=null) { throw new IllegalArgumentException("car id is already set"); }
        if (cust.getName() == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (cust.getSurname() == null) {
            throw new IllegalArgumentException("surname is null");
        }
        if (cust.getDrivingLicenseNumber() <= 0) {
            throw new IllegalArgumentException("wrong driving license number given");
        }
        if (cust.getIdentificationCardNumber() <= 0) {
            throw new IllegalArgumentException("wrong ID card number given");
        }
        if (cust.getDebts() == null) {
            throw new IllegalArgumentException("Debts is null");
        }
    }

    public void setCon(DataSource ds) {
        this.ds = ds;
    }
    public static final Logger logger = Logger.getLogger(CarManagerImpl.class.getName());

    @Override
    public boolean create(Customer cust) throws FailureException {
        isConnected();
        isValidCustomer(cust);
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            st = con.prepareStatement("INSERT INTO CUSTOMERS (NAME,SURNAME,DRIVINGLICENSENUMBER,IDENTIFICATIONCARDNUMBER,DEBT) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, cust.getName());
            st.setString(2, cust.getSurname());
            st.setInt(3, cust.getDrivingLicenseNumber());
            st.setInt(4, cust.getIdentificationCardNumber());
            st.setInt(5, cust.getDebts().intValue());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 customer created when adding" + cust);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            cust.setID(getKey(keyRS, cust));
            con.commit();

        } catch (SQLException ex) {
            throw new FailureException("Error when inserting customer " + cust, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);            
        }
        return true;
    }

    private Long getKey(ResultSet keyRS,Customer cust) throws FailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new FailureException("Error: Generated key retriving failed when trying to insert customer " + cust + " - wrong key fields count: "
                        + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new FailureException("Error: Generated key retriving failed when trying to insert customer " + cust + " - more keys found");
            }
            return result;
        } else {
            throw new FailureException("Error: Generated key retriving failed when trying to insert customer " + cust + " - no key found");
        }
    }

    @Override
    public boolean modify(Long id, Customer cust){
        isConnected();
        isValidCustomer(cust);
        
        if (id <= 0 || id == null){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            st = con.prepareStatement("UPDATE CUSTOMERS (NAME,SURNAME,DRIVINGLICENSENUMBER,IDENTIFICATIONCARDNUMBER,DEBT) VALUES (?,?,?,?,?)");
            st.setString(1, cust.getName());
            st.setString(2, cust.getSurname());
            st.setInt(3, cust.getDrivingLicenseNumber());
            st.setInt(4, cust.getIdentificationCardNumber());
            st.setBigDecimal(5, cust.getDebts());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new FailureException("Error: Not only 1 customer updated when added" + cust);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            cust.setID(getKey(keyRS, cust));
            con.commit();

        } catch (SQLException ex) {
            throw new FailureException("Error when updating ustomer " + cust, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, true);
        }
        return true;
    }

    @Override
    public List<Customer> readAll() {
        isConnected();
        List<Customer> custs =  new ArrayList<Customer>();
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT ID,NAME,SURNAME,DRIVINGLICENSENUMBER,IDENTIFICATIONCARDNUMBER,DEBT FROM CUSTOMERS");
            boolean execute = st.execute();
            if (!execute) {
                throw new FailureException("Error, when reading customers");
            }
            ResultSet resultSet = st.getResultSet();
            Customer cust = new Customer();
            while (resultSet.next()) {
                cust.setID(resultSet.getLong("ID"));
                cust.setName(resultSet.getString("NAME"));
                cust.setSurname(resultSet.getString("SURNAME"));
                cust.setDrivingLicenseNumber(resultSet.getInt("DRIVINGLICENSENUMBER"));
                cust.setIdentificationCardNumber(resultSet.getInt("IDENTIFICATIONCARDNUMBER"));
                cust.setDebts(resultSet.getBigDecimal("DEBT"));
                custs.add(cust); 
           }
        } catch (SQLException ex) {
            throw new FailureException("Error, when reading cars", ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);            
        }
        return custs;
    }
    
    @Override
    public Customer readByID(Long id) {
        isConnected();
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Wrong ID given: " + id);
        }  Customer cust = null;
        
        PreparedStatement st = null;
        Connection con = null;
        try {
            con = ds.getConnection();
            st = con.prepareStatement("SELECT ID,NAME,SURNAME,DRIVINGLICENSENUMBER,IDENTIFICATIONCARDNUMBER,DEBT FROM CUSTOMERS WHERE ID =?", Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();            
            if (resultSet.next()) {
                cust = new Customer();
                cust.setID(id);
                cust.setName(resultSet.getString("NAME"));
                cust.setSurname(resultSet.getString("SURNAME"));
                cust.setDrivingLicenseNumber(resultSet.getInt("DRIVINGLICENSENUMBER"));
                cust.setIdentificationCardNumber(resultSet.getInt("IDENTIFICATIONCARDNUMBER"));
                cust.setDebts(resultSet.getBigDecimal("DEBT"));
            }            
            if (resultSet.next()) {
                throw new FailureException("Error, id duplicity with ID:" + id);
            }
        } catch (SQLException ex) {
            throw new FailureException("Error, when reading customer with Id:" + id, ex);
        } finally {
            DatabaseCommon.cleanMe(st, con, false);
        }
        return cust;
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
        st = con.prepareStatement("DELETE FROM CUSTOMERS WHERE ID=?",Statement.RETURN_GENERATED_KEYS);
        st.setLong(1,id);
        int deletedRows = st.executeUpdate();
        if (deletedRows != 1) { throw new FailureException("Error: More rows deleted when trying to delete customer with ID: " + id); }
        con.commit();
    } catch (SQLException ex) {
        throw new FailureException("Error when deleting customer with ID "+id, ex);
    } finally {
        DatabaseCommon.cleanMe(st, con, true);
    }
    return true;
  }
}
