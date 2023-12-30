package mas.alan.simulation.entity;

import java.io.Serializable;
import java.util.Date;

public class TableTransaction implements Serializable {
    
    private long id;
    private Transaction transaction;
    private Station station;
    private TablePackage tablePackage;
    private Date transStart;
    private Date transStop;
    private String amount;
    private String duration;
    private String target;
    private short powerfailure;
    private short status; // 1 = active
    
    public TableTransaction() {}
    
    public TableTransaction(Transaction transaction, Station station, Date transStart, String amount, String duration, short status) {
        this.transaction = transaction;
        this.station = station;
        this.transStart = transStart;
        this.amount = amount;
        this.duration = duration;
        this.status = status;
    }
    
    public void setTablePackage(TablePackage tablePackage) {
        this.tablePackage = tablePackage;
    }
    
    public TablePackage getTablePackage() {
        return tablePackage;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
    
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Transaction getTransaction() {
        return transaction;
    }
    
    public void setStation(Station station) {
        this.station = station;
    }
    
    public Station getStation() {
        return station;
    }
    
    public void setStart(Date start) {
        this.transStart = start;
    }
    
    public Date getStart() {
        return transStart;
    }
    
    public void setStop(Date stop) {
        this.transStop = stop;
    }
    
    public Date getStop() {
        return transStop;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    public String getAmount() {
        return amount;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setStatus(short status) {
        this.status = status;
    }
    
    public short getStatus() {
        return status;
    }
    
    public void setPowerFailure(short powerstatus) {
        this.powerfailure = powerstatus;
    }
    
    public short getPowerFailure() {
        return powerfailure;
    }
}
