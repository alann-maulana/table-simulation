package mas.alan.simulation.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import mas.alan.simulation.utility.TimeUtil;

public class MRate {

    private List<Rate> ratelist = new ArrayList<>();

    public MRate() {
    }

    public MRate(List<Rate> rates) {
        this.ratelist = rates;
    }

    public void setRates(List<Rate> rates) {
        for (Rate rate : rates) {
            ratelist.add(rate);
        }
    }

    public void addRate(Rate rate) {
        ratelist.add(rate);
    }

    public Rate getRate(int index) {
        return ratelist.get(index);
    }

    public List<Rate> getRates() {
        return ratelist;
    }

    public List<Rate> getAvailableRates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Rate> availableRates = new ArrayList<>();

        for (Rate rate : ratelist) {
            LocalTime availableStartTime = LocalTime.parse(rate.getMFromTime());
            LocalTime availableEndTime = LocalTime.parse(rate.getMToTime());

            // Cek apakah jadwal paket tersedia selama jangka waktu yang diberikan
            if (TimeUtil.isPackageAvailableByTime(startDateTime, endDateTime, availableStartTime, availableEndTime)) {
                availableRates.add(rate);
            }
        }

        return availableRates;
    }

    public Rate getAvailableRateByTime(LocalDateTime currentTime) {
        LocalTime MFrom, MTo;
        Rate rate = null;

        for (Rate rt : ratelist) {
            MFrom = LocalTime.parse(rt.getMFromTime());
            MTo = LocalTime.parse(rt.getMToTime());

            if (TimeUtil.isPackageAvailableTime(currentTime, MFrom, MTo)) {
                rate = rt;
                break;
            }
        }
        return rate;
    }

    public Rate getAvailableRate() {
        LocalTime MFrom, MTo;
        Rate rate = null;

        for (Rate rt : ratelist) {
            MFrom = LocalTime.parse(rt.getMFromTime());
            MTo = LocalTime.parse(rt.getMToTime());

            if (TimeUtil.isPackageAvailable(MFrom, MTo)) {
                rate = rt;
            }
        }

        return rate;
    }

    public Rate getAvailableRateByTime(LocalDateTime startDateTime, LocalDateTime currentDateTime) {
        LocalTime MFrom, MTo;
        Rate rate = null;

        for (Rate rt : ratelist) {
            MFrom = LocalTime.parse(rt.getMFromTime());
            MTo = LocalTime.parse(rt.getMToTime());

            if (TimeUtil.isPackageAvailableByTime(startDateTime, currentDateTime, MFrom, MTo)) {
                rate = rt;
                break; // jika sudah ditemukan, keluar dari loop
            }
        }

        return rate;
    }
}
