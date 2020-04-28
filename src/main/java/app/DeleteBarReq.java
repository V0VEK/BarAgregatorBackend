package app;

public class DeleteBarReq {
    String session_token;
    Integer bar_id;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public Integer getBar_id() {
        return bar_id;
    }

    public void setBar_id(Integer bar_id) {
        this.bar_id = bar_id;
    }
}
