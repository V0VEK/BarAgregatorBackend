package app;

import java.util.List;

public class AddReceiptReq {
    List<Integer> products_id;
    String session_token;
    Integer bar_receipt_id;


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


    public List<Integer> getProducts_id() {
        return products_id;
    }

    public void setProducts_id(List<Integer> products_id) {
        this.products_id = products_id;
    }
}
