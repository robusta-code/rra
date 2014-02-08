package io.robusta.rra;

import java.util.Map;

/**
 * Created by  Nicolas Zozol for Robusta Code
 * Released under Apache 2 Licence : https://www.apache.org/dev/apply-license.html
 * AS-IS With NO Guarantee, Use at your own risks
 */

public interface Resource<IdType> {

    /**
     * Returns the id of the Resource.
     * <p>
     * In REST, a Resource might have an id as a Number, UUID String, CompositeKey or even an url
     * </p>
     * @return the Resource id
     */
    public IdType getId();

    /**
     * @todo2 : change to Long object
     */
    public void setId(IdType id);




    /**
     * Name of the prefix used to represent a resource.
     * For exemple, if the getPrefix() returns "house" and getListPrefix() returns "houses" for a resource House, a ResourceList<House>.getRepresentation() will return :
     * <houses>
     *  <house><id>1</id><name>House 1</name></house>
     *  <house><id>2</id><name>Small House</name></house>
     *  <house><id>3</id><name>Big House</name></house>
     * </houses>
     * Or : {houses:[{id:"1", name:"House 1"}, {id:"2", name:"Small House"}, {id:"3", name:"Big House"}]}
     */
    public String getPrefix();

    /**
     * Name of the prefix used to represent a list of this resource.
     * For exemple, if the function returns "houses" for a resource House, a ResourceList<House>.getRepresentation() will return :
     * <houses>
     *  <house><id>1</id><name>House 1</name></house>
     *  <house><id>2</id><name>Small House</name></house>
     *  <house><id>3</id><name>Big House</name></house>
     * </houses>
     * Or : {houses:[{id:"1", name:"House 1"}, {id:"2", name:"Small House"}, {id:"3", name:"Big House"}]}
     */
    public String getListPrefix();


    /**
     * Serialize this Resource. You can do it manually or use a Mapper
     * @return all name/values of this object
     */
    public Map<String, Object> serialize();

}
