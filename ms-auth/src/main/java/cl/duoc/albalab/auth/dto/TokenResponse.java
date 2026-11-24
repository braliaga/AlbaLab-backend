package cl.duoc.albalab.auth.dto;

public record TokenResponse(String token, String email, String nombre, long expiraEnSegundos) {
}
