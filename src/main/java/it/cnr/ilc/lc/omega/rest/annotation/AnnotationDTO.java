/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;

/**
 *
 * @author simone
 */
public class AnnotationDTO {
    
    public URI uri;
    public JsonNode annotationData;
    
}
