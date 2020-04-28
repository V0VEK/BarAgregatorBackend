package app;

import java.util.List;

public class BarList {
    List<String> bar_names;
    List<Integer> bar_ids;
    String status;

    public List<String> getBar_names() {
        return bar_names;
    }

    public void setBar_names(List<String> bar_names) {
        this.bar_names = bar_names;
    }

    public List<Integer> getBar_ids() {
        return bar_ids;
    }

    public void setBar_ids(List<Integer> bar_ids) {
        this.bar_ids = bar_ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
