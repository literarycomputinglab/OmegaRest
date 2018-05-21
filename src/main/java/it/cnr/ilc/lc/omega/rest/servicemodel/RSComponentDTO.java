/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCDescription;
import it.cnr.ilc.lc.omega.resourcesystem.dto.RSCName;
import java.net.URI;

/**
 *
 * @author simone
 */
public class RSComponentDTO {

    public URI uri;
    public RSCName name;
    public RSCDescription description;
    public URI uriParent;
    public URI catalogItemUri;

}
