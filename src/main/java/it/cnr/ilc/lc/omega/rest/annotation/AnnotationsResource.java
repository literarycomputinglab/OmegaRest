/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import com.fasterxml.jackson.databind.node.NullNode;
import it.cnr.ilc.lc.omega.adt.annotation.BaseAnnotationText;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAbstractAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.entity.TextContent;
import it.cnr.ilc.lc.omega.rest.annotation.handler.FactoryHandler;
import it.cnr.ilc.lc.omega.rest.annotation.handler.RestfulAnnotationHandler;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Part;

/**
 *
 * @author simone
 * @author angelo
 */
@Path("/annotation")
public class AnnotationsResource {

    private static final Logger log = LogManager.getLogger(AnnotationsResource.class);

    @Context
    private UriInfo context;

    @Part
    static FactoryHandler factoryHandler;

    public AnnotationsResource() {
    }

    @GET
    @Path("a{uri: [\\w/]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnotation(@PathParam("uri") String uri) {
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        ServiceResult sr;
        log.info("getAnnotation (" + uri + ")");
        try {

            Annotation<TextContent, ?> ann = ADTAbstractAnnotation.loadTextAnnotation(URI.create(uri));
            List<Annotation<TextContent, ?>> list = new ArrayList<>();
            list.add(ann);
            sr = new ServiceResult("0", "Result  searching Annotation with uri=(" + uri + ")", list);

            log.info("Loaded Annotation with uri=(" + uri + ")");

        } catch (ManagerAction.ActionException ex) {
            sr = new ServiceResult("-1", "Error searching Annotation with uri=(" + uri + ")");
            log.fatal(ex);
        }
        return rb.entity(sr).build();
    }

    @Path("{annotationType}/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAnnotation(@PathParam("annotationType") String type, AnnotationDTO annDTO) {

        Response.ResponseBuilder rb = Response.noContent()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS");

        if (!annDTO.check()) {
            log.error("Invalid DTO " + annDTO.toString());
            rb = Response.serverError()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS");
            rb.entity(new ServiceResult("-1", "ERR: One or more parameters are null, parameter=(" + annDTO.toString() + ")"));
        } else {

            log.info("annotation DTO " + annDTO.toString());

            try {
                RestfulAnnotationHandler restfullAnnotationHandler = factoryHandler.build(type, annDTO.annotationData);
                
                ADTAnnotation ann = ADTAbstractAnnotation.of(restfullAnnotationHandler.getAnnotationTypeClass(),
                        annDTO.uri, restfullAnnotationHandler.getBuildAnnotationParameter());
                restfullAnnotationHandler.populateAnnotation(ann);
                restfullAnnotationHandler.saveAnnotation(ann);
                
                rb = Response.created(context.getAbsolutePath())
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS");
                rb.entity(new ServiceResult("0", "Created annotation of type " + type + " with uri " + annDTO.uri));
                
            } catch (IllegalArgumentException iae) {
                log.error(iae.getLocalizedMessage());
                rb = Response.serverError()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS");
                rb.entity(new ServiceResult("-2", "ERR: One or more parameters are null, parameter=(" + annDTO.annotationData.toString() + ")"));

            } catch (ProcessingException pe) {
                log.error(pe.getLocalizedMessage());
                rb = Response.serverError()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS");
                rb.entity(new ServiceResult("-3", "ERR: Saving annotation, cause=(" + pe.getCause().getMessage() + "), parameter=(" + annDTO.toString() + ")"));

            }
        }
        return rb.build();

    }

    @Path("/{type: \\w+}/delete{uri: [\\w/]+}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAnnotation(@PathParam("type") String annotationType, @PathParam("uri") String aUri) {

        log.info("DELETE request for Annotation type=(" + annotationType + "), uri=(" + aUri + ")");
        Response.ResponseBuilder rb = Response.status(Response.Status.OK)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS");
        URI uri = null;
        try {        //log.info("Request of remove a colelction identified by uri=(" + aUri + ")");

            uri = URI.create(aUri);
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            rb = rb.status(Response.Status.BAD_REQUEST);
            rb.entity(new ServiceResult("2", "Error on URI, " + ExceptionUtils.getRootCauseMessage(e)));
            log.error("Error creating URI for " + annotationType + " idenfied by uri=(" + aUri + ")");
            return rb.build();
        }

        ADTAnnotation toBeRemoved = null;
        try {
            RestfulAnnotationHandler restfullAnnotationHandler = factoryHandler.build(annotationType, NullNode.getInstance());
            toBeRemoved = ADTAbstractAnnotation.load(uri, restfullAnnotationHandler.getAnnotationTypeClass());
            log.info(toBeRemoved);
            toBeRemoved = ADTAbstractAnnotation.delete(toBeRemoved);
        } catch (IllegalArgumentException | ManagerAction.ActionException e) {
            log.info(e);
        }

        if (null == toBeRemoved) {
            //rimosso ok
            rb.entity(new ServiceResult("0", annotationType + " identified by uri=(" + aUri + ") has been removed"));
            log.info(annotationType + " identified by uri=(" + aUri + ") has been removed");

        } else {
            //non rimosso
            rb.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new ServiceResult("1", "Error removing " + annotationType + " idenfied by uri=(" + aUri + ")"));
            log.error("Error removing " + annotationType + " idenfied by uri=(" + aUri + ")");

        }
        return rb.build();

    }
}
