package io.robusta.rra.resource;

import io.robusta.rra.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev on 29/08/14.
 */
public interface ResourceCollection<IdType, T  extends Resource<IdType>> {


    /**
     * @param id id of the searched Resource
     * @return the resource with specified id, or null if not found
     */
    public T getById(IdType id) ;



    /**
     * Return true if one of the Resources has the id
     *
     * @param id id of the searched resource
     * @return true if a Resource is found
     * @throws IllegalArgumentException if id == null
     */
    public boolean containsById(IdType id);


    /**
     * <p>
     * returns something like :
     * <code>
     * <ids>
     *  <id>28</id>
     *  <id>29</id>
     * </ids>
     * </code>
     * </p>
     * @param withXmlHeader add xml headers with UTF-8 encoding if true
     * @return the xml representation of ids
     * @see #getJsonIds()
     */
    public String getXmlIds(boolean withXmlHeader);
    /**
     * <p>
     * returns something like : [28,29]
     *
     * @see #getXmlIds(boolean)
     * @return the xml representation of ids
     */
    public String getJsonIds();

    /**
     * @return a list of all ids
     */
    public List<IdType> getIds();
}
