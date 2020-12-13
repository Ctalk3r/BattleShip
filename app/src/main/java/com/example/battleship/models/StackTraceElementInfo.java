package com.example.battleship.models;

public class StackTraceElementInfo {
    private String className;
    private String filename;
    private int lineNumber;
    private String methodName;

    public StackTraceElementInfo(String className, String filename, int lineNumber, String methodName) {
        this.className = className;
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
