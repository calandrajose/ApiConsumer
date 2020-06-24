package com.phones.phones.controller;

import com.phones.phones.dto.ErrorDto;
import com.phones.phones.exception.call.CallDoesNotExistException;
import com.phones.phones.exception.city.CityAlreadyExistException;
import com.phones.phones.exception.city.CityDoesNotExistException;
import com.phones.phones.exception.invoice.InvoiceDoesNotExistException;
import com.phones.phones.exception.line.LineCannotMakeCallsException;
import com.phones.phones.exception.line.LineDoesNotExistException;
import com.phones.phones.exception.line.LineNumberAlreadyExistException;
import com.phones.phones.exception.line.LineNumberDoesNotExistException;
import com.phones.phones.exception.province.ProviceAlreadyExistException;
import com.phones.phones.exception.province.ProvinceDoesNotExistException;
import com.phones.phones.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.text.ParseException;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /* City exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CityAlreadyExistException.class)
    public ErrorDto handleCityAlreadyExistException(CityAlreadyExistException e) {
        return new ErrorDto(1, "City name already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CityDoesNotExistException.class)
    public ErrorDto handleCityNotExistException(CityDoesNotExistException e) {
        return new ErrorDto(2, "City id do not exists");
    }


    /* Line exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LineNumberAlreadyExistException.class)
    public ErrorDto handleLineNumberAlreadyExistException(LineNumberAlreadyExistException e) {
        return new ErrorDto(3, "Line number already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineDoesNotExistException.class)
    public ErrorDto handleLineNotExistException(LineDoesNotExistException e) {
        return new ErrorDto(4, "Line id do not exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineNumberDoesNotExistException.class)
    public ErrorDto handleLineNumberNotExistException(LineNumberDoesNotExistException e) {
        return new ErrorDto(4, "Line number do not exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineCannotMakeCallsException.class)
    public ErrorDto handleLineCannotMakeCallsException(LineCannotMakeCallsException e) {
        return new ErrorDto(4, "Line is disabled or suspended, cannot make calls");
    }


    /* Province exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProviceAlreadyExistException.class)
    public ErrorDto handleProvinceAlreadyExistException(ProviceAlreadyExistException e) {
        return new ErrorDto(5, "Province name already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProvinceDoesNotExistException.class)
    public ErrorDto handleProvinceNotExistException(ProvinceDoesNotExistException e) {
        return new ErrorDto(6, "Province id do not exists");
    }


    /* User exceptions */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ErrorDto handleUsernameAlreadyExistException(UsernameAlreadyExistException e) {
        return new ErrorDto(7, "Username already exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorDto handleUsernameAlreadyExistException(UserAlreadyExistException e) {
        return new ErrorDto(8, "User dni already exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyDisableException.class)
    public ErrorDto handleUserAlreadyDisableException(UserAlreadyDisableException e) {
        return new ErrorDto(9, "User is already delete");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserDoesNotExistException.class)
    public ErrorDto handleUserNotExistException(UserDoesNotExistException e) {
        return new ErrorDto(10, "User id do not exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParseException.class)
    public ErrorDto handleParseException() {
        return new ErrorDto(11, "Not valid dates");
    }


    /* User Login exceptions */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserInvalidLoginException.class)
    public ErrorDto handleLoginException(UserInvalidLoginException e) {
        return new ErrorDto(12, "Invalid login");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorDto handleValidationException(ValidationException e) {
        return new ErrorDto(13, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserSessionDoesNotExistException.class)
    public ErrorDto handleUserSessionNotExistException(UserSessionDoesNotExistException e) {
        return new ErrorDto(14, "Token has expired");
    }


    /* Call exceptions */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CallDoesNotExistException.class)
    public ErrorDto handleCallNotExistException(CallDoesNotExistException e) {
        return new ErrorDto(15, "Call id do not exists");
    }


    /* Invoice exceptions */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvoiceDoesNotExistException.class)
    public ErrorDto handleInvoiceNotExistException(InvoiceDoesNotExistException e) {
        return new ErrorDto(16, "Invoice id do not exists");
    }


}
