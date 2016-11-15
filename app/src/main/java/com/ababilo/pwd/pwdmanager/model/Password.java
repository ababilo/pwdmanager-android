package com.ababilo.pwd.pwdmanager.model;

import java.io.Serializable;

/**
 * Created by ababilo on 14.11.16.
 */

public class Password implements Serializable {

    private short id;
    private String title;
    private byte[] part;

    public Password(short id, String title, byte[] part) {
        this.id = id;
        this.title = title;
        this.part = part;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPart() {
        return part;
    }

    public void setPart(byte[] part) {
        this.part = part;
    }
}
