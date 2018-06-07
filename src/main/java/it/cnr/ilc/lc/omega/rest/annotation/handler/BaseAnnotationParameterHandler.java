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
import it.cnr.ilc.lc.omega.adt.annotation.BaseAnnotationText;
import it.cnr.ilc.lc.omega.annotation.BaseAnnotation;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.rest.annotation.BaseAnnotationTextDTO;
import javax.ws.rs.ProcessingException;
import org.apache.logging.log4j.LogManager;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
@Register(classes = {RestfulAnnotationHandler.class}, name = "BaseAnnotationText")
public class BaseAnnotationParameterHandler implements RestfulAnnotationHandler {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BaseAnnotationParameterHandler.class);

    BaseAnnotationTextDTO dataAnnotationDTO;

    private final String type = "BaseAnnotationText";
    private final Class<BaseAnnotationText> typeClass = BaseAnnotationText.class;
    private final Class<BaseAnnotation> dataTypeClass = BaseAnnotation.class;

    public BaseAnnotationParameterHandler() {

    }

    @Override
    public void init(JsonNode jsonAnnotationDTO) {
        try {
            if (jsonAnnotationDTO != null) {
                ObjectMapper mapper = new ObjectMapper();
                dataAnnotationDTO = mapper.treeToValue(jsonAnnotationDTO, BaseAnnotationTextDTO.class);
                if (!(jsonAnnotationDTO instanceof NullNode)) {
                    if (dataAnnotationDTO != null) {
                        if (!dataAnnotationDTO.check()) {
                            throw new IllegalArgumentException("ERR: some argments are null" + dataAnnotationDTO.toString());
                        }
                    } else {
                        throw new IllegalArgumentException("ERR: unable to map the JsonObject on to BaseAnnotationDTO: " + jsonAnnotationDTO.asText());
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
        if (dataAnnotationDTO != null) {
            return new Object[]{dataAnnotationDTO.text};
        }
        return new Object[]{};
    }

    @Override
    public ADTAnnotation populateAnnotation(ADTAnnotation ann) {

        try {
            BaseAnnotationText bat = (BaseAnnotationText) ann;
            bat.addLocus(Text.load(dataAnnotationDTO.textUri), dataAnnotationDTO.start, dataAnnotationDTO.end);
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
        }
        return ann;
    }

    @Override
    public ADTAnnotation saveAnnotation(ADTAnnotation ann) {

        BaseAnnotationText bat = (BaseAnnotationText) ann;
        try {
            log.info("Saving BaseAnnotationText");
            bat.save();
            log.info("BaseAnnotationText saved");
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
            throw new ProcessingException("Error saving the annotation ", ex.getCause());
        }
        return ann;
    }

    @Override
    public Class<BaseAnnotationText> getAnnotationTypeClass() {
        return typeClass;
    }

    @Override
    public Class<BaseAnnotation> getAnnotationDataTypeClass() {
        return dataTypeClass;
    }

}
