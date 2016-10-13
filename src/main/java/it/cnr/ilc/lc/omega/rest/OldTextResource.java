///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package it.cnr.ilc.lc.omega.rest;
//
//import it.cnr.ilc.lc.omega.core.ManagerAction;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.UriInfo;
//import javax.ws.rs.Produces;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import it.cnr.ilc.lc.omega.core.datatype.Text;
//import it.cnr.ilc.lc.omega.exception.InvalidURIException;
//import it.cnr.ilc.lc.omega.text.api.TextService;
//import java.net.URI;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.FormParam;
//import javax.ws.rs.POST;
//import javax.ws.rs.QueryParam;
//import sirius.kernel.di.std.Part;
//
///**
// * REST Web Service
// *
// * @author simone
// */
//@Path("text")
public class OldTextResource {
//
//    @Context
//    private UriInfo context;
//    
//    @Part
//    private TextService textService;
//
//    /**
//     * Creates a new instance of TextResource
//     */
//    public OldTextResource() {
//        //OmegaCore.start();
//    }
//
//    /**
//     * Retrieves representation of an instance of
// it.cnr.ilc.lc.omega.rest.OldTextResource
//     *
//     * @return an instance of java.lang.String
//     */
///*    @GET
//    @Path("/create")
//    @Produces("text/html")*/
//    public String setText() throws InvalidURIException {
//        String ret = "vuota!!!!!!!!!!?";
//        try {
//
//            Text text = Text.of("boiadé", URI.create("java/fa/caa"));
//            ret = text.toString();
//            //text.save();
//        } catch (ManagerAction.ActionException ex) {
//            Logger.getLogger(OldTextResource.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidURIException ex) {
//            Logger.getLogger(OldTextResource.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "<html><head><meta charset=\"utf-8\" /></head><body><h1>" + "Creata la Risorsa" + "</h1><p>" + ret + "</p></body></html>";
//    }
//
//    @GET
//    @Path("/load")
//    @Produces("text/html")
//    public String getText() throws InvalidURIException {
//        String ret = "Testo non caricato!!";
//
//        Text text = null;
//        try {
//            text = Text.load(URI.create("java/fa/caa"));
//        } catch (ManagerAction.ActionException ex) {
//            Logger.getLogger(OldTextResource.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (text != null) {
//            ret = text.toString();
//        }
//
//        return "<html><head><meta charset=\"utf-8\" /></head><body><h1>" + "La risorsa caricata" + "</h1><p>" + ret + "</p></body></html>";
//    }
//
//    @POST
//    @Consumes("application/x-www-form-urlencoded")
//    public void createText(@FormParam("text") String text, @FormParam("uri") String uri) {
//        System.err.println("Create text web api ok! text : " + text + " uri: " + uri);
//    }
//
//    
//    
//    
//    @GET
//    @Path("/create")
//    @Produces("text/html")
//    public String  createTextGet(
//            @QueryParam("text") String text,
//            @QueryParam("uri") String uri) {
//
//        System.err.println("Create text web apu ok! text : " + text + " uri: " + uri);
//        textService.createText(text, uri);
//        
//        return "<html><h1>" + textService.getText() + "</h1></html>";
//    }
//
}
