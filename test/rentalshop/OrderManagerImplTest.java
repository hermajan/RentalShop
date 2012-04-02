/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OrderManagerImplTest {
  private OrderManagerImpl man;
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
        st = con.prepareStatement("CREATE TABLE ORDERS ("
          +"ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
          +"FROMO DATE,"
          +"TOO DATE,"
          +"CAR BIGINT, CUSTOMER BIGINT"
          +")"
        );
        st.executeUpdate();
        
        //st = con.prepareStatement("ALTER TABLE ORDERS ADD CONSTRAINT CAR FOREIGN KEY (ID) REFERENCES CARS(ID)");
        //st.execute();
        
        //st = con.prepareStatement("ALTER TABLE ORDERS ADD CONSTRAINT CUSTOMER FOREIGN KEY (ID) REFERENCES CUSTOMERS(ID)");
        //st.execute();
        
        con.commit();
        man = new OrderManagerImpl();
        man.setCon(ds);        
    } finally{
        DatabaseCommon.cleanMe(st, con, true); 
   }
    
  }
  @After
  public void tearDown() throws SQLException {
      Connection con = ds.getConnection();
      con.prepareStatement("DROP TABLE ORDERS").executeUpdate();        
      con.close();    
    }   
  
  private Order newOrder(long id, Date from, Date to, Car car, Customer customer) {
    Order ord = new Order();
    ord.setID(id);
    ord.setFrom(from);
    ord.setTo(to);
    ord.setCar(car);
    ord.setCustomer(customer);
    return ord;
  }
  private void assertDeepEquals(Order expected, Order actual) {
        assertEquals(expected.getID(), actual.getID());
        assertEquals(expected.getFrom().toString(), actual.getFrom().toString());
        assertEquals(expected.getTo().toString(), actual.getTo().toString());
        assertEquals(expected.getCar(), actual.getCar());
        assertEquals(expected.getCustomer(), actual.getCustomer());
  }
  
  @Test
  public void creating() throws SQLException {
      Car c=new Car(); c.setID(1);
      Customer cu=new Customer(); cu.setID(1);
      Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); 
      Date d1=new Date(cal1.getTimeInMillis());
      Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,13); 
      Date d2=new Date(cal2.getTimeInMillis());
      Order ord= newOrder(1,d1,d2,c,cu);

      man.create(ord);
      Long ordID = ord.getID();
      assertNotNull(ordID);
      Date ordFrom =ord.getFrom();
      assertNotNull(ordFrom);
      Date ordTo = ord.getTo();
      assertNotNull(ordTo);
      Long car = ord.getCar().getID();
      assertNotNull(car);
      Long customer = ord.getCustomer().getID();
      assertNotNull(customer);
      
      Order o = man.readByID(ordID);
      assertEquals(ord,o);
      assertDeepEquals(ord,o);
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,15); 
      Date date=new Date(cal.getTimeInMillis());
      o.setTo(date);
      assertNotSame(ord,o);
  }

  @Test
  public void creatingWrongly() throws SQLException {
    try {
      man.create(null);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Car c=new Car(); c.setID(1);
      Customer cu=new Customer(); cu.setID(1);
      Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); 
      Date d1=new Date(cal1.getTimeInMillis());
      Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,13); 
      Date d2=new Date(cal2.getTimeInMillis());
      Order o2= newOrder(1,null,d2,c,cu);
      man.create(o2); 
      assertNull(o2.getFrom());
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Car c=new Car(); c.setID(1);
      Customer cu=new Customer(); cu.setID(1);
      Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); 
      Date d1=new Date(cal1.getTimeInMillis());
      Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,13); 
      Date d2=new Date(cal2.getTimeInMillis());
      Order o3= newOrder(1,d1,null,c,cu);
      man.create(o3); assertNull(o3.getTo());
      fail(); 
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Car c=new Car(); c.setID(1);
      Customer cu=new Customer(); cu.setID(1);
      Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); 
      Date d1=new Date(cal1.getTimeInMillis());
      Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,13); 
      Date d2=new Date(cal2.getTimeInMillis());
      Order o4= newOrder(1,d1,d2,null,cu);
      man.create(o4); assertNull(o4.getCustomer());
      fail();     
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Car c=new Car(); c.setID(1);
      Customer cu=new Customer(); cu.setID(1);
      Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); 
      Date d1=new Date(cal1.getTimeInMillis());
      Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,13); 
      Date d2=new Date(cal2.getTimeInMillis());
      Order o5= newOrder(1,d1,d2,c,null);
      man.create(o5); assertNull(o5.getCar());
      fail(); 
    }
    catch(IllegalArgumentException iae) {}
  }
  
  
  
  @Test
  public void deleting() throws SQLException {
    Car c=new Car(); c.setID(1);
    Customer cu=new Customer(); cu.setID(1);
    Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); 
    Date d1=new Date(cal1.getTimeInMillis());
    Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,13); 
    Date d2=new Date(cal2.getTimeInMillis());
    Order o1= newOrder(1,d1,d2,c,cu);
    
    Car c2=new Car(); c.setID(1);
    Customer cu2=new Customer(); cu2.setID(1);
    Calendar cal3=new GregorianCalendar(); cal3.set(2010,8,12); 
    Date d3=new Date(cal3.getTimeInMillis());
    Calendar cal4=new GregorianCalendar(); cal4.set(2010,8,13); 
    Date d4=new Date(cal4.getTimeInMillis());
    Order o2= newOrder(2,d3,d4,c2,cu2);
     
    man.create(o1);
    man.create(o2);

    assertNotNull(man.readByID(o1.getID()));
    assertNotNull(man.readByID(o2.getID()));

    man.delete(o1.getID());

    assertNull(man.readByID(o1.getID()));
    assertNotNull(man.readByID(o2.getID()));    
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
