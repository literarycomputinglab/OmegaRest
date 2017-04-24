/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.virtualdatasystem;

import it.cnr.ilc.lc.omega.exception.VirtualResourceSystemException;
import it.cnr.ilc.lc.omega.resourcesystem.Collection;
import it.cnr.ilc.lc.omega.resourcesystem.Resource;
import it.cnr.ilc.lc.omega.resourcesystem.ResourceSystemComponent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author simone
 */
@Path("demo/repository")
public class RepositoryResource {

    private static final Logger log = LogManager.getLogger(RepositoryResource.class);

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

}
