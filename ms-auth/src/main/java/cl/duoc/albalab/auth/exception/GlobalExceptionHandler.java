package cl.duoc.albalab.auth.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> noEncontrado(ResourceNotFoundException e, HttpServletRequest req) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage(), req, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> solicitudInvalida(BadRequestException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage(), req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validacion(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> campos.putIfAbsent(f.getField(), f.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "Hay datos inválidos en la solicitud", req, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no tiene un formato válido", req, null);
    }

    private ResponseEntity<ApiErrorResponse> respuesta(HttpStatus status, String mensaje, HttpServletRequest req,
                                                       Map<String, String> campos) {
        ApiErrorResponse cuerpo = new ApiErrorResponse(LocalDateTime.now(), status.value(),
                status.getReasonPhrase(), mensaje, req.getRequestURI(), campos);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
