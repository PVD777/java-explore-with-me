package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(ObjectNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Data not found exception")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(BadRequestException e) {
        log.debug("Получен статус 400 bad request {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Bad request")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(MissingServletRequestParameterException e) {
        log.debug("Получен статус 400 bad request {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Missed request parameters")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 bad request {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Method argument not valid")
                .message(e.getMessage())
                .build();
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(ValidationException e) {
        log.debug("Получен статус 409 conflict {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.CONFLICT)
                .reason("Validation exception")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(ConstraintViolationException e) {
        log.debug("Получен статус 409 conflict {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.CONFLICT)
                .reason("Validation exception")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(Throwable e) {
        log.debug("Получен статус 500 server error {}", e.getMessage(), e);
        return ApiError
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("ALARM!ALARM!")
                .message(e.getMessage())
                .build();
    }
}
