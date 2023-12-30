package mas.alan.simulation.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List;
import mas.alan.simulation.utility.TimeConverter;
import mas.alan.simulation.utility.TimeFormat;

public class TablePackage implements Serializable {

    private long id;
    private String name;
    private String price;
    private String rate;
    private String every;
    private String minrate;
    private String multi_rate;
    private String multi_every;
    private String multi_minrate;
    private String mfrom;
    private String mto;
    private String stations;
    private String days;
    private String stopsetelah;
    private String pembulatan;
    private short deleted;

    public TablePackage() {
    }

    public void setMinRate(String minrate) {
        this.minrate = minrate;
    }

    public String getMinRate() {
        return minrate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getEvery() {
        return every;
    }

    public void setEvery(String every) {
        this.every = every;
    }

    public String getMultiRate() {
        return multi_rate;
    }

    public void setMultiRate(String multiRate) {
        this.multi_rate = multiRate;
    }

    public String getMultiEvery() {
        return multi_every;
    }

    public void setMultiEvery(String multiEvery) {
        this.multi_every = multiEvery;
    }

    public String getMultiMinRate() {
        return multi_minrate;
    }

    public void setMultiMinRate(String multiMinRate) {
        this.multi_minrate = multiMinRate;
    }

    public String getMFrom() {
        return mfrom;
    }

    public void setMFrom(String MFrom) {
        this.mfrom = MFrom;
    }

    public String getMTo() {
        return mto;
    }

    public void setMTo(String mto) {
        this.mto = mto;
    }

    public String getStations() {
        return stations;
    }

    public List<String> getStationList() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res = mapper.readTree(getStations());
        List<String> stationList = mapper.readValue(res.get("stations").toString(), new TypeReference<List<String>>() {
        });

        return stationList;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setStopSetelah(String stopsetelah) {
        this.stopsetelah = stopsetelah;
    }

    public String getStopSetelah() {
        return stopsetelah;
    }

    public void setPembulatan(String pembulatan) {
        this.pembulatan = pembulatan;
    }

    public String getPembulatan() {
        return pembulatan;
    }

    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }

    public short getDeleted() {
        return deleted;
    }

    public ActivePackage getActivePackage() {
        ActivePackage activePackage = new ActivePackage(getName());
        activePackage.setPackageId(getId());
        MRate mrate = new MRate();

        if (getRate() == null) {
            int currentIndex = 0;
            activePackage.setSingle(false);
            String[] multirate = getMultiRate().split("~");
            String[] multievery = getMultiEvery().split(",");
            String[] multiminrate = getMultiMinRate().split(",");
            String[] mfrom = getMFrom().split(",");
            String[] mto = getMTo().split(",");

            for (String rt : multirate) {
                double tableRate = Double.parseDouble(rt);
                int every = calculate(TimeConverter.convert(multievery[currentIndex]));
                double minrate = Double.parseDouble(multiminrate[currentIndex]);
                String MToTime = mto[currentIndex];
                String MFromTime = mfrom[currentIndex];
                Rate rate = new Rate(tableRate, every, minrate, MFromTime, MToTime);
                mrate.addRate(rate);
                currentIndex++;
            }
        } else {
            activePackage.setSingle(true);
            double tableRate = Double.parseDouble(getRate());
            int every = calculate(TimeConverter.convert(getEvery()));
            double minrate = Double.parseDouble(getMinRate());
            String MToTime = getMTo();
            String MFromTime = getMFrom();
            Rate rate = new Rate(tableRate, every, minrate, MFromTime, MToTime);
            mrate.addRate(rate);
        }

        activePackage.setRateList(mrate);
        activePackage.setPrice(Double.parseDouble(getPrice()));

        return activePackage;
    }

    private int calculate(TimeFormat time) {
        int totalminutes, totalseconds;
        if (time.getHours() != 0) {
            totalminutes = time.getHours() * 60;
            totalseconds = time.getMinutes() + totalminutes * 60;
        } else {
            totalseconds = time.getMinutes() * 60;
        }
        totalseconds += time.getSeconds();

        return totalseconds;
    }
}
