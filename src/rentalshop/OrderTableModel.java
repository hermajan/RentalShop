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
public class OrderTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;
  private OrderManagerImpl man;

    public OrderTableModel() {
        man = new OrderManagerImpl();
        man.setCon(Window.ds);
    }   
 
    
    @Override
    public int getRowCount() {
        return man.readAll().size();
    }
 
    @Override
    public int getColumnCount() {
        return 5;
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = man.readAll().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return order.getID();
            case 1:
                return order.getFrom();
            case 2:
                return order.getTo();
            case 3:
                return order.getCar();
            case 4:
                return order.getCustomer();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addOrder(Order ord) {
        man.create(ord);
        int lastRow = getRowCount() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }
    public void deleteOrder(Long id,int row) {
        man.delete(id);
        fireTableRowsDeleted(row, row);
    }
    public void updateOrder(Long id,int row,Order order) {
        man.modify(id,order);
        fireTableRowsUpdated(row, row);
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("ID");
            case 1:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("from");
            case 2:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("to");
            case 3:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("SPZ");
            case 4:
                return java.util.ResourceBundle.getBundle("Resource/Czech").getString("Customer");
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
                return Date.class;
            case 3:
                return Car.class;
            case 4:
                return Customer.class;               
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Order ord = man.readAll().get(rowIndex);
        switch (columnIndex) {
            case 0:
                ord.setID((Long) aValue);
                break;
            case 1:
                ord.setFrom((Date) aValue);
                break;
            case 2:
                ord.setTo((Date) aValue);
                break;
            case 3:
                ord.setCar((Car) aValue);
                break;
            case 4:
                ord.setCustomer((Customer) aValue);
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
