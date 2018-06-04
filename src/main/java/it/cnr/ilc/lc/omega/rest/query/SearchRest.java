/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.query;

import com.fasterxml.jackson.databind.node.NullNode;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.SearchManager;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.entity.Source;
import it.cnr.ilc.lc.omega.entity.TextContent;
import it.cnr.ilc.lc.omega.rest.annotation.handler.FactoryHandler;
import it.cnr.ilc.lc.omega.rest.annotation.handler.RestfulAnnotationHandler;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author simone
 */
@Path("/Search")
public class SearchRest {

    private static final Logger log = LogManager.getLogger(SearchRest.class);

    @Context
    private UriInfo context;

    @Part
    private static SearchManager searchManager;

    @Part
    static FactoryHandler factoryHandler;

    public SearchRest() {
    }

    @Path("text")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Text> getTextsByKeyword(@QueryParam("keyword") String keyword) {
        log.info("getTextsByKeyword(" + keyword + ")");
        try {
            return Text.loadBySearch(keyword);
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return null;
    }

    @Path("content")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContentByKeyword(@QueryParam("keyword") String keyword) {
        log.info("getContentByKeyword(" + keyword + ")");
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        ServiceResult sr;
        try {
            List<Source<TextContent>> lostc = searchManager.searchContentByKeyword(keyword);
            sr = new ServiceResult("0", "Result of getContentByKeyword(" + keyword + ")", lostc);

        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
            sr = new ServiceResult("-1", "Error processing getContentByKeyword(" + keyword + ")");

        }

        return rb.entity(sr).build();
    }

    @Path("annotation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnotationByKeyword(@QueryParam("keyword") String keyword) {
        log.info("getAnnotationByKeyword(" + keyword + ")");
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        ServiceResult sr;
        try {
            List<Annotation<TextContent, ?>> lostc = searchManager.searchAnnotationByKeyword(keyword);
            sr = new ServiceResult("0", "Result of getAnnotationByKeyword(" + keyword + ")", lostc);

        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
            sr = new ServiceResult("-1", "Error processing getAnnotationByKeyword(" + keyword + ")");

        }

        return rb.entity(sr).build();
    }

    @Path("annotation/{annotationType}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnotationByTypeAndField(@PathParam("annotationType") String type,
            @QueryParam("query") String query, @QueryParam("field") String field) {
        log.info("getAnnotationByTypeAndField type=(" + type + "), query=(" + query + "), field=(" + field + ")");
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        ServiceResult sr;
        try {

            RestfulAnnotationHandler rah = factoryHandler.build(type, NullNode.getInstance());
            List<Annotation<TextContent, ?>> lostc = searchManager.searchAnnotationByTypeAndField(rah.getAnnotationDataTypeClass(), query, field);
            sr = new ServiceResult("0", "Result of getAnnotationByTypeAndField: query=(" + query + "), tyep=(" + type + ")", lostc);

        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
            sr = new ServiceResult("-1", "Error processing getAnnotationByTypeAndField: query=(" + query + "), tyep=(" + type + ")");

        }

        return rb.entity(sr).build();
    }

}
