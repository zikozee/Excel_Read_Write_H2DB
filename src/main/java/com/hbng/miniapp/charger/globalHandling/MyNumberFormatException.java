package com.hbng.miniapp.charger.globalHandling;

public class MyNumberFormatException extends RuntimeException {
    public MyNumberFormatException(String message) {
        super(message);
    }
    public MyNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyNumberFormatException(Throwable cause) {
        super(cause);
    }
}
