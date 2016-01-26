/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

import it.cnr.ilc.lc.omega.core.OmegaCore;
import it.cnr.ilc.lc.omega.core.ResourceManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import sirius.kernel.di.std.Part;

/**
 * REST Web Service
 *
 * @author simone
 */
@Path("coremanager")
@RequestScoped
public class CoreManagerResource {

    @Context
    private UriInfo context;
  
    /**
     * Creates a new instance of CoreManagerResource
     */
    public CoreManagerResource() {
    }

    /**
     * Retrieves representation of an instance of
     * it.cnr.ilc.lc.omega.rest.CoreManagerResource
     *
     * @param action
     * @return an instance of java.lang.String
     */
    @Path("/{action}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml(
            @PathParam("action") String action
    ) {
        System.out.println("parameter in CoreManager " + action);
        String ret = "Microkernel  " + action + " ok";
        //TODO return proper representation object
        if ("start".equals(action)) {
            System.out.println("starting methods");
            //start MicroKernel
            OmegaCore.start();
        } else {
            System.out.println("parameter in CoreManager " + action);
            OmegaCore.stop();
            //stop MicroKernel
        }
        System.out.println("returning getHtml " + ret);
        
        return ret;
    }

}

// webresources/coremanager/stop
