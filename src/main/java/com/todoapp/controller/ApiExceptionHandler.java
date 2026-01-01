package com.todoapp.controller;

import com.todoapp.service.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<Void> onWrongPassword() {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // password가 틀렸으면 아무 정보도 주지 말기
  }
}
