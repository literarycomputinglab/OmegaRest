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
import it.cnr.ilc.lc.omega.adt.annotation.Work;
import it.cnr.ilc.lc.omega.adt.annotation.dto.DTOValue;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Loci;
import it.cnr.ilc.lc.omega.annotation.structural.WorkAnnotation;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import it.cnr.ilc.lc.omega.entity.TextLocus;
import it.cnr.ilc.lc.omega.rest.annotation.LocusDTO;
import it.cnr.ilc.lc.omega.rest.annotation.WorkAnnotationDTO;
import java.util.logging.Level;
import javax.ws.rs.ProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
@Register(classes = {RestfulAnnotationHandler.class}, name = "Work")
public class WorkAnnotationParameterHandler implements RestfulAnnotationHandler {

    private static final Logger log = LogManager.getLogger(WorkAnnotationParameterHandler.class);

    WorkAnnotationDTO dataAnnotationDTO;

    private final String type = "Work";
    private final Class<Work> typeClass = Work.class;
    private final Class<WorkAnnotation> dataTypeClass = WorkAnnotation.class;

    public WorkAnnotationParameterHandler() {
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Class getAnnotationTypeClass() {
        return this.typeClass;
    }

    @Override
    public Class getAnnotationDataTypeClass() {
        return this.dataTypeClass;
    }

    @Override
    public void init(JsonNode jsonAnnotationDTO) {
        try {
            if (jsonAnnotationDTO != null) {
                ObjectMapper mapper = new ObjectMapper();
                dataAnnotationDTO = mapper.treeToValue(jsonAnnotationDTO, WorkAnnotationDTO.class);
                if (!(jsonAnnotationDTO instanceof NullNode)) {
                    if (dataAnnotationDTO != null) {
                        if (!dataAnnotationDTO.check()) {
                            throw new IllegalArgumentException("ERR: some argments are null" + dataAnnotationDTO.toString());
                        }
                    } else {
                        throw new IllegalArgumentException("ERR: unable to map the JsonObject on to WorkAnnotationDTO: " + jsonAnnotationDTO.asText());
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
    public Object[] getBuildAnnotationParameter() {
        if (dataAnnotationDTO != null) {
            log.info("Work Annotation DTO: " + dataAnnotationDTO.toString());
            return new Object[]{dataAnnotationDTO.authors,
                dataAnnotationDTO.pubblicationDate,
                dataAnnotationDTO.title};
        } else {
            log.warn("Work Annotation DTO is null!");
        }
        return new Object[]{};
    }

    @Override
    public ADTAnnotation populateAnnotation(ADTAnnotation ann) {
        Work work = (Work) ann;

        try {
            Loci loci = DTOValue.instantiate(Loci.class);
            for (LocusDTO locusDto : dataAnnotationDTO.loci) {
                TextLocus locus;
                log.info("locusDTO: " + locusDto.toString());

                if (locusDto.start != null && locusDto.end != null) {
                    locus = Work.createTextLocus(Text.load(locusDto.textUri).getSource(), locusDto.start, locusDto.end);
                } else {
                    locus = Work.createTextLocus(Text.load(locusDto.textUri).getSource());
                }
                loci.withValue(locus);
                log.info("Locus uri=(" + locus.getUri() + ") on Text uri=(" + locus.getSourceUri() + ")");
            }
            work.addLoci(loci);
        } catch (ManagerAction.ActionException | InstantiationException | IllegalAccessException ex) {
            log.error("Error adding locus to work with uri=(" + work.getURI() + ")", ex.getLocalizedMessage());
            throw new IllegalStateException("Error adding locus to work with uri=(" + work.getURI() + ")", ex);
        }

        return ann;
    }

    @Override
    public ADTAnnotation saveAnnotation(ADTAnnotation ann) {

        Work work = (Work) ann;
        try {
            log.info("Saving Work Annotation");
            work.save();
            log.info("Work Annotation saved");
        } catch (ManagerAction.ActionException ex) {
            log.error(ex);
            throw new ProcessingException("Error saving the annotation ", ex.getCause());
        }
        return ann;
    }

}
