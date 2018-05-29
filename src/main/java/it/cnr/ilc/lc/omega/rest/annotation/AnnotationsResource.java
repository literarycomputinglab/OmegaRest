/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.lc.omega.adt.annotation.BaseAnnotationText;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Couple;
import it.cnr.ilc.lc.omega.core.datatype.ADTAbstractAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.rest.annotation.handler.FactoryHandler;
import it.cnr.ilc.lc.omega.rest.annotation.handler.RestfulAnnotationHandler;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
 * @author simone
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

    @Path("{annotationType}/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAnnotation(@PathParam("annotationType") String type, AnnotationDTO annDTO) {

        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());

        log.info("type is " + type);
        log.info("annDTO uri is " + annDTO.uri);

        RestfulAnnotationHandler restfullAnnotationHandler = factoryHandler.build(type, annDTO.annotationData);
        ADTAnnotation ann = ADTAbstractAnnotation.of(restfullAnnotationHandler.getAnnotationTypeClass(),
                annDTO.uri, restfullAnnotationHandler.getBuildAnnotationParameter());
        ann = restfullAnnotationHandler.populateAnnotation(ann);
       // restfullAnnotationHandler.saveAnnotation(ann);
        rb.entity(new ServiceResult("0", "BaseAnnotation " + ann.toString() + ", annDTO=(" + annDTO + ")"));
        return rb.build();

    }

}
