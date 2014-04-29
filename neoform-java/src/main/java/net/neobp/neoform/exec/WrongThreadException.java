package net.neobp.neoform.exec;

public class WrongThreadException extends Exception {

    public WrongThreadException(final String cause) {
        super(cause);
    }
}
