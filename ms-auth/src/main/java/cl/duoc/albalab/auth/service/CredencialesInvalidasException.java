package cl.duoc.albalab.auth.service;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        // Mismo mensaje para correo inexistente y contraseña errónea: no revela qué cuentas existen.
        super("Correo o contraseña incorrectos");
    }
}
