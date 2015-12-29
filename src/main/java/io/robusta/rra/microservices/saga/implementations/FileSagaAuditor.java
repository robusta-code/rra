package io.robusta.rra.microservices.saga.implementations;

import io.robusta.rra.microservices.saga.SagaAuditor;
import io.robusta.rra.microservices.saga.SharedContext;

/**
 * Created by Nicolas Zozol on 26/12/2015.
 */
public class FileSagaAuditor implements SagaAuditor {



    @Override
    public <T> void write(String category, String message, T data) {
        //TODO : write into a file
    }
}
