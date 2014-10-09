package io.robusta.rra.integration.jaxRsController.implementation;

import io.robusta.rra.controller.ClientPropertyJaxRs;

import javax.ws.rs.core.HttpHeaders;

public class MyClientPropertyJaxRs extends ClientPropertyJaxRs{

	public boolean isWebKit( HttpHeaders httpHeaders ) {
        return getAgent( httpHeaders ).toUpperCase().contains( "WEBKIT" );
    }
}
