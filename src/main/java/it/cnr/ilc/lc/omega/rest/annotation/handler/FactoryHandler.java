/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation.handler;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Parts;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
@Register(classes = {FactoryHandler.class})
public final class FactoryHandler {

    private static Logger log = LogManager.getLogger(FactoryHandler.class);

    @Parts(value = RestfulAnnotationHandler.class)
    private Collection<RestfulAnnotationHandler> handlers;

    public FactoryHandler() {

    }

    public static FactoryHandler create() {
        return new FactoryHandler();
    }

    public RestfulAnnotationHandler build(String type, JsonNode annDTO) {

        for (RestfulAnnotationHandler handler : handlers) {
            if (handler.getType().equals(type)) {
                handler.init(annDTO);
                return handler;
            }
        }
        throw new IllegalArgumentException("Unable to find a suitable handler for type " + type);
    }

}
