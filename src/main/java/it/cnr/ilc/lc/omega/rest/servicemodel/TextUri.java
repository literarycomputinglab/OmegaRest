/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import it.cnr.ilc.lc.omega.core.datatype.Text;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simone
 */
public class TextUri {

    private Text text;

    public TextUri(Text text) {
        this.text = text;
    }

    public String getTextUri() {

        return text.getSource().getUri();
    }

    public static List<TextUri> toTextUri (List<Text> lot) {
        
        List<TextUri> lotu = new ArrayList<>();
        
        for (Text text : lot) {
            lotu.add(new TextUri(text));
        }
        
        return lotu;
    }

    
    
}
