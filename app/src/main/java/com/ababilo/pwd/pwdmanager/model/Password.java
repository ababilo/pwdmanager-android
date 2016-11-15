package com.ababilo.pwd.pwdmanager.model;

import java.io.Serializable;

/**
 * Created by ababilo on 14.11.16.
 */

public class Password implements Serializable {

    private short id;
    private String title;
    private byte[] part1;

    public Password(short id, String title, byte[] part1) {
        this.id = id;
        this.title = title;
        this.part1 = part1;
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

    public byte[] getPart1() {
        return part1;
    }

    public void setPart1(byte[] part1) {
        this.part1 = part1;
    }
}
