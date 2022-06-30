package com.phuxuan.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet( name = "CountryServlet", urlPatterns = "/coutry")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 50, // 50MB
    maxRequestSize = 1024 * 1024 * 50) // 50MB
public class CountryServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/country/create.jsp");
		
		requestDispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		for (Part part : req.getParts()) {
		      String fileName = extractFileName(part);
		      // refines the fileName in case it is an absolute path
		      fileName = new File(fileName).getName();
		      // Phuong thuc nay khong duoc write 2 lan
		      //part.write(this.getFolderUpload().getAbsolutePath() + File.separator + fileName);
		      System.out.println("Store default: " + this.getFolderUpload().getAbsolutePath() + File.separator + fileName);
		      
		      String path  = this.getServletContext().getRealPath("/");
		      System.out.println("Path info" + path);
		      System.out.println("Path full: " + path + "images\\" +  fileName);
		      part.write(path + "images\\" +  fileName);
		    }
		req.setAttribute("message", "Upload File Success!");
		getServletContext().getRequestDispatcher("/result.jsp").forward(req, resp);
	}
	private String extractFileName(Part part) {
	    String contentDisp = part.getHeader("content-disposition");
	    String[] items = contentDisp.split(";");
	    for (String s : items) {
	      if (s.trim().startsWith("filename")) {
	        return s.substring(s.indexOf("=") + 2, s.length() - 1);
	      }
	    }
	    return "";
	  }
	  public File getFolderUpload() {
	    File folderUpload = new File(System.getProperty("user.home") + "/Uploads");
	    if (!folderUpload.exists()) {
	      folderUpload.mkdirs();
	    }
	    return folderUpload;
	  }

	
}
