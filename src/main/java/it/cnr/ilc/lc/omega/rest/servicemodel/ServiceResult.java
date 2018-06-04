/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.rest.servicemodel;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author simone
 */
public class ServiceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codeError = "0";
    private String body = "no errors";
    private List<?> result = null;

    public ServiceResult() {
    }

    public ServiceResult(String _codeError, String _bodyError) {

        this(_codeError, _bodyError, null);
    }

    public ServiceResult(String _codeError, String _bodyError, List<?> _result) {

        codeError = _codeError;
        body = _bodyError;
        result = _result;

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

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

}
