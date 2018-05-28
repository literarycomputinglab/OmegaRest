/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import it.cnr.ilc.lc.omega.adt.annotation.BaseAnnotationText;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Couple;
import it.cnr.ilc.lc.omega.core.ManagerAction;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;
import it.cnr.ilc.lc.omega.core.datatype.Text;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author simone
 */
public class ParameterHandler {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ParameterHandler.class);

    Object t;
    Couple<Class<?>, Class<?>> c;
    String annotatinType;

    public ParameterHandler(Object obj, Couple<Class<?>, Class<?>> couple) {

        t = obj;
        c = couple;
        annotatinType = c.getFirst().getName();

    }

    public Object[] getBuildAnnotationParameter() {
        if (c.getSecond().isInstance(t)) {
            if (c.getSecond().getSimpleName().equals("BaseAnnotationTextDTO")) {
                BaseAnnotationTextDTO batdto = (BaseAnnotationTextDTO) t;
                return new Object[]{batdto.text};
            }
        }
        return null;
    }

    public ADTAnnotation populateAnnotation(ADTAnnotation ann) {

        if (c.getFirst().isInstance(ann)) {
            if (c.getFirst().getSimpleName().equals("BaseAnnotationText")) {
                BaseAnnotationText bat = (BaseAnnotationText) ann;
                BaseAnnotationTextDTO batdto = (BaseAnnotationTextDTO) t;
                try {
                    bat.addLocus(Text.load(batdto.textUri), batdto.start, batdto.end);
                } catch (ManagerAction.ActionException ex) {
                    log.error(ex);
                }
            }
        }
        return ann;

    }
}
