package com.example.battleship.models;

public class ErrorInfo {
    private String message;
    private String localizedMessage;
    private StackTraceElementInfo stackTraceElementInfo;

    public ErrorInfo(String message, String localizedMessage, StackTraceElementInfo stackTraceElementInfo) {
        this.message = message;
        this.localizedMessage = localizedMessage;
        this.stackTraceElementInfo = stackTraceElementInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    public StackTraceElementInfo getStackTraceElements() {
        return stackTraceElementInfo;
    }

    public void setStackTraceElements(StackTraceElementInfo stackTraceElementInfo) {
        this.stackTraceElementInfo = stackTraceElementInfo;
    }
}
