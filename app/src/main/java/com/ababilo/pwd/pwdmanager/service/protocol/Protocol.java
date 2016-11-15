package com.ababilo.pwd.pwdmanager.service.protocol;

import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.util.AESUtil;
import com.ababilo.pwd.pwdmanager.util.ArrayUtils;
import com.ababilo.pwd.pwdmanager.util.HashUtils;
import com.ababilo.pwd.pwdmanager.util.ShortUtil;

import org.apache.commons.io.Charsets;

/**
 * Created by ababilo on 15.11.16.
 */

public class Protocol {

    private final ProtocolKeysProvider keysProvider;

    public Protocol(ProtocolKeysProvider keysProvider) {
        this.keysProvider = keysProvider;
    }

    public byte[] sendPassword(Password password) {
        byte[] data = ArrayUtils.concatArrays(ShortUtil.getShortBytes(password.getId()), password.getPart());
        byte[] payload = keysProvider.appendNextKeys(data);

        byte[] encrypted = AESUtil.encrypt(payload, keysProvider.getCurrentBTKey());
        byte[] signature = HashUtils.hmacSha256(encrypted, keysProvider.getCurrentHBTKEy());

        return ArrayUtils.concatArrays(encrypted, signature); // iv + encrypted + signature
    }

    public byte[] addPassword(short id, String title, String password, byte[] randomPart) {
        byte[] part2 = new byte[128];
        byte[] passwordBytes = password.getBytes(Charsets.US_ASCII);
        for (int i = 0; i < passwordBytes.length; i++) {
            part2[i] = (byte) (passwordBytes[i] ^ randomPart[i]);
        }
        return sendPassword(new Password(id, title, part2));
    }
}
