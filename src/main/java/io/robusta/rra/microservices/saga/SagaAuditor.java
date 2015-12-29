package io.robusta.rra.microservices.saga;

/**
 * Created by Nicolas Zozol on 24/12/2015.
 */
public interface SagaAuditor {


    public <T> void write(String category, String message, T data);

    default  void  start(String message, Saga saga){
        this.write("attempt", message, saga);
    };

    default <T> void  attempt(String message, SharedContext<T> context){
        this.write("attempt", message, context);
    };

    default <T> void  success(String message, SharedContext<T> context){
        this.write("success", message, context);
    };

    default <T> void  fail(String message, SharedContext<T> context){
        this.write("fail", message, context);
    };


}
