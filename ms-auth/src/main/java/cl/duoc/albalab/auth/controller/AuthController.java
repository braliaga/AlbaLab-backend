package cl.duoc.albalab.auth.controller;

import cl.duoc.albalab.auth.dto.LoginRequest;
import cl.duoc.albalab.auth.dto.RegistroRequest;
import cl.duoc.albalab.auth.dto.TokenResponse;
import cl.duoc.albalab.auth.dto.UsuarioResponse;
import cl.duoc.albalab.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistroRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromModel(authService.registrar(req)));
    }
}
