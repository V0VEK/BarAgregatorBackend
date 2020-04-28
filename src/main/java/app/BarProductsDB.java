package app;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name="bar_products")
public class BarProductsDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Integer productID;

    @Column(name="bar_id")
    private Integer barID;

    @Column(name="product_name")
    private String productName;


    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getBarID() {
        return barID;
    }

    public void setBarID(Integer barID) {
        this.barID = barID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
