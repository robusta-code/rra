package io.robusta.rra.microservices.saga;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicolas Zozol on 24/12/2015.
 */
public interface Saga extends Auditable {


    default void startSaga(){
        this.getSagaAuditor().start("starting "+this.getName(), this);
    };

    default void endSaga(){
        this.getSagaAuditor().start("ending "+this.getName(), this);
    };

    /**
     * @return topic of the saga
     */
    public String getName();

    /**
     * @return name of the main user involved in this saga
     */
    public String getUsername();

    /**
     * Defines responsibilities for users involved in this saga.
     * For exemple : {host : briancon , guest : john}
     * @return responsibilities
     */
    default Map<String, String> getResponsabilities(){
        HashMap<String, String> map = new HashMap<>();
        map.put(this.getName(), this.getUsername());
        return map;
    };




}
