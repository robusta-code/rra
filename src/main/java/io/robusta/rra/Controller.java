package io.robusta.rra;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * @author Nicolas Zozol
 */
public interface Controller {

    public boolean validate(HttpServletRequest req, HttpServletResponse resp,String ... keys) throws IOException;

    public String [] getBasicAuthentication(HttpServletRequest req, HttpServletResponse resp);

}
