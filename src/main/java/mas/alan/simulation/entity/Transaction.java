package mas.alan.simulation.entity;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

    private String id;
    private Date transdate;
    private String total;

    public Transaction() {
    }

    public Transaction(String id, Date transdate, String total) {
        this.id = id;
        this.transdate = transdate;
        this.total = total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }

    public Date getTransdate() {
        return transdate;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }
}
