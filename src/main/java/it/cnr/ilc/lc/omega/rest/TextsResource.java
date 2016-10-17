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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public List<Text> getAllTexts() {
        logger.info("getAllText");
        try {
            return Text.loadAll();
        } catch (ManagerAction.ActionException ex) {
            java.util.logging.Logger.getLogger(TextsResource.class.getName()).log(Level.SEVERE, null, ex);
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
