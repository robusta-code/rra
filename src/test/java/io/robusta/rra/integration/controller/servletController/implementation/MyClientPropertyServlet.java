package io.robusta.rra.integration.controller.servletController.implementation;

import io.robusta.rra.controller.ClientPropertyServlet;

import javax.servlet.http.HttpServletRequest;

public class MyClientPropertyServlet extends ClientPropertyServlet {

	public boolean isFF(HttpServletRequest request) {
		return false;
	}
}
