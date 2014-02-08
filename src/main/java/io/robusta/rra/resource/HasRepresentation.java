package io.robusta.rra.resource;

import io.robusta.rra.Representation;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * Released under Apache 2 Licence : https://www.apache.org/dev/apply-license.html
 * AS-IS With NO Guarantee, Use at your own risks
 *
 * <p>This interface allows a Resource to choose a Representation with a Strategy (http://en.wikipedia.org/wiki/Strategy_pattern).
 * You can bypass complexity by ignoring Strategies and just implementing #getRepresentation()</p>
 *
 * <p>Most of the time, #getRepresentation() will be this snippet : </p>
 * <code>
 *     public Representation getRepresentation(){
 *         if (this.strategy == null){
 *             // choose a default Representation or throw exception
 *             return new StaxRepresentation(this);
 *         }else{
 *             return this.strategy.getRepresentation();
 *         }
 *     }
 *
 * </code>
 */
public interface HasRepresentation {

    void setRepresentationStrategy(RepresentationStrategy strategy);
    RepresentationStrategy getRepresentationStrategy();
    Representation getRepresentation();

}
