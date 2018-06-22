/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.annotation.handler;

import it.cnr.ilc.lc.omega.adt.annotation.DublinCore;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sirius.kernel.di.std.Register;

/**
 *
 * @author simone
 */
//@Register(classes = {RestfulAnnotationHandler.class}, name = "DublinCore")
public class DublinCoreAnnotationParameterHandler {
    
    private Class<DublinCore> bobbe = DublinCore.class;

    public DublinCoreAnnotationParameterHandler() {
        try {
            bobbe.getMethod("", String.class).invoke(null, "");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(DublinCoreAnnotationParameterHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(DublinCoreAnnotationParameterHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DublinCoreAnnotationParameterHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DublinCoreAnnotationParameterHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(DublinCoreAnnotationParameterHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
