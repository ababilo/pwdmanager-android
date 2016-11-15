package com.ababilo.pwd.pwdmanager.service.protocol;

import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.util.AESUtil;
import com.ababilo.pwd.pwdmanager.util.ArrayUtils;
import com.ababilo.pwd.pwdmanager.util.HashUtils;
import com.ababilo.pwd.pwdmanager.util.ShortUtil;

/**
 * Created by ababilo on 15.11.16.
 */

public class Protocol {

    private final ProtocolKeysProvider keysProvider;

    public Protocol(ProtocolKeysProvider keysProvider) {
        this.keysProvider = keysProvider;
    }

    public byte[] sendPassword(Password password) {
        byte[] data = ArrayUtils.concatArrays(ShortUtil.getShortBytes(password.getId()), password.getPart1());
        byte[] payload = keysProvider.appendNextKeys(data);

        byte[] encrypted = AESUtil.encrypt(payload, keysProvider.getCurrentBTKey());
        byte[] signature = HashUtils.hmacSha256(encrypted, keysProvider.getCurrentHBTKEy());

        return ArrayUtils.concatArrays(encrypted, signature); // iv + encrypted + signature
    }
}
