package io.robusta.rra.resource;

/**
 * Created by  Nicolas Zozol for Robusta Code
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.robusta.rra.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Created by  Nicolas Zozol for Robusta Code
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ResourceList<IdType, T extends Resource<IdType>> extends ArrayList<T> {



    /**
     * create a List with T that extends Ressource
     *
     * @param resources Resources of the new ResourceList
     */
    public ResourceList(Collection<T> resources) {
        super();
        this.addAll(resources);
    }

    /**
     * create a List with T that extends Ressource
     *
     * @param resources Resources of the new ResourceList
     */
    public ResourceList(T[] resources) {
        super();

        if (resources == null) {
            throw new IllegalArgumentException("Resources array is null");
        }

        Collections.addAll(this, resources);
    }

    /**
     * Creates an empty List that you must fill with Ressources
     */
    public ResourceList() {
        super();
    }


    /**
     * @param id id of the searched Resource
     * @return the resource with specified id, or null if not found
     */
    public T getById(IdType id) {

        for (T resource : this) {
            if (resource != null && resource.getId() != null && resource.getId().equals(id)) {
                return resource;
            }
        }
        return null;
    }

    /**
     * remove all items of the list with this id and returns the last one found.
     * Returns null if we can't find it.
     *
     * @param id id of the searched Resource
     * @return the found resource or null
     */
    public T removeById(IdType id) {

        T found = null;
        for (T resource : this) {
            if (resource != null && resource.getId() != null && resource.getId().equals(id)) {
                this.remove(resource);
                found = resource;
            }
        }
        return found;
    }

    /**
     * Return true if one of the Resources has the id
     *
     * @param id id of the searched resource
     * @return true if a Resource is found
     * @throws IllegalArgumentException if id == null
     */
    public boolean containsId(IdType id) {
        if (id == null) {
            throw new IllegalArgumentException("Null id entered");
        } else {
            for (T resource : this) {
                if (resource != null && resource.getId() != null && resource.getId().equals(id)) {
                    return true;
                }
            }
        }

        //not found
        return false;
    }

    /**
     * This function replace the resource with id by the new Resource, and returns the old one
     *
     * @return the replaced Resource, or null if no replacement is done
     */
    public T replace(IdType id, T resource) {

        if (id == null) {
            throw new IllegalArgumentException("null id in parameters");
        }


        int index;
        for (T t : this) {

            if (t != null && t.getId() != null && t.getId().equals(id)) {
                index = this.indexOf(t);
                this.remove(t);
                this.add(index, resource);
                return t;
            }
        }
        return null;
    }

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
    public String getXmlIds(boolean withXmlHeader) {
        final String headers = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        StringBuilder result = new StringBuilder();

        if (withXmlHeader){
            result.append(headers);
        }
        result.append("<ids>\n") ;

        for (T resource : this) {
            result.append("<id>");
            if (resource==null){
                result.append(String.valueOf(null));
            }else{
                result.append(String.valueOf(resource.getId()));
            }
            result.append("</id>\n");
        }

        result.append("\n</ids>");
        return result.toString();
    }

    /**
     * <p>
     * returns something like : [28,29]
     *
     * @see #getXmlIds(boolean)
     * @return the xml representation of ids
     */
    public String getJsonIds() {

        StringBuilder result = new StringBuilder();


        result.append('[') ;
        result.append(joinIds(","));
        result.append(']');
        return result.toString();
    }

    /**
     * @return a list of all ids
     */
    public List<IdType> getIds() {
        ArrayList<IdType> ids = new ArrayList<IdType>();
        for (Resource<IdType> r : this) {
            ids.add(r.getId());
        }
        return ids;
    }

    StringBuilder joinIds (String glue){
        if (glue == null){
            throw new IllegalArgumentException("Glue is null");
        }
        int size = this.size();
        StringBuilder result = new StringBuilder();

        for (int i = 0 ; i < size ; i++) {
            T resource = this.get(i);

            if (resource==null){
                result.append(String.valueOf(null));
            }else{
                result.append(String.valueOf(resource.getId()));
            }
            if (i < size - 1 ){
                result.append(glue);
            }
        }
        return result;
    }

}










