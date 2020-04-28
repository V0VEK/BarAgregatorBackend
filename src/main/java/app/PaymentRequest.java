package app;

import java.util.List;

public class PaymentRequest {
    List<Integer> products_id;
    Integer unique_receipt_id;
    String session_token;

    public List<Integer> getProducts_id() {
        return products_id;
    }

    public void setProducts_id(List<Integer> products_id) {
        this.products_id = products_id;
    }

    public Integer getUnique_receipt_id() {
        return unique_receipt_id;
    }

    public void setUnique_receipt_id(Integer unique_receipt_id) {
        this.unique_receipt_id = unique_receipt_id;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }
}
