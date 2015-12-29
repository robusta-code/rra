package io.robusta.rra.microservices.saga;

/**
 * Created by Nicolas Zozol on 24/12/2015.
 */
public interface SagaTransaction  {


    public boolean executeCommand();
    public boolean cancelCommand();
    public boolean compensateCommand();

}
