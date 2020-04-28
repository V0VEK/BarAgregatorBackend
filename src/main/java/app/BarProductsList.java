package app;

import java.util.List;

public class BarProductsList {
    List<String> products_list;
    List<Integer> products_id;
    String status;

    public List<String> getProducts_list() {
        return products_list;
    }

    public void setProducts_list(List<String> products_list) {
        this.products_list = products_list;
    }

    public List<Integer> getProducts_id() {
        return products_id;
    }

    public void setProducts_id(List<Integer> products_id) {
        this.products_id = products_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
