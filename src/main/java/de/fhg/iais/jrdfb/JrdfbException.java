package de.fhg.iais.jrdfb;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class JrdfbException extends Exception {
    public JrdfbException () {

    }

    public JrdfbException (String message) {
        super (message);
    }

    public JrdfbException (Throwable cause) {
        super (cause);
    }

    public JrdfbException (String message, Throwable cause) {
        super (message, cause);
    }
}
