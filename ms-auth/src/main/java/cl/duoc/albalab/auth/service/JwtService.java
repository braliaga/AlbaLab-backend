package cl.duoc.albalab.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey clave;
    private final long duracionSegundos;

    public JwtService(@Value("${jwt.secret}") String secreto,
                      @Value("${jwt.duracion-segundos:3600}") long duracionSegundos) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.duracionSegundos = duracionSegundos;
    }

    // HS256 explícito: el gateway valida con ese mismo algoritmo (jjwt elegiría HS512 si la clave fuera larga).
    public String generar(String email, String rol) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + duracionSegundos * 1000))
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }

    public String emailDe(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public long getDuracionSegundos() {
        return duracionSegundos;
    }
}
