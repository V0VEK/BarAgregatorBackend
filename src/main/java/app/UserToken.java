package app;

import java.util.Random;

public class UserToken {
    String user_token;
    String status;

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    private String FromBytesToHexString(byte[] bytesArray) {
        String buff = new String();
        for (byte b : bytesArray)
            buff += String.format("%02X", b);

        return buff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
