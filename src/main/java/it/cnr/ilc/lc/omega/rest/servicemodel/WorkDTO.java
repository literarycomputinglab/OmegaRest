/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import it.cnr.ilc.lc.omega.adt.annotation.dto.Authors;
import it.cnr.ilc.lc.omega.adt.annotation.dto.PubblicationDate;
import it.cnr.ilc.lc.omega.adt.annotation.dto.Title;
import java.util.List;

/**
 *
 * @author simone
 */
public class WorkDTO {

    public String textUri;
    public Authors authors;
    public PubblicationDate pd;
    public Title title;
    public String workUri;
}
