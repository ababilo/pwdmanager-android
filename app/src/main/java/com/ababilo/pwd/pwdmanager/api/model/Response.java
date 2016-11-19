package com.ababilo.pwd.pwdmanager.api.model;

/**
 * Created by ababilo on 18.11.16.
 */

public class Response<T> {

    private T content;

    public Response(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
