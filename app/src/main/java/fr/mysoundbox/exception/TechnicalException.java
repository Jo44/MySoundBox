package fr.mysoundbox.exception;

/**
 * Classe d'exception technique
 * <p>
 * Author: Jonathan B.
 * Created: 20/05/2018
 */

public class TechnicalException extends Exception {

    /**
     * Attributs
     */
    private final String message;

    /**
     * Constructeur
     *
     * @param message String
     */
    public TechnicalException(String message) {
        this.message = message;
    }

    /**
     * Getter / Setter
     */
    public String getMessage() {
        return message;
    }

}
