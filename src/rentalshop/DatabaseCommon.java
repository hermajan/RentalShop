package rentalshop;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;


public class DatabaseCommon {

    private static final Logger logger = Logger.getLogger(
            DatabaseCommon.class.getName());
    
    /**
     Cleaning method. Can clean Statement, Connection and if you want to clean after modification - 
     it means do rollback and turn on autocommit, then third paramether give True.
     Default value for edit is false.
    */    
    public static void cleanMe(Statement st, Connection con){
        cleanMe(st, con, false);        
    }
    public static void cleanMe(Statement st, Connection con, boolean edit){
        if (st != null){
            try{
                st.close();
            } catch (SQLException ex){
                    logger.log(Level.SEVERE, "Error when closing statement", ex);
            }
        }
        if (con != null){
            if (edit == true){
                try {
                if (con.getAutoCommit()) {
                    throw new IllegalStateException("Connection is in the autocommit mode");
                }
                con.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when doing rollback", ex);
            }            
            try {
            con.setAutoCommit(true);   
            } catch (SQLException ex){
                logger.log(Level.SEVERE, "Error when switching autocommit mode back to true", ex);
            }
            }
            try {
                con.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when closing connection", ex);
            }
        }
    }
    
    
}
