package co.edu.itm.clinicaldata.mapper;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;
    private int statusCode;
    private String message;
    private String debugMessage;

    ApiError(HttpStatus status, Throwable ex) {
        this.status = status;
        this.statusCode = status.value();
        this.message = ex.getMessage();
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.debugMessage = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
