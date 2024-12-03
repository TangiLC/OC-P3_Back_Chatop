package com.chatop.exception;

/**
 * Exception spécifique pour les erreurs liées à la validation des tokens JWT.
 */
public class JwtValidationException extends RuntimeException {

    /**
     * Constructeur avec un message spécifique.
     *
     * @param message Le message décrivant l'erreur.
     */
    public JwtValidationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message et une cause.
     *
     * @param message Le message décrivant l'erreur.
     * @param cause   La cause de l'exception.
     */
    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
