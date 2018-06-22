/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.virtualdatasystem;

import it.cnr.ilc.lc.omega.adt.annotation.CatalogItem;
import it.cnr.ilc.lc.omega.adt.annotation.Work;
import it.cnr.ilc.lc.omega.annotation.catalog.ResourceSystemAnnotationType;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import it.cnr.ilc.lc.omega.resourcesystem.Collection;
import it.cnr.ilc.lc.omega.resourcesystem.Resource;
import it.cnr.ilc.lc.omega.resourcesystem.ResourceSystemComponent;
import it.cnr.ilc.lc.omega.resourcesystem.dto.DTOValueRSC;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCParent;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCType;
import it.cnr.ilc.lc.omega.rest.servicemodel.RSComponentDTO;
import it.cnr.ilc.lc.omega.rest.servicemodel.ServiceResult;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
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
 *
 * @author simone
 */
@Path("demo/repository")
public class RepositoryResource {

    private static final Logger log = LogManager.getLogger(RepositoryResource.class);

    @Context
    private UriInfo context;

    @GET
    @Path("root")
    @Produces(MediaType.APPLICATION_JSON)
    public ResourceSystemComponent getRoot() {

        ResourceSystemComponent rsc = getResourceSystemComponent("/collection/root/000");

        return rsc;
    }

    @GET
    @Path("root{uri: [\\w/]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResourceSystemComponent getResourceSystemComponent(@PathParam("uri") String aUri) {

        log.info("URI: " + aUri);

        ResourceSystemComponent component = null;
        try {
            component = getRSCCollection(aUri);
        } catch (VirtualResourceSystemException e) {

            log.warn("Unable to load Collection reffered by URI: " + aUri, e.getMessage());
            try {
                component = getRSCResource(aUri);
            } catch (VirtualResourceSystemException ex) {
                log.error("Unable to load Resource reffered by URI: " + aUri, ex);
            }
        } catch (Exception e2) {
            log.error("Unable to load ResourceSystemComponent reffered by URI: " + aUri, e2);
        }

        if (null != component) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os);
            component.print(ps);
            log.info(String.format("%s", os.toString()));
        } else {
            log.warn(String.format("The requested resource referred by URI %s was not loaded", aUri));
        }

        return component;

    }

    @Path("resource/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response createText(@FormParam("") TextDTO text) {
    public Response createResource(RSComponentDTO dto) {
//ResourceSystemComponent firstLevel = ResourceSystemComponent.of(Resource.class, URI.create("/collection/first/001"))
//                .withParams(firstName, firstDescription, firstType, rootParent);
        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());

        try {

            Collection parentCollection = ResourceSystemComponent.load(Collection.class, dto.uriParent);
            RSCType type = DTOValueRSC.instantiate(RSCType.class).withValue(ResourceSystemAnnotationType.RESOURCE);
            RSCParent parent = DTOValueRSC.instantiate(RSCParent.class).withValue(parentCollection);

            ResourceSystemComponent resource = ResourceSystemComponent.of(Resource.class, dto.uri)
                    .withParams(dto.name, dto.description, type, parent);

            resource.setResourceContent(load(dto.catalogItemUri));
            parentCollection.add(resource);
            parentCollection.save();

        } catch (InstantiationException ie) {
            log.error(ie.getLocalizedMessage());
            rb.entity(new ServiceResult("1", "Error, " + ExceptionUtils.getRootCauseMessage(ie)));
            return rb.build();
        } catch (IllegalAccessException iae) {
            log.error(iae.getLocalizedMessage());
            rb.entity(new ServiceResult("2", "Error, " + ExceptionUtils.getRootCauseMessage(iae)));
            return rb.build();
        } catch (VirtualResourceSystemException vrse) {
            log.error(vrse.getLocalizedMessage());
            rb.entity(new ServiceResult("3", "Error, " + ExceptionUtils.getRootCauseMessage(vrse)));
            return rb.build();
        } catch (ManagerAction.ActionException ex) {
            log.error(ex.getLocalizedMessage());
            rb.entity(new ServiceResult("4", "Error, " + ExceptionUtils.getRootCauseMessage(ex)));
            return rb.build();
        } catch (Annotation.Data.InvocationMethodException ime) {
            log.error(ime.getLocalizedMessage());
            rb.entity(new ServiceResult("5", "Error, " + ExceptionUtils.getRootCauseMessage(ime)));
            return rb.build();
        }

        log.info("resource created");

        return rb.entity(new ServiceResult()).build();
    }

    @Path("collection/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response createText(@FormParam("") TextDTO text) {
    public Response createCollection(RSComponentDTO dto) {

        Response.ResponseBuilder rb = Response.created(context.getAbsolutePath());
        try {
            Collection parentCollection = ResourceSystemComponent.load(Collection.class, dto.uriParent);
            RSCType type = DTOValueRSC.instantiate(RSCType.class).withValue(ResourceSystemAnnotationType.COLLECTION);
            RSCParent parent = DTOValueRSC.instantiate(RSCParent.class).withValue(parentCollection);
            ResourceSystemComponent collection = ResourceSystemComponent.of(Collection.class, dto.uri)
                    .withParams(dto.name, dto.description, type, parent);

            parentCollection.add(collection);
            parentCollection.save();

        } catch (InstantiationException ie) {
            log.error(ie.getLocalizedMessage());
            rb.entity(new ServiceResult("1", "Error, " + ExceptionUtils.getRootCauseMessage(ie)));
            return rb.build();
        } catch (IllegalAccessException iae) {
            log.error(iae.getLocalizedMessage());
            rb.entity(new ServiceResult("2", "Error, " + ExceptionUtils.getRootCauseMessage(iae)));
            return rb.build();
        } catch (VirtualResourceSystemException vrse) {
            log.error(vrse.getLocalizedMessage());
            rb.entity(new ServiceResult("3", "Error, " + ExceptionUtils.getRootCauseMessage(vrse)));
            return rb.build();
        } catch (ManagerAction.ActionException ex) {
            log.error(ex.getLocalizedMessage());
            rb.entity(new ServiceResult("4", "Error, " + ExceptionUtils.getRootCauseMessage(ex)));
            return rb.build();
        } catch (Annotation.Data.InvocationMethodException ime) {
            log.error(ime.getLocalizedMessage());
            rb.entity(new ServiceResult("5", "Error, " + ExceptionUtils.getRootCauseMessage(ime)));
            return rb.build();
        }

        log.info("collection created!");

        return rb.entity(new ServiceResult()).build();
    }

    @Path("/{type:collection|resource}/delete{uri: [\\w/]+}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFileSystemComponent(@PathParam("type") String type, @PathParam("uri") String aUri) {
        log.info("Request of remove a " + type + " identified by uri=(" + aUri + ")");

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
            return rb.build();
        }

        ResourceSystemComponent root = getResourceSystemComponent("/collection/root2/col000");
        ResourceSystemComponent toBeRemoved = root.getChild(uri);
        toBeRemoved = root.remove(toBeRemoved);
        if (null == toBeRemoved) {
            //rimosso ok
            rb.entity(new ServiceResult("0", type + " identified by uri=(" + aUri + ") has been removed"));
        } else {
            //non rimosso
            rb.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new ServiceResult("1", "Error removing "+type+" idenfied by uri=(" + aUri + ")"));
        }
        return rb.build();
    }

    private ResourceSystemComponent getRSCCollection(String aUri) throws VirtualResourceSystemException {

        log.info(String.format("trying to get a Collection referred by URI %s", aUri));

        ResourceSystemComponent rsc = ResourceSystemComponent.load(Collection.class, URI.create(aUri));

        return rsc;

    }

    private ResourceSystemComponent getRSCResource(String aUri) throws VirtualResourceSystemException {

        log.info(String.format("trying to get a Resource referred by URI %s", aUri));

        ResourceSystemComponent rsc = ResourceSystemComponent.load(Resource.class, URI.create(aUri));

        return rsc;
    }

    private CatalogItem load(URI catalogItemUri) {

        try {
            return Work.load(catalogItemUri); //FIXME: generalizzare: si può linkare qualcsiasi CatalogItem (Es. DublinCode)
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
        }
        return null;
    }

}
