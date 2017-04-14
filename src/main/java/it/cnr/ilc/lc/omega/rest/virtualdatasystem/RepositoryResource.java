/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.virtualdatasystem;

import it.cnr.ilc.lc.omega.resourcesystem.Collection;
import it.cnr.ilc.lc.omega.resourcesystem.ResourceSystemComponent;
import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author simone
 */

@Path("demo/repository")
public class RepositoryResource {
    
    @GET
    @Path("root")
    @Produces(MediaType.APPLICATION_JSON)
    public ResourceSystemComponent getRoot() {
        
        return ResourceSystemComponent.load(Collection.class, URI.create("/collection/root/000"));
    }
    
}
