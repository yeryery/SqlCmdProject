package ua.com.juja.yeryery.controller.commands.Utility;

public class ConnectException extends RuntimeException {

    private String message;

    public ConnectException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format("Error! %s\nTry again", message);
    }
}