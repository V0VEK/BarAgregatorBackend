package app;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="bar_info")
public class BarInfoDB {

    @Id
    @Column(name="bar_id")
    private Integer barID;

    @Column(name="bar_name")
    private String barName;

    @Column(name="waiter_id")
    private Integer waiterID;

    public Integer getWaiterID() {
        return waiterID;
    }

    public void setWaiterID(Integer waiterID) {
        this.waiterID = waiterID;
    }

    public Integer getBarID() {
        return barID;
    }

    public void setBarID(Integer barID) {
        this.barID = barID;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }
}
