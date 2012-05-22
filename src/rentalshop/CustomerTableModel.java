/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rentalshop;

import java.math.BigDecimal;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author xhermann
 */
public class CustomerTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;
  private CustomerManagerImpl man;

    public CustomerTableModel() {
        man = new CustomerManagerImpl();
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
        Customer cust = man.readAll().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cust.getID();
            case 1:
                return cust.getName();
            case 2:
                return cust.getSurname();
            case 3:
                return cust.getDrivingLicenseNumber();
            case 4:
                return cust.getIdentificationCardNumber();
            case 5:
                return cust.getDebts().toString();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addCustomer(Customer cust) {
        man.create(cust);
        int lastRow = getRowCount() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    public void deleteCustomer(Long id,int row) {
        man.delete(id);
        fireTableRowsDeleted(row, row);
    }
    public void updateCustomer(Long id,int row,Customer cust) {
        man.modify(id,cust);
        fireTableRowsUpdated(row, row);
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("ID");
            case 1:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Name");
            case 2:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Surname");
            case 3:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Identification");
            case 4:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Driving");
            case 5:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Debts");
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
            case 5:
                return String.class;
            case 3:
            case 4:
                return Integer.class;               
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Customer cust = man.readAll().get(rowIndex);
        switch (columnIndex) {
            case 0:
                cust.setID((Long) aValue);
                break;
            case 1:
                cust.setName((String) aValue);
                break;
            case 2:
                cust.setSurname((String) aValue);
                break;
            case 3:
                cust.setDrivingLicenseNumber((Integer) aValue);
                break;
            case 4:
                cust.setIdentificationCardNumber((Integer) aValue);
                break;
            case 5:
                cust.setDebts((BigDecimal) aValue);
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
