/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author DJohnny
 */
public class CarManagerImplTest {
  private CarManagerImpl man;
  
  @Before
  public void setUp() {
    man=new CarManagerImpl();
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
  
  @Test
  public void creating() {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=cal.getTime();
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
      assertEquals(car,c); //dodělat deep equals
      c.setModel("Octavia");
      assertNotSame(car,c);
  }

  @Test
  public void creatingWrongly() {
    try {
      man.create(null);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=cal.getTime();
      Car c1=newCar(-1,"Skoda","Fabia",date,BigDecimal.valueOf(1000));
      man.create(c1);
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=cal.getTime();
      Car c2=newCar(1,null,"Fabia",date,BigDecimal.valueOf(1000));
      man.create(c2); assertNull(c2.getProducer());
      fail();
    }
    catch(IllegalArgumentException iae) {}
    
    try {
      Calendar cal=new GregorianCalendar(); cal.set(2010,5,12); Date date=cal.getTime();
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
  public void deleting() {
    Calendar cal1=new GregorianCalendar(); cal1.set(2010,5,12); Date d1=cal1.getTime();
    Car c1=newCar(1,"Skoda","Fabia",d1,BigDecimal.valueOf(1000));
    Calendar cal2=new GregorianCalendar(); cal2.set(2010,5,12); Date d2=cal2.getTime();
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
