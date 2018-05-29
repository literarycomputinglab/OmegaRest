/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation.handler;

import com.fasterxml.jackson.databind.JsonNode;
import it.cnr.ilc.lc.omega.core.datatype.ADTAnnotation;

/**
 *
 * @author simone
 */

public interface RestfulAnnotationHandler {

    public String getType();
    
    public Object[] getBuildAnnotationParameter();

    public ADTAnnotation populateAnnotation(ADTAnnotation ann);

    public ADTAnnotation saveAnnotation(ADTAnnotation ann);

    public void init(JsonNode annDTO);

    public Class getAnnotationTypeClass();
    
}
