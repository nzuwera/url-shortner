package com.nzuwera.assignment.urlshortner.exception;

import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
@EnableWebMvc
@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Error handling RuntimeException.class, NullPointerException.class, SQLException.class, JAXBException.class
     *
     * @param ex RuntimeException, NullPointerException, SQLException, JAXBException
     * @return ResponseEntity<ResponseObject>
     */
    @ExceptionHandler({RuntimeException.class, NullPointerException.class, SQLException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject<?>> handleAllException(Exception ex) {
        log.error("handleAllException by class {} : {}", this.getClass().getSimpleName(), ex.getMessage());
        ResponseObject responseObject = ResponseObject.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .status(false)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Error handling NotFoundException
     *
     * @param ex NotFoundException
     * @return ResponseEntity<ResponseObject>
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject<?>> customHandleNotFoundException(NotFoundException ex) {
        ResponseObject responseObject = ResponseObject.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .status(false)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
    }

    /**
     * Error handling for Duplicates
     *
     * @param ex AlreadyExistsException
     * @return ResponseEntity<ResponseObject>
     */
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseObject> customHandleAlreadyExistsException(AlreadyExistsException ex) {
        ResponseObject responseObject = ResponseObject.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .status(false)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.CONFLICT);
    }


    /**
     * Error Handling for @Valid annotation
     *
     * @param ex      MethodArgumentNotValidException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return ResponseEntity<Object>
     */
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        ResponseObject responseObject = ResponseObject.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .status(false)
                .build();
        return new ResponseEntity<>(responseObject, headers, status);
    }

    /**
     * Handling unknown request mapping
     *
     * @param ex      NoHandlerFoundException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseObject responseObject = ResponseObject.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .status(false)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }
}
