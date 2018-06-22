/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import java.lang.reflect.Field;
import java.net.URI;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author simone
 */
public class LexoTermDTO implements RestfulDTO {

        private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(LexoTermDTO.class);

//    public String expansion;
    public URI textUri;
    public URI senseUri;
    public Integer start;
    public Integer end;
    public String fragment;
    public String leftTermContext;
    public String rightTermContext;
    public String note;

    @Override
    public boolean check() {
        boolean resp = true;
        if ((textUri == null) || (senseUri == null) || (start == null) || (end == null) || (fragment == null)) {
            resp = false;
        }
        return resp;
    }

    @Override
    public String toString() {
        Field[] fields = this.getClass().getFields();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            try {
                sb.append(fields[i].get(this));
                if (i < fields.length - 1) {
                    sb.append(" ");
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                log.error(ex);
            }

        }
        return sb.toString();

    }

}
