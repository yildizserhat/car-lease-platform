package com.yildiz.serhat.carleaseplatform.exception;

import com.yildiz.serhat.carleaseplatform.controller.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_LOG_FORMAT_STR = "Api Exception Occurred, exception:%s, errorCode:%s, errorMsg:%s";


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleInternalServerErrors(RuntimeException exception) {
        ApiErrorType errorType = ApiErrorType.INTERNAL_SERVER_ERROR;

        log.error(String.format(ERROR_LOG_FORMAT_STR,
                exception.getClass().getName(), errorType.getErrorCode(), errorType.getErrorMessage()), exception);

        return createErrorResponse(errorType.getHttpStatus(), errorType.getErrorCode(), errorType.getErrorMessage());
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(CarNotFoundException exception) {
        ApiErrorType errorType = ApiErrorType.CAR_NOT_FOUND_EXCEPTION;

        log.error(String.format(ERROR_LOG_FORMAT_STR,
                exception.getClass().getName(), errorType.getErrorCode(), errorType.getErrorMessage()), exception);

        return createErrorResponse(errorType.getHttpStatus(), errorType.getErrorCode(), errorType.getErrorMessage());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(CustomerNotFoundException exception) {
        ApiErrorType errorType = ApiErrorType.CUSTOMER_NOT_FOUND_EXCEPTION;

        log.error(String.format(ERROR_LOG_FORMAT_STR,
                exception.getClass().getName(), errorType.getErrorCode(), errorType.getErrorMessage()), exception);

        return createErrorResponse(errorType.getHttpStatus(), errorType.getErrorCode(), errorType.getErrorMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse> genericMethodNotAllowedException(HttpRequestMethodNotSupportedException exception) {
        ApiErrorType errorType = ApiErrorType.INVALID_REQUEST_ERROR;

        log.error(String.format(ERROR_LOG_FORMAT_STR,
                exception.getClass().getName(), errorType.getErrorCode(), errorType.getErrorMessage()), exception);

        return createErrorResponse(errorType.getHttpStatus(), errorType.getErrorCode(), errorType.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiErrorType errorType = ApiErrorType.FIELD_VALIDATION_ERROR;

        List<ApiResponse.FieldErrorData> validationErrorList = new ArrayList<>();
        BindingResult bindingResult = e.getBindingResult();
        for (ObjectError objectError : bindingResult.getGlobalErrors()) {
            validationErrorList.add(new ApiResponse.FieldErrorData("request", objectError.getDefaultMessage()));
        }
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationErrorList.add(new ApiResponse.FieldErrorData(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return createErrorResponse(errorType.getHttpStatus(), errorType.getErrorCode(), errorType.getErrorMessage(), validationErrorList);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingMethodArgument(MissingServletRequestParameterException e) {
        ApiErrorType errorType = ApiErrorType.FIELD_MISSING_ERROR;

        ApiResponse.FieldErrorData fieldErrorData = new ApiResponse.FieldErrorData(e.getParameterName(), e.getLocalizedMessage());

        return createErrorResponse(errorType.getHttpStatus(), errorType.getErrorCode(), errorType.getErrorMessage(), fieldErrorData);
    }


    private ResponseEntity<ApiResponse> createErrorResponse(HttpStatus httpStatus, int errorCode, String errorMessage, Object body) {
        ResponseEntity<ApiResponse> errorResponse = createErrorResponse(httpStatus, errorCode, errorMessage);
        errorResponse.getBody().setOperationResultData(body);

        return errorResponse;
    }

    private ResponseEntity<ApiResponse> createErrorResponse(HttpStatus httpStatus, int errorCode, String errorMessage) {
        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.builder()
                        .operationResult(ApiResponse.OperationResult.builder()
                                .returnCode(Integer.toString(errorCode))
                                .returnMessage(errorMessage)
                                .build())
                        .build());
    }
}
