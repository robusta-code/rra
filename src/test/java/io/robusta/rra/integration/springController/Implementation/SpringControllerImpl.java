package io.robusta.rra.integration.springController.Implementation;

import io.robusta.rra.controller.SpringController;
import io.robusta.rra.integration.servletController.implementation.MyClientPropertyServlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringControllerImpl extends SpringController {
	@Override
	public void init() {
		super.init();
		setClientProperty(new MyClientPropertyServlet());
		// decomment to override Rra.defaultRepresentation (GsonRepresentation
		// by default)
		// Rra.defaultRepresentation = new JacksonRepresentation();
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/springJson", method = RequestMethod.POST)
	@ResponseBody
	public String springJson(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
		return getRepresentation(request).remove("email").toString();
	}

	@RequestMapping(value = "/spring", method = RequestMethod.POST)
	public String spring(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String[] userPassword = getBasicAuthentication(request, response);
		if (userPassword[0] != null) {
			System.out.println("user=" + userPassword[0].toString());
			System.out.println("Password=" + userPassword[1].toString());
		}
		System.out.println("getRepresentation=" + getRepresentation(request));
		System.out.println("validate=" + validate(request, response, "name", "email"));
		System.out.println("isChrome()=" + getClientProperty().isChrome(request));
		System.out.println("isFF()=" + getClientProperty().isFF(request));

		response.getWriter().println(getRepresentation(request).remove("email").toString());
		return null;
	}

	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String client(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (request.getParameter("p1") != null) {
			response.getWriter().println(request.getParameter("p1"));
		} else {
			response.getWriter().println("ok");
		}

		return null;
	}

	public void ok() {

	}

	@Override
	public MyClientPropertyServlet getClientProperty() {
		return (MyClientPropertyServlet) super.getClientProperty();
	}
}
