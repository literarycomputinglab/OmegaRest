/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import it.cnr.ilc.lc.omega.adt.annotation.LexoTerm;
import it.cnr.ilc.lc.omega.adt.annotation.dto.lexo.LeftTextContext;
import it.cnr.ilc.lc.omega.adt.annotation.dto.lexo.LexoDTOValue;
import it.cnr.ilc.lc.omega.adt.annotation.dto.lexo.RightTextContext;
import it.cnr.ilc.lc.omega.adt.annotation.dto.lexo.TextFragment;
import it.cnr.ilc.lc.omega.adt.annotation.dto.lexo.UriSense;
import it.cnr.ilc.lc.omega.annotation.LexoAnnotation;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.rest.annotation.LexoTermDTO;
import javax.ws.rs.ProcessingException;
import org.apache.logging.log4j.LogManager;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
@Register(classes = {RestfulAnnotationHandler.class}, name = "LexoTerm")

public class LexoAnnotationParameterHandler implements RestfulAnnotationHandler {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(LexoAnnotationParameterHandler.class);

    LexoTermDTO lexoTermDTO;

    private final String type = "LexoTerm";
    private final Class<LexoTerm> typeClass = LexoTerm.class;
    private final Class<LexoAnnotation> dataTypeClass = LexoAnnotation.class;

    @Override
    public String getType() {
        return type;
    }

    public LexoAnnotationParameterHandler() {
    }

    @Override
    public Object[] getBuildAnnotationParameter() {
        return new Object[]{};
    }

    @Override
    public ADTAnnotation populateAnnotation(ADTAnnotation ann) {

        try {
            LexoTerm lexoTerm = (LexoTerm) ann;
            log.error("lexoTermDTO = " + lexoTermDTO.toString());
            lexoTerm = lexoTerm.withParams(LexoDTOValue.instantiate(UriSense.class).withValue(lexoTermDTO.senseUri),
                    LexoDTOValue.instantiate(TextFragment.class).withValue(lexoTermDTO.fragment),
                    LexoDTOValue.instantiate(LeftTextContext.class).withValue(lexoTermDTO.leftTermContext),
                    LexoDTOValue.instantiate(RightTextContext.class).withValue(lexoTermDTO.rightTermContext))
                    .build();
            lexoTerm.addLocus(Text.load(lexoTermDTO.textUri), lexoTermDTO.start, lexoTermDTO.end);
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
        } catch (InstantiationException ex) {
            log.error(ex);
        } catch (IllegalAccessException ex) {
            log.error(ex);
        }
        return ann;
    }

    @Override
    public ADTAnnotation saveAnnotation(ADTAnnotation ann) {
        LexoTerm lexoTerm = (LexoTerm) ann;
        try {
            log.info("Saving Lexo Term");
            lexoTerm.save();
            log.info("Lexo Term saved");
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
            throw new ProcessingException("Error saving the annotation ", ex.getCause());
        }
        return ann;
    }

    @Override
    public void init(JsonNode jsonAnnotationDTO) {

        try {
            if (jsonAnnotationDTO != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                lexoTermDTO = mapper.treeToValue(jsonAnnotationDTO, LexoTermDTO.class);
                if (!(jsonAnnotationDTO instanceof NullNode)) {
                    if (lexoTermDTO != null) {
                        if (!lexoTermDTO.check()) {
                            throw new IllegalArgumentException("ERR: some argments are null" + lexoTermDTO.toString());
                        }
                    } else {
                        throw new IllegalArgumentException("ERR: unable to map the JsonObject on to LexoTermDTO: " + jsonAnnotationDTO.asText());
                    }
                }
            } else {
                throw new IllegalArgumentException("ERR: jsonAnnotationDTO is null!");
            }
        } catch (JsonProcessingException ex) {
            log.error(ex);
        }
    }

    @Override
    public Class getAnnotationTypeClass() {
        return typeClass;
    }

    @Override
    public Class getAnnotationDataTypeClass() {
        return dataTypeClass;
    }

}
