/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

import it.cnr.ilc.lc.omega.core.ManagerAction;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.exception.InvalidURIException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST Web Service
 *
 * @author simone
 */
@Path("text")
public class TextResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TextResource
     */
    public TextResource() {
    }

    /**
     * Retrieves representation of an instance of
     * it.cnr.ilc.lc.omega.rest.TextResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/html")
    public String getHtml() {
        String ret = "vuota!!!!!!!!!!";
        try {
            //TODO return proper representation object
            Text text = Text.of("boiadé", URI.create("java/fa/caa"));
            ret  = text.toString();
        } catch (ManagerAction.ActionException ex) {
            Logger.getLogger(TextResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidURIException ex) {
            Logger.getLogger(TextResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "<html><h1>" + ret + "</h1></html>";
    }

}
