package app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="waiter_info")
public class WaiterInfoDB {

    @Id
    @Column(name="waiter_id")
    private int waiterID;

    @Column(name="bar_id")
    private int barID;

    @Column(name="bar_receipt_id")
    private int receiptBarID;

    @Column(name="waiter_name")
    private String waiterName;

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public int getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(int waiterID) {
        this.waiterID = waiterID;
    }

    public int getBarID() {
        return barID;
    }

    public void setBarID(int barID) {
        this.barID = barID;
    }

    public int getReceiptBarID() {
        return receiptBarID;
    }

    public void setReceiptBarID(int receiptBarID) {
        this.receiptBarID = receiptBarID;
    }
}
