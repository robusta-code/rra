package io.robusta.rra.microservices.saga.implementations;

import io.robusta.rra.microservices.saga.Auditable;
import io.robusta.rra.microservices.saga.Saga;
import io.robusta.rra.microservices.saga.SagaAuditor;
import io.robusta.rra.resource.Resource;

import java.util.Map;

/**
 * Created by Nicolas Zozol on 26/12/2015.
 */
public class SagaResource implements Saga,  Resource<Long>, Auditable {

    long id;
    String name;
    SagaAuditor auditor;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getPrefix() {
        return "saga";
    }

    @Override
    public String getCollectionPrefix() {
        return "sagas";
    }


    @Override
    public SagaAuditor getSagaAuditor() {
        return this.auditor;
    }


    @Override
    public void endSaga() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
