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
public class AbbreviationDTO {
    
    public String expansion;
    public URI textUri;
    public int start;
    public int end;
    public String fragment;
    
}
