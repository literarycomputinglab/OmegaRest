/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author simone
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

//    @Override
//    public Set<Object> getSingletons() {
//        
//        return Collections.emptySet();
//    }
//    @Override
//    public Map<String, Object> getProperties() {
//
//        Map<String, Object> properties = new HashMap<>();
//        
//        properties.put("jersey.config.server.wadl.disableWadl", true);
//        
//        return properties;
//    }
    private void addRestResourceClasses(Set<Class<?>> resources) {
    }

}
