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
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAbstractAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
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

    private final Map<String, Couple<Class<?>, Class<?>>> typeClassMap;

    {
        typeClassMap = new HashMap<>();
        typeClassMap.put("BaseAnnotationText", new Couple<>(BaseAnnotationText.class, BaseAnnotationTextDTO.class));
    }
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

            Class annotationTypeClass = typeClassMap.get("BaseAnnotationText").getFirst();

            Class annotationDTOClass = typeClassMap.get("BaseAnnotationText").getSecond();

            //final ObjectMapper mapper = mapperResolver.getContext(Object.class);
            ObjectMapper mapper = new ObjectMapper();
            Object badto = mapper.treeToValue(annDTO.annotationData, annotationDTOClass);

            ParameterHandler ph = new ParameterHandler(badto, typeClassMap.get(type));

            ADTAnnotation ann = ADTAbstractAnnotation.of(annotationTypeClass, annDTO.uri, ph.getBuildAnnotationParameter());

            ann = ph.populateAnnotation(ann);

            
            //bat.save(); //FIXME salvare l'annotazione nel DB;
            rb.entity(new ServiceResult("0", "BaseAnnotation " + ann.toString() + ", annDTO=(" + annDTO + ") badto=(" + badto + ")"));
            return rb.build();

        } catch (JsonProcessingException ex) {
            log.error(ex);
        }

        return null; // FIXME

    }

}
