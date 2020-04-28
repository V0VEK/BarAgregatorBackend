package app;

import java.util.List;

public class WaiterReceiptList {
    List<Integer> receipt_numbers;
    String status;

    public List<Integer> getReceipt_numbers() {
        return receipt_numbers;
    }

    public void setReceipt_numbers(List<Integer> receipt_numbers) {
        this.receipt_numbers = receipt_numbers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
