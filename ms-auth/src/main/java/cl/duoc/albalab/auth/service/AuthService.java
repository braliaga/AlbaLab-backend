package cl.duoc.albalab.auth.service;

import cl.duoc.albalab.auth.dto.LoginRequest;
import cl.duoc.albalab.auth.dto.RegistroRequest;
import cl.duoc.albalab.auth.dto.TokenResponse;
import cl.duoc.albalab.auth.exception.BadRequestException;
import cl.duoc.albalab.auth.model.Usuario;
import cl.duoc.albalab.auth.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public static final String ROL_GESTOR = "GESTOR";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponse login(LoginRequest req) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(req.email().trim())
                .orElseThrow(CredencialesInvalidasException::new);
        if (!passwordEncoder.matches(req.password(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }
        String token = jwtService.generar(usuario.getEmail(), usuario.getRol());
        return new TokenResponse(token, usuario.getEmail(), usuario.getNombre(), jwtService.getDuracionSegundos());
    }

    public Usuario registrar(RegistroRequest req) {
        String email = req.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Ya existe un usuario con ese correo");
        }
        Usuario nuevo = new Usuario(null, email, req.nombre().trim(), passwordEncoder.encode(req.password()), ROL_GESTOR);
        return usuarioRepository.save(nuevo);
    }
}
