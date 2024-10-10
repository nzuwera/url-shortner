package com.nzuwera.assignment.urlshortner.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject<T> {
    private boolean status;
    private int code;
    private String message;
    private T data;

}