package be.howest.ti.mars.logic.util;

public class MarsException extends RuntimeException {

    public MarsException() {
    }

    public MarsException(String msg) {
        super(msg);
    }

    public MarsException(String msg, Throwable t) {
        super(msg, t);
    }
}
