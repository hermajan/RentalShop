/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import java.util.Date;
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
      Date date=new Date(2010,5,12);
      Car car=newCar(1,"Skoda","Fabia",date,BigDecimal.valueOf(1000));

      man.create(car);
      Long carID=car.getID();
      assertNotNull(carID);
      String carProd=car.getProducer();
      String carModel=car.getModel();
      Date carManuf=car.getManufactured();
      BigDecimal carPrice=car.getPrice();
      
      Car c=man.readByID(carID);
      assertEquals(car,c);
      assertNotSame(car,c);
  }

  @Test
  public void creatingWrong() {
    try {
      man.create(null);
      fail();
    }
    catch (IllegalArgumentException iae) {
      
    }
    
    Date date=new Date(2010,5,12);
    Car car=newCar(1,"Skoda","Fabia",date,BigDecimal.valueOf(1000));
    car.setID(0);
    try {
      man.create(car);
      fail();
    } catch (IllegalArgumentException ex) {
    
    }
  }
  
  @Test
  public void deleting() {
    Date d1=new Date(2010,5,12);
    Car c1=newCar(1,"Skoda","Fabia",d1,BigDecimal.valueOf(1000));
    Date d2=new Date(2011,8,16);
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
