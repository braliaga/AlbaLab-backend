package cl.duoc.albalab.auth.controller;

import cl.duoc.albalab.auth.exception.ApiErrorResponse;
import cl.duoc.albalab.auth.service.CredencialesInvalidasException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthErrorHandler {
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiErrorResponse> credencialesInvalidas(CredencialesInvalidasException e, HttpServletRequest req) {
        HttpStatus s = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(s).body(new ApiErrorResponse(LocalDateTime.now(), s.value(), s.getReasonPhrase(),
                e.getMessage(), req.getRequestURI(), null));
    }
}
