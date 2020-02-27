package com.kakaopaysec.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ServiceException  extends RuntimeException{

    private final HttpStatus ERR_CODE;

    public ServiceException(String msg, HttpStatus errCode){
        super(msg);
        ERR_CODE = errCode;
    }

    public HttpStatus getERR_CODE(){
        return ERR_CODE;
    }

}
