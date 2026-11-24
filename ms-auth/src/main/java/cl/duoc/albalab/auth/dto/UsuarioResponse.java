package cl.duoc.albalab.auth.dto;

import cl.duoc.albalab.auth.model.Usuario;

public record UsuarioResponse(Long id, String email, String nombre, String rol) {
    public static UsuarioResponse fromModel(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getEmail(), u.getNombre(), u.getRol());
    }
}
