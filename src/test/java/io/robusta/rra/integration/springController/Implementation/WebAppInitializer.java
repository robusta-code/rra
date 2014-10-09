package io.robusta.rra.integration.springController.Implementation;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		WebApplicationContext context = getContext();
		servletContext.addListener(new ContextLoaderListener(context));
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(
				context));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/*");

		// WebApplicationContext appContext = null;// = set up the context
		//
		// ServletRegistration.Dynamic dispatcher =
		// servletContext.addServlet("dispatcher", new DispatcherServlet(
		// appContext));
		// dispatcher.setLoadOnStartup(1);
		// dispatcher.addMapping("/");

		// XmlWebApplicationContext appContext = new XmlWebApplicationContext();
		// appContext.setConfigLocation("/rra/src/test/java/io/robusta/rra/integration/WEB-INF/servlet-context.xml");
		//
		// ServletRegistration.Dynamic dispatcher =
		// servletContext.addServlet("dispatcher", new
		// DispatcherServlet(appContext));
		// dispatcher.setLoadOnStartup(1);
		// dispatcher.addMapping("/");
	}
	
	private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.setConfigLocation("io.robusta.rra");
        return context;
    }


}
