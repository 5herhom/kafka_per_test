package cn.com.sherhom.reno.common.exception;

public class RenoException extends RuntimeException {
    public RenoException() {
    }

    public RenoException(String message) {
        super(message);
    }

    public RenoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenoException(Throwable cause) {
        super(cause);
    }

    public RenoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
