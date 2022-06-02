package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phuxuan.model.Country;
import com.phuxuan.model.User;

import dao.CountryDAO;
import dao.IUserDAO;
import dao.UserDAO;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private CountryDAO countryDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        countryDAO = new CountryDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        
        System.out.println(this.getClass() + " doGet: " + action);
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
            	case "":
            		listUserPagging(request, response);
            		break;
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUserPagging(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String query = "";
    	int country = -1;
    	int page = 1;
    	int recordsPerPage = 5;
    	
    	if(request.getParameter("page")!=null) {
    		page = Integer.parseInt(request.getParameter("page"));
    	}
    	if(request.getParameter("q")!=null) {
    		query = request.getParameter("q");
    	}
    	if(request.getParameter("country")!=null) {
    		country = Integer.parseInt(request.getParameter("country"));
    	}
    	
    	
    	
    	IUserDAO userDAO = new UserDAO();
    	List<User> listUser = userDAO.viewAllUsers((page-1)*recordsPerPage, recordsPerPage, query, country);
    	
    	int noOfRecords = userDAO.getNoOfRecords();
    	int noOfPages  = (int) Math.ceil(noOfRecords*1.0/recordsPerPage);
    	
    	
    	 List<Country> listCountry = countryDAO.getAllCountry();
         
         request.setAttribute("listCountry", listCountry);
         
    	request.setAttribute("listUser", listUser);
    	request.setAttribute("noOfPages", noOfPages);
    	request.setAttribute("currentPage", page);
    	
    	request.setAttribute("q", query);
    	request.setAttribute("country", country);
    	
    	RequestDispatcher view = request.getRequestDispatcher("user/listPagging.jsp");
    	view.forward(request, response);
    	
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> listUser = userDAO.selectAllUsers();
        List<Country> listCountry = countryDAO.getAllCountry();
        
        
        request.setAttribute("listUser", listUser);
        request.setAttribute("listCountry", listCountry);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
	
	

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	List<Country> listCountry = countryDAO.getAllCountry();
    	request.setAttribute("listCountry", listCountry);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int idCountry = Integer.parseInt(request.getParameter("country"));
        User newUser = new User(name, email, idCountry);
        userDAO.insertUser(newUser);
        
        
        List<Country> listCountry = countryDAO.getAllCountry();
        request.setAttribute("listCountry", listCountry);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        int idCountry = Integer.parseInt(request.getParameter("country"));

        User book = new User(id, name, email, idCountry);
        userDAO.updateUser(book);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);

        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
}
