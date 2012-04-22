package rentalshop.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import rentalshop.Car;
import rentalshop.CarManager;
import rentalshop.CarManagerImpl;
import rentalshop.DBUtils;

@WebServlet(
        name = "Servlet", 
        urlPatterns = {
            Servlet.ACTION_LIST_CARS, 
            Servlet.ACTION_ADD_CAR})
public class Servlet extends HttpServlet {

    static final String ACTION_ADD_CAR = "/AddCar";
    static final String ACTION_LIST_CARS = "/ListCars";    
    static final String ATTRIBUTE_CARS = "cars";    
    static final String ATTRIBUTE_CAR_FORM = "carForm";
    static final String ATTRIBUTE_ERROR = "error";
    static final String JSP_ADD_CAR = "/WEB-INF/jsp/index.jsp";
    static final String JSP_LIST_CARS = "/WEB-INF/jsp/list.jsp";
    private static final long serialVersionUID = 1L;

    private CarManagerImpl carManager = new CarManagerImpl();

    @Resource(name="jdbc/dbRentalShop")
    private void setDataSource(DataSource ds) throws SQLException {
        DBUtils.tryCreateTables(ds,CarManager.class.getResource("rentalshop.sql"));
        carManager.setCon(ds);
    }

    public static Car extractFromRequest(HttpServletRequest request) {
      Car car = new Car();
      //String idString = request.getParameter("id");        
      //carForm.setID(Long.parseLong(idString));
      car.setProducer(request.getParameter("producer"));
      car.setModel(request.getParameter("model"));
      car.setSpz(request.getParameter("spz"));
      String manufacturedString = request.getParameter("manufactured");
      if(manufacturedString.length()!=0) car.setManufactured(Date.valueOf(manufacturedString));
      String priceString = request.getParameter("price");  
      if(priceString.length()!=0) car.setPrice(BigDecimal.valueOf(Long.parseLong(priceString)));
      return car;
    } 
      
    private void listCars(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(ATTRIBUTE_CARS, carManager.readAll());
        request.getRequestDispatcher(JSP_LIST_CARS).forward(request, response);
    }
        
    private void addCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            if (request.getParameter("cancel") != null) {
                response.sendRedirect(request.getContextPath());
                return;
            }

            Car carForm = extractFromRequest(request);
            StringBuilder errors = new StringBuilder();
            Car car = carForm.validateAndToCar(errors);

            if (car == null) {
                request.setAttribute(ATTRIBUTE_ERROR, errors.toString());
                request.setAttribute(ATTRIBUTE_CAR_FORM, carForm);
                request.getRequestDispatcher(JSP_ADD_CAR).forward(request, response);
            } else {
                carManager.create(car);
                response.sendRedirect(request.getContextPath());
            }

        } else {
            request.setAttribute(ATTRIBUTE_CAR_FORM, new Car());
            request.getRequestDispatcher(JSP_ADD_CAR).forward(request, response);
        }
    }
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    switch (request.getServletPath()) {
      case ACTION_LIST_CARS:
        listCars(request, response);
        break;
      case ACTION_ADD_CAR:
        addCar(request, response);
        break;
      default:
        throw new RuntimeException("Unknown operation: " + request.getServletPath());
    }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
