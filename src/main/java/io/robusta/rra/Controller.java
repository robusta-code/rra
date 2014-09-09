package io.robusta.rra;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * @author Nicolas Zozol
 */
public interface Controller {

    public void validate(Representation r, String ... keys);

    public String [] getBasicAuthentication();

}
