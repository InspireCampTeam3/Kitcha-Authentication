package com.kitcha.authentication.exception;

import com.kitcha.authentication.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static java.util.Collections.singletonMap;

@ControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerExceptionHandler {

    // @Valid로 인한 검증 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(singletonMap("message", e.getMessage()));
    }

    // DuplicateException 처리 (signUpMember)
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(singletonMap("message", e.getMessage()));
    }

    // AuthenticationException 처리 (loginMember)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(singletonMap("message", "ID 또는 PW가 일치하지 않습니다."));
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(singletonMap("message", "서버 오류가 발생하였습니다."));
    }


}
