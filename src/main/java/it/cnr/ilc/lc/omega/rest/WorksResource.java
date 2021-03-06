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
import it.cnr.ilc.lc.omega.adt.annotation.Work;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Authors;
import it.cnr.ilc.lc.omega.adt.annotation.dto.DTOValue;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Loci;
import it.cnr.ilc.lc.omega.adt.annotation.dto.PubblicationDate;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Title;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.entity.TextLocus;
import it.cnr.ilc.lc.omega.exception.InvalidURIException;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import it.cnr.ilc.lc.omega.rest.servicemodel.WorkDTO;
import it.cnr.ilc.lc.omega.rest.servicemodel.WorkUri;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
@Path("/Works")
public class WorksResource {

    @Context
    private UriInfo context;

    private static final Logger log = LogManager.getLogger(WorksResource.class);

    public WorksResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkUri> getAllWorkRefs() {
        log.info("getAllWorkRefs()");
        try {

            return WorkUri.toWorkUri(Work.loadAll());
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return null;
    }

    @Path("work/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response createText(@FormParam("") TextDTO text) {
    public Response createWork(WorkDTO work) {
        log.info("createWork(" + work + ")");
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        Work w = null;

        try {

            URI textUri = URI.create(work.textUri);
            URI workUri = URI.create(work.workUri);
            Authors authors = work.authors;
            PubblicationDate pd = work.pd;
            Title title = work.title;

            w = Work.of(authors, pd, title, workUri);

            TextLocus tl = Work.createTextLocus(Text.load(textUri).getSource());
            Loci loci = DTOValue.instantiate(Loci.class).withValue(tl);
            w.addLoci(loci);

            w.save();

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
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WorksResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WorksResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("createText: ok!");

        return rb.entity(new ServiceResult()).build();
    }
    /*
    @Path("text")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Work getTextByUri(@QueryParam("uri") String uri) {
        log.info("getTextByUri(" + uri + ")");
        try {

            return Work.load(URI.create(uri));
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return null;
    }

    @Path("annotations")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AnnotationUri> getAnnotations() {

        log.info("getAnnotations()");
        try {
            return AnnotationUri.toAnnotationUri(Work.loadAllAnnotations());
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return null;
    }

    @Path("annotations/annotation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Annotation getAnnotation(@QueryParam("uri") String uri) {

        log.info("getAnnotation (" + uri + ")");
        try {
            return Work.loadAnnotation(URI.create(uri));
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return null;
    }
     */
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
