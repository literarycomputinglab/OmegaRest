/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.query;

import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author simone
 */
@Path("/Search")
public class Search {

    private static final Logger log = LogManager.getLogger(Search.class);

  //  @Part
 //   private static SearchManager sm;
    
    public Search() {
    }
    

    @Path("text")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Text> getTextsByKeyword(@QueryParam("keyword") String keyword) {
        log.info("getTextsByKeyword(" + keyword + ")");
        try {

            return Text.loadBySearch(keyword);
        } catch (ManagerAction.ActionException ex) {
            log.fatal(ex);
        }
        return null;
    }

}
