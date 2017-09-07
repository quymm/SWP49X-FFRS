package utils;


public class Respond {
    private String status;
    private String message;
    private Object data;


    public Respond setData(Object data) {
        this.data = data;
        return this;
    }

    public Respond setMessage(String message) {
        this.message = message;
        return this;
    }

    public Respond setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Respond() {
    }

    public Respond(String status, Object data) {
        this.status = status;
        this.data = data;
    }
}
