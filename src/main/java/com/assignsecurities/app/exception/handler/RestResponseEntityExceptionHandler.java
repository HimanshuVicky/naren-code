package com.assignsecurities.app.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.assignsecurities.app.exception.NotAuthorisedException;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.bean.ExceptionDetail;
import com.assignsecurities.bean.ValidationError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	
	@ExceptionHandler({ NotAuthorisedException.class })
	public ResponseEntity<Object> handleNotAuthorisedException(Exception e, WebRequest request) {
		log.error(e.getMessage(), e);
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		jsonResponse.put("errors",
				ExceptionDetail.builder().exceptionType(e.getClass().getSimpleName()).message(e.getMessage())
						.validationErrorList(((ValidationException) e).getErrorList().stream().map(ValidationError::map)
								.collect(Collectors.toList()))
						.build());
		return new ResponseEntity<Object>(jsonResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}
	

	@ExceptionHandler({ ValidationException.class })
	public ResponseEntity<Object> handleValidationException(Exception e, WebRequest request) {
		log.error(e.getMessage(), e);
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		jsonResponse.put("errors",
				ExceptionDetail.builder().exceptionType(e.getClass().getSimpleName()).message(e.getMessage())
						.validationErrorList(((ValidationException) e).getErrorList().stream().map(ValidationError::map)
								.collect(Collectors.toList()))
						.build());
		return new ResponseEntity<Object>(jsonResponse, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleGenericExceptionn(Exception exception, WebRequest request) {
		log.error(exception.getMessage(), exception);
		Map<String, Object> jsonResponse = new HashMap<String, Object>();

        if(Objects.nonNull(exception.getCause()) && Objects.nonNull(exception.getCause().getMessage())){
        	jsonResponse.put("errors",ExceptionDetail.builder()
                            .exceptionType(ServiceException.class.getSimpleName())
                            .message("APHIS is not available. Please contact Support.")
                            .build());
        }

        if (exception.getMessage().contains("does not have permissions to execute operation")) {
        	jsonResponse.put("errors",ExceptionDetail.builder()
                            .exceptionType(ServiceException.class.getSimpleName())
                            .message("User does not have permissions to execute operation 'Claim'.")
                            .build());

        } else {
        	jsonResponse.put("errors",ExceptionDetail.builder()
                            .exceptionType(ServiceException.class.getSimpleName())
                            .message("There has been an issue processing your request. Please contact Support.")
                            .build());
        }
        return new ResponseEntity<Object>(jsonResponse, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
