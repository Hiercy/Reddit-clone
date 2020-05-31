package com.mike.reddit.exceptions;

public class SpringRedditException extends RuntimeException {

    public SpringRedditException(Exception exception, String exceptionMessage) {
        super(exceptionMessage, exception);
    }

    public SpringRedditException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
