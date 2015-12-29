package io.robusta.rra.microservices.saga;

/**
 * Created by Nicolas Zozol on 26/12/2015.
 */
public interface SharedContext<T> {

    public T getContext();

}
