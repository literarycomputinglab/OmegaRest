/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.SearchManager;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author angelo
 * @author simone
 */
@Path("/Index")
public class IndexResource {

    @Context
    private UriInfo context;

    @Part
    private static SearchManager searchManager;

    private static final Logger LOG = LogManager.getLogger(IndexResource.class);

    public IndexResource() {

    }

    @POST
    @Path("reindexing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reindexing() {
        LOG.info("reindexing the internal intedex");
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        responseBuilder.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS");

        try {
            Boolean status = searchManager.reindex();
            responseBuilder = responseBuilder.status(status ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR);
            responseBuilder.entity(new ServiceResult());
        } catch (ManagerAction.ActionException e) {
            LOG.error("Error reindexing resources", e);
            responseBuilder.entity(new ServiceResult("1", "Error on reindex: " + e.getLocalizedMessage()));
        }

        return responseBuilder.build();
    }

}
