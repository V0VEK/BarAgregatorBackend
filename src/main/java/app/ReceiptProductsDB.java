package app;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="receipt_products")
public class ReceiptProductsDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="primary_key")
    private Integer primaryKey;

    @Column(name="unique_receipt_id")
    private Integer uniqueReceiptID;

    @Column(name="product_id")
    private Integer productID;

    @Column(name="is_paid")
    private Integer isPaidFlag;

    @Column(name="waiter_id")
    private Integer waiterID;


    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Integer getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(Integer waiterID) {
        this.waiterID = waiterID;
    }

    public Integer getUniqueReceiptID() {
        return uniqueReceiptID;
    }

    public void setUniqueReceiptID(Integer uniqueReceiptID) {
        this.uniqueReceiptID = uniqueReceiptID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getIsPaidFlag() {
        return isPaidFlag;
    }

    public void setIsPaidFlag(Integer isPaidFlag) {
        this.isPaidFlag = isPaidFlag;
    }
}
