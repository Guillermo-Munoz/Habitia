package com.habitia.shared.domain.exception;

public class ResourceNoFoundException extends DomainException {
    public ResourceNoFoundException(String resource, String id){
        super(resource + " not found whit id: " + id);
    }
}
