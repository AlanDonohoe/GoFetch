package com.gofetch.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(ProcessNewTargets.class
			.getName());
	
	
	public TestServlet(){
		
		
		super();
		
		log.info("In test servlet - constructor....");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		log.info("In test servlet - doGet 1");
		response.setContentType("text/plain");
		
		log.info("In test servlet - doGet 2");

		try {
			getServletContext().getRequestDispatcher("/index.jsf").forward(
					request, response);
		} catch (ServletException e) {

			String msg = "Exception thrown in TestServlet\n";
			log.severe(msg + e.getMessage());

		}
		
		log.info("In test servlet - doGet 3");
		
	}

}
