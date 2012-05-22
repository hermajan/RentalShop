/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import java.sql.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xhermann
 */
public class CarTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;
  private CarManagerImpl man;

    public CarTableModel() {
        man = new CarManagerImpl();
        man.setCon(Window.ds);
    }   
 
    
    @Override
    public int getRowCount() {
        return man.readAll().size();
    }
 
    @Override
    public int getColumnCount() {
        return 6;
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Car car = man.readAll().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return car.getID();
            case 1:
                return car.getProducer();
            case 2:
                return car.getModel();
            case 3:
                return car.getSpz();
            case 4:
                return car.getManufactured();
            case 5:
                return car.getPrice();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addCar(Car car) {
        man.create(car);
        int lastRow = getRowCount() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    public void deleteCar(Long id,int row) {
        man.delete(id);
        fireTableRowsDeleted(row, row);
    }
    public void updateCar(Long id,int row,Car car) {
        man.modify(id,car);
        fireTableRowsUpdated(row, row);
    }
     
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("ID");
            case 1:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Producer");
            case 2:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Model");
            case 3:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("SPZ");
            case 4:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Manufactured");
            case 5:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Price");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
            case 2:
            case 3:
                return String.class;
            case 4:
                return Date.class;
            case 5:
                return BigDecimal.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Car car = man.readAll().get(rowIndex);
        switch (columnIndex) {
            case 0:
                car.setID((Long) aValue);
                break;
            case 1:
                car.setProducer((String) aValue);
                break;
            case 2:
                car.setModel((String) aValue);
                break;
            case 3:
                car.setSpz((String) aValue);
                break;
            case 4:
                car.setManufactured((Date) aValue);
                break;
            case 5:
                car.setPrice((BigDecimal) aValue);
                break;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
 
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            case 0:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

}
