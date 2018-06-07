/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import it.cnr.ilc.lc.omega.adt.annotation.Abbreviation;
import it.cnr.ilc.lc.omega.annotation.AbbreviationAnnotation;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.entity.TextLocus;
import it.cnr.ilc.lc.omega.rest.annotation.AbbreviationDTO;
import javax.ws.rs.ProcessingException;
import org.apache.logging.log4j.LogManager;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
@Register(classes = {RestfulAnnotationHandler.class}, name = "Abbreviation")
public class AbbreviationAnnotationParameterHandler implements RestfulAnnotationHandler {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(AbbreviationAnnotationParameterHandler.class);

    AbbreviationDTO dataAnnotationDTO;

    private final String type = "Abbreviation";
    private final Class<Abbreviation> typeClass = Abbreviation.class;
    private final Class<AbbreviationAnnotation> dataTypeClass = AbbreviationAnnotation.class;

    public AbbreviationAnnotationParameterHandler() {

    }

    @Override
    public void init(JsonNode jsonAnnotationDTO) {
        try {
            if (jsonAnnotationDTO != null) {
                ObjectMapper mapper = new ObjectMapper();
                dataAnnotationDTO = mapper.treeToValue(jsonAnnotationDTO, AbbreviationDTO.class);
                if (!(jsonAnnotationDTO instanceof NullNode)) {
                    if (dataAnnotationDTO != null) {
                        if (!dataAnnotationDTO.check()) {
                            throw new IllegalArgumentException("ERR: some argments are null" + dataAnnotationDTO.toString());
                        }
                    } else {
                        throw new IllegalArgumentException("ERR: unable to map the JsonObject on to AbbreviationDTO: " + jsonAnnotationDTO.asText());
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
    public String getType() {
        return type;
    }

    @Override
    public Object[] getBuildAnnotationParameter() {
        if (dataAnnotationDTO != null) { //l'ordine segue l'ordine dei parametri di Annotation.of
            return new Object[]{dataAnnotationDTO.expansion, dataAnnotationDTO.fragment};
        }
        return new Object[]{};
    }

    @Override
    public ADTAnnotation populateAnnotation(ADTAnnotation ann) {

        try {
            Abbreviation abb = (Abbreviation) ann;
            log.error("dataAnnotationDTO = " + dataAnnotationDTO.toString());
            TextLocus locus = Abbreviation.createTextLocus(Text.load(dataAnnotationDTO.textUri).getSource(), dataAnnotationDTO.start, dataAnnotationDTO.end);
            abb.addLocus(locus);
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
        }
        return ann;
    }

    @Override
    public ADTAnnotation saveAnnotation(ADTAnnotation ann) {

        Abbreviation abb = (Abbreviation) ann;
        try {
            log.info("Saving Abbreviation");
            abb.save();
            log.info("Abbreviation saved");
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
            throw new ProcessingException("Error saving the annotation ", ex.getCause());
        }
        return ann;
    }

    @Override
    public Class<Abbreviation> getAnnotationTypeClass() {
        return typeClass;
    }

    @Override
    public Class<AbbreviationAnnotation> getAnnotationDataTypeClass() {
        return dataTypeClass;
    }

}
