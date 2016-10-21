/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

/**
 *
 * @author simone
 */
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.rest.servicemodel.AnnotationUri;
import it.cnr.ilc.lc.omega.rest.servicemodel.TextUri;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author angelo
 */
@Path("/TextsSentence")
public class TextsResource {

    private static Logger logger = LogManager.getLogger(TextsResource.class);

    public TextsResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TextUri> getAllTextRefs() {
        logger.info("getAllTextRefs");
        try {

            return TextUri.toTextUri(Text.loadAll());
        } catch (ManagerAction.ActionException ex) {
            logger.fatal(ex);
        }
        return null;
    }

    @Path("text")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Text getTextByUri(@QueryParam("uri") String uri) {
        logger.info("getTextByUri(" + uri + ")");
        try {

            return Text.load(URI.create(uri));
        } catch (ManagerAction.ActionException ex) {
            logger.fatal(ex);
        }
        return null;
    }

    @Path("annotations")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AnnotationUri> getAnnotations() {

        logger.info("getAnnotations");
        try {
            return AnnotationUri.toAnnotationUri(Text.loadAllAnnotations());
        } catch (ManagerAction.ActionException ex) {
            logger.fatal(ex);
        }
        return null;
    }

    @Path("annotations/annotation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Annotation getAnnotation(@QueryParam("uri") String uri) {

        logger.info("getAnnotation (" + uri +")");
        try {
            return Text.loadAnnotation(URI.create(uri));
        } catch (ManagerAction.ActionException ex) {
            logger.fatal(ex);
        }
        return null;
    }
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void postJson(String name) {
//        logger.info(name);
//        DB.getInstance().add(TextSentence.of("ciao", "come stai"), name);
//    }
//
//    @Path("{name}")
//    public TextsResource getTextSentenceResource(@PathParam("name") String name) {
//        logger.info("getTextSentence");
//        logger.info(name);
//        return TextsResource.get(name);
//
//    }
}
