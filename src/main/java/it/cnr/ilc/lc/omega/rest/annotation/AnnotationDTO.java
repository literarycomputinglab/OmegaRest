/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;

/**
 *
 * @author simone
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationDTO implements RestfulDTO {

    @JsonProperty(required = true)
    public URI uri;
    public JsonNode annotationData;

    @Override
    public boolean check() {

        boolean ret = true;
        if (uri == null) {
            ret = false;
        } else if (annotationData == null) {
            ret = false;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "(" + uri + ") (" + annotationData + ")";
    }

}
