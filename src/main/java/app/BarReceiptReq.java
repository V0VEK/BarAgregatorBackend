package app;

public class BarReceiptReq {
    Integer bar_id;
    Integer bar_receipt_id;
    String session_token;

    public Integer getBar_id() {
        return bar_id;
    }

    public void setBar_id(Integer bar_id) {
        this.bar_id = bar_id;
    }

    public Integer getBar_receipt_id() {
        return bar_receipt_id;
    }

    public void setBar_receipt_id(Integer bar_receipt_id) {
        this.bar_receipt_id = bar_receipt_id;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }
}
