/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import it.cnr.ilc.lc.omega.persistence.PersistenceHandler;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import sirius.kernel.di.std.Part;

/**
 *
 * @author angelo
 */
/**
 * Jackson JSON processor could be controlled via providing a custom Jackson
 * ObjectMapper instance. This could be handy if you need to redefine the
 * default Jackson behavior and to fine-tune how your JSON data structures look
 * like (copied from Jersey web site).
 *
 *
 * @see https://jersey.java.net/documentation/latest/media.html#d0e4799
 */
@Provider
//@Produces({MediaType.APPLICATION_JSON})
//@Consumes(MediaType.APPLICATION_JSON)
//@Singleton
public class MyJacksonJsonProvider implements ContextResolver<ObjectMapper> {

    private static Logger logger = LogManager.getLogger(MyJacksonJsonProvider.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Part
    static PersistenceHandler persistence;

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        //MAPPER.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        //MAPPER.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    }

    public MyJacksonJsonProvider() {
        Hibernate5Module h5m = new Hibernate5Module((SessionFactory) persistence.getEntityManager().getEntityManagerFactory().
                unwrap(HibernateEntityManagerFactory.class).
                getSessionFactory());
        h5m.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        h5m.enable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        h5m.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        MAPPER.registerModule(h5m);

        logger.info("Instantiate MyJacksonJsonProvider");
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        logger.info("MyJacksonProvider.getContext() called with type: " + type);
        logger.info(MAPPER.getSerializationConfig());

        return MAPPER;
    }
}
