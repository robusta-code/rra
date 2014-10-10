package io.robusta.rra.integration.controller.servletController.implementation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.robusta.rra.controller.ServletController;

@WebServlet( "/test" )
public class ServletControllerImpl extends ServletController{

	  @Override
	    public void init() throws ServletException {
	        super.init();
	        // System.out.println("My application starts");
	        setClientProperty( new MyClientPropertyServlet() );
	        // decomment to override Rra.defaultRepresentation (GsonRepresentation
	        // by default)
	        // Rra.defaultRepresentation = new JacksonRepresentation();
	    }

	    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
	            IOException {

	        String[] userPassword = getBasicAuthentication( request, response );
	        if ( userPassword[0] != null ) {
	            System.out.println( "user " + userPassword[0].toString() );
	            System.out.println( "Password " + userPassword[1].toString() );
	        }
	        System.out.println( "getRepresentation " + getRepresentation( request ) );
	        System.out.println( "validate " + validate( request, response, "name", "email" ) );
	        System.out.println( "isChrome()" + getClientProperty().isChrome( request ) );
	        System.out.println( "isFF()" + getClientProperty().isFF( request ) );
	        response.getWriter().println( getRepresentation( request ) );

	    }

	    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
	            IOException {
	        System.out.println( "isChrome :: " + getClientProperty().isChrome( request ) );
	        System.out.println( "isFF :: " +getClientProperty().isFF( request ) );
	        System.out.println( "isFirefox :: " +getClientProperty().isFirefox( request ) );
	        System.out.println( "isMobile :: " +getClientProperty().isMobile( request ) );
	        System.out.println( "isTablet :: " +getClientProperty().isTablet( request ) );
	    }

	    @Override
	    public MyClientPropertyServlet getClientProperty() {
	        return (MyClientPropertyServlet) super.getClientProperty();
	    }

	
}
