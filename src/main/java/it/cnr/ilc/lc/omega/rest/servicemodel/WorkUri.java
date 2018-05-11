/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import it.cnr.ilc.lc.omega.adt.annotation.Work;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simone
 */
public class WorkUri {

    private Work work;

    public WorkUri(Work w) {
        this.work = w;
    }

    public String getWorkUri() {

        return work.getURI().toASCIIString();
    }

    public static List<WorkUri> toWorkUri (List<Work> low) {
        
        List<WorkUri> lowu = new ArrayList<>();
        
        for (Work text : low) {
            lowu.add(new WorkUri(text));
        }
        
        return lowu;
    }

    
    
}
