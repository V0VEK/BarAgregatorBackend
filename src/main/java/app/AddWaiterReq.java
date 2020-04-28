package app;

public class AddWaiterReq {
    String waiter_name;
    String email;
    String session_token;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getWaiter_name() {
        return waiter_name;
    }

    public void setWaiter_name(String waiter_name) {
        this.waiter_name = waiter_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
