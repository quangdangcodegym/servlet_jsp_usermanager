package com.phuxuan.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.phuxuan.dao.CountryDAO;
import com.phuxuan.dao.IUserDAO;
import com.phuxuan.dao.UserDAO;
import com.phuxuan.model.Country;
import com.phuxuan.model.User;
import com.phuxuan.utils.AppUtils;
import com.phuxuan.utils.ValidateUtils;

@WebServlet(name = "UserServlet", urlPatterns = "/users", loadOnStartup = 0)
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private CountryDAO countryDAO;
    
    private ValidateUtils validateUtils;
    private String errors;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        countryDAO = new CountryDAO();
        validateUtils = new ValidateUtils();
		
	  if(this.getServletContext().getAttribute("listCountry")==null) {
		  System.out.println(this.getClass() + " setAttribute listCountry");
		  this.getServletContext().setAttribute("listCountry",
		  AppUtils.getAllCountry()); 
	  }
		 
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
                case "create1":
                	insertUserValidate(request, response);
                	break;
                case "create2":
                	insertUserValidateFull(request, response);
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
        
   
        System.out.println( request.getContextPath());
        System.out.println(request.getMethod());
        System.out.println(request.getRequestURI());
        
        
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
                case "create1" :
                	showNewFormValidate(request, response);
                	break;
                case "create2" :
                	showNewFormValidateFull(request, response);
                	break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                	System.out.println(this.getClass() + " doGet: default is show list");
                	listUser(request, response);
                                        break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

	private void insertUserValidate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		
		user.setId(Integer.parseInt(request.getParameter("id")));
		user.setEmail(request.getParameter("email"));
		user.setIdCountry(Integer.parseInt(request.getParameter("country")));
		user.setName(request.getParameter("name"));
		user.setPassword(request.getParameter("password"));
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		
		System.out.println("User: " + user);
		
		if (!constraintViolations.isEmpty()) {
			
			String errors = "<ul>";
			for (ConstraintViolation<User> constraintViolation : constraintViolations) {
				errors += "<li>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
						+ "</li>";
			}
			errors += "</ul>";
			request.setAttribute("user", user);
			request.setAttribute("errors", errors);
			
			List<Country> listCountry = countryDAO.getAllCountry();
	    	request.setAttribute("listCountry", listCountry);
			request.getRequestDispatcher("/user/createvalidate.jsp").forward(request, response);
		} else {
			
			User u = new User();
			request.setAttribute("account", u);
			
			List<Country> listCountry = countryDAO.getAllCountry();
	    	request.setAttribute("listCountry", listCountry);
			request.getRequestDispatcher("user/createvalidate.jsp").forward(request, response);
		}
		
	}
	private void insertUserValidateFull(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		boolean flag = true;
		Map<String, String> hashMap = new HashMap<String, String>();
		
		
		
		System.out.println(this.getClass() + " insertUserValidateFull ");
		try {
			user.setId(Integer.parseInt(request.getParameter("id")));
			
			String email = request.getParameter("email");
			user.setEmail(email);
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));
			
			System.out.println(this.getClass() + "Country value from request: " + request.getParameter("country") );
			int idCountry = Integer.parseInt(request.getParameter("country"));
			user.setCountry(idCountry);
			
			System.out.println(this.getClass() + "User info from request: " + user);
			
			ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = validatorFactory.getValidator();
			
			Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
			
			System.out.println("User: " + user);
			
			if (!constraintViolations.isEmpty()) {
				
				errors = "<ul>";
				// constraintViolations is has error
				for (ConstraintViolation<User> constraintViolation : constraintViolations) {
					errors += "<li>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
							+ "</li>";
				}
				errors += "</ul>";
				
				
				request.setAttribute("user", user);
				request.setAttribute("errors", errors);
				
				List<Country> listCountry = countryDAO.getAllCountry();
		    	request.setAttribute("listCountry", listCountry);
		    	
		    	System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
				request.getRequestDispatcher("/user/createvalidate_full.jsp").forward(request, response);
			}else{
				if(userDAO.selectUserByEmail(email)!=null) {
					flag = false;
					hashMap.put("email", "Email exits in database");
					System.out.println(this.getClass() + " Email exits in database");
					
				}
				if(countryDAO.selectCountry(idCountry)==null) {
					flag = false;
					hashMap.put("country", "Country value invalid");
					System.out.println(this.getClass() + " Country invalid");
				}
				
				if(flag) {
					// Create user susscess
					User u = new User();
					request.setAttribute("account", u);
					
					List<Country> listCountry = countryDAO.getAllCountry();
			    	request.setAttribute("listCountry", listCountry);
					request.getRequestDispatcher("user/createvalidate_full.jsp").forward(request, response);
				}else {
					// Error : Email exits in database
					// Error: Country invalid
					errors = "<ul>";
					// One more field has error
					hashMap.forEach(new BiConsumer<String, String>() {
						@Override
						public void accept(String keyError, String valueError) {
							errors += "<li>"  + valueError
										+ "</li>";
							
						}
					});
					errors +="</ul>";
					
					request.setAttribute("user", user);
					request.setAttribute("errors", errors);
					
			    	
			    	System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
					request.getRequestDispatcher("/user/createvalidate_full.jsp").forward(request, response);
				}
				
				
			}
		}
		catch (NumberFormatException ex) {
			System.out.println(this.getClass() + " NumberFormatException: User info from request: " + user);
			errors = "<ul>";
					errors += "<li>" + "Input format not right"
								+ "</li>";
					
			errors += "</ul>";
			
			
			request.setAttribute("user", user);
			request.setAttribute("errors", errors);
			
			request.getRequestDispatcher("/user/createvalidate_full.jsp").forward(request, response);
		}
		catch(Exception ex){
			
		}
		
		
	}
    private void showNewFormValidate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	List<Country> listCountry = countryDAO.getAllCountry();
    	request.setAttribute("listCountry", listCountry);
    	
    	User user  = new User();
    	request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/createvalidate.jsp");
        dispatcher.forward(request, response);
	}
    private void showNewFormValidateFull(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//ResourceBundle resourceBundle = new ResourceBundle()
    	
    	List<Country> listCountry = countryDAO.getAllCountry();
    	request.setAttribute("listCountry", listCountry);
    	
    	User user  = new User();
    	request.setAttribute("user", user);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/createvalidate_full.jsp");
        dispatcher.forward(request, response);
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
        
        
        
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);
    }
	
	

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	//request.getContextPath()
    	System.out.println("Test servlet: " + request.getServletPath());
    	
    	
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
