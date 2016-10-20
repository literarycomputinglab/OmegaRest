/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import it.cnr.ilc.lc.omega.entity.Annotation;
import it.cnr.ilc.lc.omega.entity.Content;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simone
 */
public class AnnotationUri {

    String uri;

    private AnnotationUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    
    public static <T extends Content> List<AnnotationUri> toAnnotationUri(List<Annotation<T,?>> loa) {

        List<AnnotationUri> lotu = new ArrayList<>();

        for (Annotation ann : loa) {
            lotu.add(new AnnotationUri(ann.getUri()));
        }

        return lotu;
    }
}
