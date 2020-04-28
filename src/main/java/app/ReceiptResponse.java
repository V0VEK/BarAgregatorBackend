package app;

import java.util.List;

public class ReceiptResponse {
    List<String> products_names;
    List<Integer> products_id;
    Integer unique_receipt_id;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUnique_receipt_id() {
        return unique_receipt_id;
    }

    public void setUnique_receipt_id(Integer unique_receipt_id) {
        this.unique_receipt_id = unique_receipt_id;
    }

    public List<String> getProducts_names() {
        return products_names;
    }

    public void setProducts_names(List<String> products_names) {
        this.products_names = products_names;
    }


    public List<Integer> getProducts_id() {
        return products_id;
    }

    public void setProducts_id(List<Integer> products_id) {
        this.products_id = products_id;
    }
}
