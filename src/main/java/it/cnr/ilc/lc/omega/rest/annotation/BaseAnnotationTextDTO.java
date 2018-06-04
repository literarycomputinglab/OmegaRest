/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation;

import java.net.URI;

/**
 *
 * @author simone
 */
public class BaseAnnotationTextDTO implements RestfulDTO {

    public String text;
    public URI textUri;
    public Integer start;
    public Integer end;

    @Override
    public boolean check() {

        boolean resp = true;
        if ((textUri == null) || (start == null) || (end == null) || (text == null)) {
            resp = false;
        }
        return resp;
    }
}
