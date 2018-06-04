/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author simone
 */
public class AbbreviationDTO implements RestfulDTO {

    private static Logger log = LogManager.getLogger(AbbreviationDTO.class);
    
    public String expansion;
    public URI textUri;
    public Integer start;
    public Integer end;
    public String fragment;

    @Override
    public boolean check() {

        boolean resp = true;
        if ((textUri == null) ||  (start == null) ||  (end == null) ||  (fragment == null)){
            resp = false;
        }         
        return resp;
    }

    @Override
    public String toString() {
        return "(" +expansion + ") (" + textUri + ") (" + start + ") (" + end + ") (" + fragment +")";
    }
    
    

}
