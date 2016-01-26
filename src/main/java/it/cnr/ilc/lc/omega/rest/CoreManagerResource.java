/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

import it.cnr.ilc.lc.omega.core.OmegaCore;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
     * @return an instance of java.lang.String
     */
    @Path("/{action}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHtml(
            @DefaultValue("start") @QueryParam("action") String action
    ) {
        String ret = "Microkernel  " + action + " ok";
        //TODO return proper representation object
        if ("start".equals(action)) {
            //start MicroKernel
            OmegaCore.start();
        } else {
            OmegaCore.stop();
            //stop MicroKernel
        }
        return ret;
    }

}
