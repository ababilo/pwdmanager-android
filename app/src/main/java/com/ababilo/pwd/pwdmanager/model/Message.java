package com.ababilo.pwd.pwdmanager.model;

import com.ababilo.pwd.pwdmanager.util.ArrayUtils;

/**
 * Created by ababilo on 14.11.16.
 */

public class Message {

    private Commands command;
    private byte[] data;

    public Message(Commands command, byte[] data) {
        this.command = command;
        this.data = data;
    }

    public Message(Commands command) {
        this.command = command;
    }

    public Commands getCommand() {
        return command;
    }

    public void setCommand(Commands command) {
        this.command = command;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static byte[] getBytes(Message message) {
        return ArrayUtils.concatArrays(new byte[] {message.command.getByte()}, message.data);
    }
}
