package io.robusta.rra.microservices.saga.implementations;

import io.robusta.rra.microservices.saga.Saga;
import io.robusta.rra.microservices.saga.SharedContext;
import io.robusta.rra.resource.Resource;

/**
 * Created by Nicolas Zozol on 26/12/2015.
 */
public class SimpleSharedContext<T> implements Resource<Long>, SharedContext<T>{


    long id;

    String sagaName;
    long sagaId;
    T context;

    public SimpleSharedContext(SagaResource saga, T context) {
        this.sagaName = saga.getName();
        this.sagaId = saga.getId();
        this.context = context;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Context for "+this.sagaName;
    }

    @Override
    public T getContext() {
        return context;
    }
}
