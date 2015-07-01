package de.fh_muenster.chat;

import android.app.Application;

/**
 * Created by marius on 30.06.15.
 */
public class ChatApplication extends Application {
    private byte[] privkey_user;
    private String name;
    private String[] message;

    public byte[] getPrivkey_user() {
        return privkey_user;
    }

    public void setPrivkey_user(byte[] privkey_user) {
        this.privkey_user = privkey_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
