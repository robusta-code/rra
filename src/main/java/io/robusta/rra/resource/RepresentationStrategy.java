package io.robusta.rra.resource;

import io.robusta.rra.Representation;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * Released under Apache 2 Licence : https://www.apache.org/dev/apply-license.html
 * AS-IS With NO Guarantee, Use at your own risks
 * <p>This interface allows a Resource to choose a Representation with a Strategy (http://en.wikipedia.org/wiki/Strategy_pattern)</p>
 */
public interface RepresentationStrategy {

    Representation getRepresentation();

}
