package app;

import java.util.List;

public class WaitersList {
    List<String> waiters_name;
    List<Integer> waiters_id;
    String status;

    public List<String> getWaiters_name() {
        return waiters_name;
    }

    public void setWaiters_name(List<String> waiters_name) {
        this.waiters_name = waiters_name;
    }

    public List<Integer> getWaiters_id() {
        return waiters_id;
    }

    public void setWaiters_id(List<Integer> waiters_id) {
        this.waiters_id = waiters_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
