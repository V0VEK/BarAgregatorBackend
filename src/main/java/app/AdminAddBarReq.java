package app;

public class AdminAddBarReq {
    String session_token;
    String email;
    String bar_name;

    public String getBar_name() {
        return bar_name;
    }

    public void setBar_name(String bar_name) {
        this.bar_name = bar_name;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
