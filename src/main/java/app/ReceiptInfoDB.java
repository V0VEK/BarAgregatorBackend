package app;


import javax.persistence.*;

@Entity
@Table(name="receipt_info")
public class ReceiptInfoDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_receipt_id")
    private Integer uniqueReceiptID;

    @Column(name="bar_id")
    private Integer barID;

    @Column(name="bar_receipt_id")
    private Integer barReceiptID;

    @Column(name="waiter_id")
    private Integer waiterID;

    public Integer getUniqueReceiptID() {
        return uniqueReceiptID;
    }

    public void setUniqueReceiptID(Integer uniqueReceiptID) {
        this.uniqueReceiptID = uniqueReceiptID;
    }

    public Integer getBarID() {
        return barID;
    }

    public void setBarID(Integer barID) {
        this.barID = barID;
    }

    public Integer getBarReceiptID() {
        return barReceiptID;
    }

    public void setBarReceiptID(Integer barReceiptID) {
        this.barReceiptID = barReceiptID;
    }

    public Integer getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(Integer waiterID) {
        this.waiterID = waiterID;
    }
}
