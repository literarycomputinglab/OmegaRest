/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest;

import it.cnr.ilc.lc.omega.core.ManagerAction;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
        //OmegaCore.start();
    }

    /**
     * Retrieves representation of an instance of
     * it.cnr.ilc.lc.omega.rest.TextResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/create")
    @Produces("text/html")
    public String setText() throws InvalidURIException {
        String ret = "vuota!!!!!!!!!!?";
        try {
            //TODO return proper representation object
            /*ResourceManager manager = new ResourceManager();
             try {
             manager.createSource(URI.create("java/fa/caa"), new MimeType("text/plain"));
             } catch (MimeTypeParseException ex) {
             Logger.getLogger(TextResource.class.getName()).log(Level.SEVERE, null, ex);
             }*/

            Text text = Text.of("boiadé", URI.create("java/fa/caa"));
            ret = text.toString();
            //text.save();
        } catch (ManagerAction.ActionException ex) {
            Logger.getLogger(TextResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidURIException ex) {
            Logger.getLogger(TextResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "<html><head><meta charset=\"utf-8\" /></head><body><h1>" + "Creata la Risorsa" + "</h1><p>"+ret+"</p></body></html>";
    }

    @GET
    @Path("/load")
    @Produces("text/html")
    public String getText() throws InvalidURIException {
        String ret = "Testo non caricato!!";

        Text text = null;
        try {
            text = Text.load(URI.create("java/fa/caa"));
        } catch (ManagerAction.ActionException ex) {
            Logger.getLogger(TextResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (text != null) {
            ret = text.toString();
        }

        return "<html><head><meta charset=\"utf-8\" /></head><body><h1>" + "La risorsa caricata" + "</h1><p>"+ ret +"</p></body></html>";
    }

}
