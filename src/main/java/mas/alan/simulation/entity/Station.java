package mas.alan.simulation.entity;

import java.io.Serializable;

public class Station implements Serializable {

    private long id;
    private String name;
    private String status;
    private String terminal;
    private TablePackage packageId;

    public Station() {
    }

    public Station(long id, String name, String terminal) {
        this.id = id;
        this.name = name;
        this.terminal = terminal;
    }

    public Station(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPackage(TablePackage id) {
        this.packageId = id;
    }

    public TablePackage getPackage() {
        return packageId;
    }
}
