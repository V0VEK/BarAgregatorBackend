package app;

public class DeleteWaiterReq {
    String session_token;
    Integer waiter_id;

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public Integer getWaiter_id() {
        return waiter_id;
    }

    public void setWaiter_id(Integer waiter_id) {
        this.waiter_id = waiter_id;
    }
}
