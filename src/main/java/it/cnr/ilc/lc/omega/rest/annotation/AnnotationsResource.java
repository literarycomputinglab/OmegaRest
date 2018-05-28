/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.ilc.lc.omega.adt.annotation.BaseAnnotationText;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAbstractAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import java.net.URI;
import java.util.logging.Level;
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

/**
 *
 * @author simone
 */
@Path("/annotation")
public class AnnotationsResource {

    private static final Logger log = LogManager.getLogger(AnnotationsResource.class);

    @Context
    private UriInfo context;

   // @Context
    //private ContextResolver<ObjectMapper> mapperResolver;

    public AnnotationsResource() {
    }

    @Path("{annotationType}/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response createText(@FormParam("") TextDTO text) {
    public Response createAnnotation(@PathParam("annotationType") String type, AnnotationDTO annDTO) {

        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());

        log.info("type is " + type);
        log.info("annDTO uri is " + annDTO.uri);

        try {
            String annotationType = "it.cnr.ilc.lc.omega.adt.annotation."+type;
            Class<T> annotationTypeClass = Class.forName(annotationType);
            String annotationDTO = "it.cnr.ilc.lc.omega.rest.annotation."+type+"DTO";
            Class<E> annotationDTOClass = Class.forName(annotationDTO);

            
            //final ObjectMapper mapper = mapperResolver.getContext(Object.class);
            ObjectMapper mapper = new ObjectMapper();
            (E) badto = mapper.treeToValue(annDTO.annotationData, annotationDTOClass);

            (T) bat = ADTAbstractAnnotation.of(annotationTypeClass, badto.text, annDTO.uri);

            bat.addLocus(Text.load(badto.textUri), badto.start, badto.end);
            
            //bat.save(); //FIXME salvare l'annotazione nel DB;
            
            rb.entity(new ServiceResult("0", "BaseAnnotation " + bat.toString() + ", annDTO=(" + annDTO + ") badto=(" + badto+")"));

            return rb.build();
            
        } catch (JsonProcessingException ex) {
            log.error(ex);
            // FIXME adding return rb.build
        } catch (ManagerAction.ActionException exAction) {
           log.error(exAction);
        }
        
        return null; // FIXME

    }

}
