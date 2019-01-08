package com.salesmanager.core.business.exception;

public class ServiceRuntimeException extends RuntimeException {

    public ServiceRuntimeException(Throwable throwable){
        super(throwable);
    }
}
