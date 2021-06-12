package com.nicktheblackbeard.server.serverfiles;


/**
 * @author nicktheblackbeard
 * 12/6/21
 */
public class ServerException extends Exception{
    int ID;
    String message;

    public ServerException(int ID, String message) {
        this.ID = ID;
        this.message = message;
    }

    public ServerException(String message, int ID, String message1) {
        super(message);
        this.ID = ID;
        this.message = message1;
    }

    public ServerException(String message, Throwable cause, int ID, String message1) {
        super(message, cause);
        this.ID = ID;
        this.message = message1;
    }

    public ServerException(Throwable cause, int ID, String message) {
        super(cause);
        this.ID = ID;
        this.message = message;
    }

    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int ID, String message1) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ID = ID;
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
