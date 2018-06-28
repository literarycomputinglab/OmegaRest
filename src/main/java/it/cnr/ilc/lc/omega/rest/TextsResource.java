/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

/**
 *
 * @author simone
 * @author angelo
 */
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.exception.InvalidURIException;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import it.cnr.ilc.lc.omega.rest.servicemodel.TextDTO;
import it.cnr.ilc.lc.omega.rest.servicemodel.TextUri;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import javax.ejb.RemoveException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author simone
 * @author angelo
 */
@Path("/Texts")
public class TextsResource {

    @Context
    private UriInfo context;

    private static final Logger log = LogManager.getLogger(TextsResource.class);

    public TextsResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTextRefs() {
        log.info("getAllTextRefs()");
        List<TextUri> ltu;
        Response.ResponseBuilder rb = Response.status(Response.Status.SERVICE_UNAVAILABLE);
        rb.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS");
        try {
            ltu = TextUri.toTextUri(Text.loadAll());
            rb.status(Response.Status.OK);
            return rb.entity(ltu).build();

        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return rb.build();
    }

    @Path("text/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response createText(@FormParam("") TextDTO text) {
    public Response createText(TextDTO text) {
        log.info("createText(" + text + ")");
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        Text t = null;
        URI uri = null;

        try {

            uri = URI.create(text.uri);
            if (null != text.text) {
                t = Text.of(text.text, uri);
            } else {
                t = Text.of(uri.toURL());
            }
            t.save();

        } catch (NullPointerException npe) {
            log.error("createText: ", npe);
            rb.entity(new ServiceResult("1", "Error, " + ExceptionUtils.getRootCauseMessage(npe)));
            return rb.build();

        } catch (ManagerAction.ActionException ex) {
            log.error("createText: " + ex.getMessage());
            rb.entity(new ServiceResult("2", "Error, " + ex.getLocalizedMessage()));
            return rb.build();

        } catch (InvalidURIException iue) {
            log.error("createText: " + iue.getMessage());
            rb.entity(new ServiceResult("3", "Invalid URI, " + iue.getLocalizedMessage()));
            return rb.build();

        } catch (IllegalArgumentException iae) {
            log.error("createText: " + iae.getMessage());
            rb.entity(new ServiceResult("4", "Invalid URI, " + iae.getLocalizedMessage()));
        } catch (MalformedURLException mue) {
            log.error("createText: " + mue.getMessage());
            rb.entity(new ServiceResult("5", "Invalid URL, " + mue.getLocalizedMessage()));
        }
        log.info("createText: ok!");

        return rb.entity(new ServiceResult()).build();
    }

    @Path("text{uri: [\\w/]+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTextByUri(@PathParam("uri") String uri) {
        log.info("getTextByUri(" + uri + ")");
        Response.ResponseBuilder rb = Response.status(Response.Status.SERVICE_UNAVAILABLE);
        rb.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS");
        Text text;

        try {
            text = Text.load(URI.create(uri));
            if (null != text) {
                rb.status(Response.Status.FOUND);
                return rb.entity(text).build();
            } else {
                throw new Error("error during loading of text with uri: ("+uri+")");
            }
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return rb.build();
    }

    @Path("text/delete{uri: [\\w/]+}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeText(@PathParam("uri") URI textUri) {
        log.info("remove Text with uri: (" + textUri + ")");
        Response.ResponseBuilder rb = Response.status(Response.Status.OK);
        rb.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS");
        Text text;

        try {

            if (null == textUri) {
                throw new IllegalArgumentException("impossible to remove the text as the uri is null");
            }

            text = Text.load(textUri); // //source/text/hillary/curri

            if (text == null || text.getSource() == null || text.getSource().getContent() == null) {
                throw new InstantiationException("The text with uri: (" + textUri.toASCIIString() + ") cannot be loaded");
            }

            text = (Text) Text.delete(text); // FIXME: se ci sono loci entranti bisogna veramente cancellare??

            if (text != null) {
                throw new Error("The text with the uri: (" + textUri + ") cannot be removed");
            }

        } catch (ManagerAction.ActionException ex) {
            log.error(ex.getLocalizedMessage());
            rb = rb.status(Response.Status.INTERNAL_SERVER_ERROR);
            rb.entity(new ServiceResult("1", "Error on loading text or deleting text, " + ExceptionUtils.getRootCauseMessage(ex)));
            log.error("Error delating text!");
            return rb.build();

        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            rb = rb.status(Response.Status.BAD_REQUEST);
            rb.entity(new ServiceResult("2", "Error on URI, " + ExceptionUtils.getRootCauseMessage(e)));
            log.error("Error delating text as URI is null!");
            return rb.build();
        } catch (InstantiationException iex) {
            log.error(iex.getLocalizedMessage());
            rb = rb.status(Response.Status.INTERNAL_SERVER_ERROR);
            rb.entity(new ServiceResult("3", "Error on loading text, " + ExceptionUtils.getRootCauseMessage(iex)));
            log.error("Error delating text as loading failed!");
            return rb.build();
        } catch (Error rex) {
            log.error(rex.getLocalizedMessage());
            rb = rb.status(Response.Status.INTERNAL_SERVER_ERROR);
            rb.entity(new ServiceResult("4", "Error on deleting text, " + ExceptionUtils.getRootCauseMessage(rex)));
            log.error("Error delating text!");
            return rb.build();
        }

        return rb.entity(String.format("uri: %s\n\n text: %s\n\n", textUri.toASCIIString(), text.getTextContent())).build();
    }

//    @Path("annotations")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<AnnotationUri> getAnnotations() {
//
//        log.info("getAnnotations()");
//        try {
//            return AnnotationUri.toAnnotationUri(Text.loadAllAnnotations());
//        } catch (ManagerAction.ActionException ex) {
//            log.fatal(ex);
//        }
//        return null;
//    }
//
//    @Path("annotations/annotation")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Annotation getAnnotation(@QueryParam("uri") String uri) {
//
//        log.info("getAnnotation (" + uri + ")");
//        try {
//            return Text.loadAnnotation(URI.create(uri));
//        } catch (ManagerAction.ActionException ex) {
//            log.fatal(ex);
//        }
//        return null;
//    }
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
