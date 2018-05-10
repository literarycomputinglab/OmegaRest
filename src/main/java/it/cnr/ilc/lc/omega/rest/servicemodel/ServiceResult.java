/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import java.io.Serializable;

/**
 *
 * @author simone
 */
public class ServiceResult implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String codeError = "0";
    private String body = "no errors";

    public ServiceResult() {
    }

    public ServiceResult(String _codeError, String _bodyError) {

        codeError = _codeError;
        body = _bodyError;
    }

    public String getCodeError() {
        return codeError;
    }

    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
