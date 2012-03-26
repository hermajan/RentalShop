/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CarManagerImplTest {
  private CarManagerImpl man;
  private Connection con;
  
  @Before
  public void setUp() throws SQLException {
    con=DriverManager.getConnection("jdbc:derby:memory:CarManagerTest;create=true");
        con.prepareStatement("CREATE TABLE CARS ("
            +"ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
            +"PRODUCER VARCHAR(50),"
            +"MODEL VARCHAR(50),"
            +"SPZ VARCHAR(50),"
            +"MANUFACTURED DATE,"
            +"PRICE DECIMAL)").executeUpdate();
        man = new CarManagerImpl(con);
  }
  @After
  public void tearDown() throws SQLException {
      con.prepareStatement("DROP TABLE CARS").executeUpdate();        
      con.close();
  }
  
  private Car newCar(long id, String producer, String model, Date manufactured, BigDecimal price) {
    Car car=new Car();
    car.setID(id);
    car.setProducer(producer);
    car.setModel(model);
    car.setManufactured(manufactured);
    car.setPrice(price);
    return car;
  }
  private void assertDeepEquals(Car expected, Car actual) {
        assertEquals(expected.getID(), actual.getID());
        assertEquals(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getManufactured(), actual.getManufactured());
        assertEquals(expected.getPrice(), actual.getPrice());
  }
  
  @Test
  public void creating() throws SQLException {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=new Date(cal.getTime().getTime());
      Car car=newCar(1,"Skoda","Fabia",date,BigDecimal.valueOf(1000));

      man.create(car);
      Long carID=car.getID();
      assertNotNull(carID);
      String carProd=car.getProducer();
      assertNotNull(carProd);
      String carModel=car.getModel();
      assertNotNull(carModel);
      Date carManuf=car.getManufactured();
      assertNotNull(carManuf);
      BigDecimal carPrice=car.getPrice();
      assertNotNull(carPrice);
      
      Car c=man.readByID(carID);
      assertEquals(car,c);
      assertDeepEquals(car,c);
      c.setModel("Octavia");
      assertNotSame(car,c);
  }

  @Test
  public void creatingWrongly() throws SQLException {
    try {
      man.create(null);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=new Date(cal.getTime().getTime());
      Car c2=newCar(1,null,"Fabia",date,BigDecimal.valueOf(1000));
      man.create(c2); assertNull(c2.getProducer());
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=new Date(cal.getTime().getTime());
      Car c3=newCar(1,"Skoda",null,date,BigDecimal.valueOf(1000));
      man.create(c3); assertNull(c3.getModel());
      fail(); 
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Car c4=newCar(1,"Skoda","Fabia",null,BigDecimal.valueOf(1000));
      man.create(c4); assertNull(c4.getManufactured());
      fail();
    }
    catch(IllegalArgumentException iae) {}
  }
  
  @Test
  public void deleting() throws SQLException {
    Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); Date d1=new Date(cal1.getTime().getTime());
    Car c1=newCar(1,"Skoda","Fabia",d1,BigDecimal.valueOf(1000));
    Calendar cal2=new GregorianCalendar(); cal2.set(2010,12,5); Date d2=new Date(cal2.getTime().getTime());;
    Car c2=newCar(2,"Volkswagen","Golf",d2,BigDecimal.valueOf(1000));
    man.create(c1);
    man.create(c2);

    assertNotNull(man.readByID(c1.getID()));
    assertNotNull(man.readByID(c2.getID()));

    man.delete(c1.getID());

    assertNull(man.readByID(c1.getID()));
    assertNotNull(man.readByID(c2.getID()));    
  }
}
