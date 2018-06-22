/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import java.net.URI;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author simone
 */
public class LocusDTO implements RestfulDTO {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(LocusDTO.class);

    public URI textUri;
    public Integer start;
    public Integer end;
    public String fragment;

    @Override
    public boolean check() {
        return textUri != null;
    }

    @Override
    public String toString() {
        return String.format("textUri=(%s), start=(%d), end=(%d), fragment=(%s)", 
                textUri, start, end, (fragment!=null?(fragment.length()<10?fragment:fragment.substring(0, 10)+"..."):"no fragment"));
    }

    
}
