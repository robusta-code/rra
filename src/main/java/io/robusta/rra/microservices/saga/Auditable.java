package io.robusta.rra.microservices.saga;

/**
 * Created by Nicolas Zozol on 24/12/2015.
 */
public interface Auditable {

    public SagaAuditor getSagaAuditor();
}
