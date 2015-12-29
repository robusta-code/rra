package io.robusta.rra.microservices.saga;

/**
 * Created by Nicolas Zozol on 26/12/2015.
 */
public interface BrokerBoundary {

    public void send(String message);

    public void onSuccess(Saga saga, SagaTransaction transaction, String message);

    public void onFail(Saga saga, SagaTransaction transaction, String message);

}
