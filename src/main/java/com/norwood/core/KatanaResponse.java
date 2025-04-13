package com.norwood.core;

public class KatanaResponse {
    private Object value;

    private KatanaResponse(Object value) {
        this.value = value;
    }

    public static KatanaResponse success(Object responseValue) {
        return new KatanaResponse(responseValue);
    }

    public static KatanaResponse error(String string) {
        return new KatanaResponse(string);
    }

    public String value() {
        return String.valueOf(value);
    }
}

