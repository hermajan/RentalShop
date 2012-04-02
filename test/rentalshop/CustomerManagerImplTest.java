/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CustomerManagerImplTest {
  private CustomerManagerImpl man;
  private DataSource ds;
  
  private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        //we will use in memory database
        ds.setUrl("jdbc:derby:memory:CarManagerTest;create=true");
        return ds;
    }
  
  @Before
  public void setUp() throws SQLException {
    ds = prepareDataSource();
    Connection con = null;
    PreparedStatement st = null;
    try{
        
 
        con = ds.getConnection();
        con.setAutoCommit(false);
        st = con.prepareStatement("CREATE TABLE CUSTOMERS ("
        +"ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
        +"NAME VARCHAR(50),"
	+"SURNAME VARCHAR(50),"
	+"DRIVINGLICENSENUMBER INTEGER,"
	+"IDENTIFICATIONCARDNUMBER INTEGER,"
	+"DEBT DECIMAL)");
        st.executeUpdate();
        con.commit();
        man = new CustomerManagerImpl();
        man.setCon(ds);        
    } finally{
        DatabaseCommon.cleanMe(st, con, true); 
   }
    
  }
  @After
  public void tearDown() throws SQLException {
      Connection con = ds.getConnection();
      con.prepareStatement("DROP TABLE CUSTOMERS").executeUpdate();        
      con.close();    
    }   
  
  private Customer newCustomer(long id, String name, String surname, int drivinglLicenseNumber, int identificationCardNumber, BigDecimal debt) {
    Customer cust = new Customer();
    cust.setID(id);
    cust.setName(name);
    cust.setSurname(surname);
    cust.setDrivingLicenseNumber(drivinglLicenseNumber);
    cust.setIdentificationCardNumber(identificationCardNumber);
    cust.setDebts(debt);
    return cust;
  }
  private void assertDeepEquals(Customer expected, Customer actual) {
        assertEquals(expected.getID(), actual.getID());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getDrivingLicenseNumber(), actual.getDrivingLicenseNumber());
        assertEquals(expected.getIdentificationCardNumber(), actual.getIdentificationCardNumber());
        assertTrue(expected.getDebts().equals(actual.getDebts()));    
  }
  
  @Test
  public void creating() throws SQLException {
      
      Customer cust= newCustomer(1,"Karel","Karlovič", 123456789, 987654321 ,BigDecimal.valueOf(1000));

      man.create(cust);
      Long custID = cust.getID();
      assertNotNull(custID);
      String custName =cust.getName();
      assertNotNull(custName);
      String custSuName = cust.getSurname();
      assertNotNull(custSuName);
      int licenseNumber = cust.getDrivingLicenseNumber();
      assertNotNull(licenseNumber);
      int cardNumber = cust.getIdentificationCardNumber();
      assertNotNull(cardNumber);
      BigDecimal customerDebt = cust.getDebts();
      assertNotNull(customerDebt);
      
      Customer c = man.readByID(custID);
      assertEquals(cust,c);
      assertDeepEquals(cust,c);
      c.setName("Alojz");
      assertNotSame(cust,c);
  }

  @Test
  public void creatingWrongly() throws SQLException {
    try {
      man.create(null);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Customer c2=newCustomer(1,null,"Karlovič", 123456789, 987654321 ,BigDecimal.valueOf(1000));
      man.create(c2); 
      assertNull(c2.getName());
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Customer c3=newCustomer(1,"Karel",null, 123456789, 987654321 ,BigDecimal.valueOf(1000));
      man.create(c3); assertNull(c3.getSurname());
      fail(); 
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Customer c4=newCustomer(1,"Karel","Karlovič", 0, 987654321 ,BigDecimal.valueOf(1000));
      man.create(c4); assertEquals(c4.getDrivingLicenseNumber(), 0);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Customer c4=newCustomer(1,"Karel","Karlovič", 123456789, 0 ,BigDecimal.valueOf(1000));
      man.create(c4); assertEquals(c4.getIdentificationCardNumber(), 0);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Customer c3=newCustomer(1,"Karel", "Karlovič", 123456789, 987654321 , null);
      man.create(c3); 
      assertNull(c3.getDebts());
      fail(); 
    } catch(IllegalArgumentException iae) {}
  }
  
  
  
  @Test
  public void deleting() throws SQLException {
    Customer c1=newCustomer(1,"Karel","Karlovič", 123456789, 987654321 ,BigDecimal.valueOf(1000));
    Customer c2=newCustomer(2,"Lucie","Dvořáčková", 123456790, 987654322 ,BigDecimal.valueOf(100));
    man.create(c1);
    man.create(c2);

    assertNotNull(man.readByID(c1.getID()));
    assertNotNull(man.readByID(c2.getID()));

    man.delete(c1.getID());

    assertNull(man.readByID(c1.getID()));
    assertNotNull(man.readByID(c2.getID()));    
  }
  
  @Test
  public void readWithWrongArguments() throws SQLException {
          assertNull(man.readByID(1L)); 
      
      try{
          man.readByID(null); 
          fail();
      } catch(IllegalArgumentException ex){
          //ok          
      } try{
          man.readByID(0L); 
          fail();
      } catch(IllegalArgumentException ex){
          //ok          
      }  
      try{
          man.readByID(-1L); 
          fail();
      } catch(IllegalArgumentException ex){
          //ok          
      }   
  }
  
}
