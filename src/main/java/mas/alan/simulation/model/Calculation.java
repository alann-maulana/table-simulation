/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mas.alan.simulation.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import mas.alan.simulation.entity.ActivePackage;
import mas.alan.simulation.entity.MRate;
import mas.alan.simulation.entity.Rate;
import mas.alan.simulation.entity.TablePackage;
import mas.alan.simulation.entity.TableTransaction;

/**
 *
 * @author maulana
 */
public class Calculation {

    private double totalTarif;
    private long sisaWaktu;

    public Calculation(TableTransaction transaction, Date upto) {
        this.totalTarif = 0;
        this.sisaWaktu = 0;

        this.calculate(transaction, upto);
    }

    public double getTotalTarif() {
        return totalTarif;
    }

    public long getSisaWaktu() {
        return sisaWaktu;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("- total Tarif : Rp").append(Math.round(totalTarif));
        sb.append("\n");
        String formatted = String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                sisaWaktu / 3600,
                (sisaWaktu % 3600) / 60,
                sisaWaktu % 60
        );
        sb.append("- sisa Waktu : ").append(formatted);
        return sb.toString();
    }

    private void calculate(TableTransaction transaction, Date upto) {
        Date start = transaction.getStart();
        TablePackage tablePackage = transaction.getTablePackage();

        ActivePackage activePackage = tablePackage.getActivePackage();
        MRate mRate = activePackage.getRateList();
        Duration target = Duration.ofSeconds(Long.parseLong(transaction.getTarget()));

        // execute when transaction status = 0
        if (transaction.getStatus() != (short) 0) {
            return;
        }

        // execute when transaction power failure = 0
        if (transaction.getPowerFailure() != (short) 0) {
            return;
        }

        // Waktu meja start
        LocalDateTime begin = toLocalDateTime(start);
        // Waktu sekarang
        LocalDateTime activeTimer = toLocalDateTime(upto);
        // Waktu meja berakhir
        LocalDateTime end = begin.plus(target.getSeconds(), ChronoUnit.SECONDS).withNano(0);
        Duration diffSisa = Duration.between(activeTimer, end);
        this.sisaWaktu = diffSisa.getSeconds();

        List<Rate> rates = mRate.getAvailableRates(begin, activeTimer);
        
        // stop if no range rate found
        if (rates.isEmpty()) {
            return;
        }

        // single range rate
        if (rates.size() == 1) {
            Rate rate = rates.get(0);
            int every = rate.getEvery();
            LocalDateTime time = begin.plus(every, ChronoUnit.SECONDS).withNano(0);

            while (time.isBefore(activeTimer) || time.isEqual(activeTimer)) {
                // increment total tarif
                totalTarif += rate.getRate();

                // increment calculation time
                time = time.plus(every, ChronoUnit.SECONDS);
            }

            if (totalTarif < rate.getMinRate()) {
                totalTarif = rate.getMinRate();
            }

            return;
        }

        // multi range rate
        for (Rate rate : rates) {
            LocalTime from = LocalTime.parse(rate.getMFromTime());
            LocalTime to = LocalTime.parse(rate.getMToTime());

            // setup from date
            Calendar calendar = Calendar.getInstance();
            setCalendarDateTime(calendar, start, from.getHour(), from.getMinute(), from.getSecond());
            LocalDateTime fromDate = begin.toLocalTime().withNano(0).isAfter(from)
                    ? begin
                    : toLocalDateTime(calendar.getTime());

            // setup to date
            setCalendarDateTime(calendar, upto, to.getHour(), to.getMinute(), to.getSecond());
            LocalDateTime toDate = activeTimer.toLocalTime().withNano(0).isBefore(to)
                    ? activeTimer
                    : toLocalDateTime(calendar.getTime());

            int every = rate.getEvery();

            LocalDateTime time = fromDate.plus(every, ChronoUnit.SECONDS).withNano(0);

            double total = 0;
            while (time.isBefore(toDate) || time.isEqual(toDate)) {
                // increment total tarif
                total += rate.getRate();
                totalTarif += rate.getRate();

                StringBuilder sb = new StringBuilder();
                sb.append("~ ").append(time);
                sb.append(" : Rp").append(rate.getRate());
                System.out.println(sb.toString());

                // increment calculation time
                time = time.plus(every, ChronoUnit.SECONDS);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("$ ").append(rate.getMFromTime()).append("-").append(rate.getMToTime());
            sb.append(" : Rp").append(Math.round(total));
            System.out.println(sb.toString());
        }
    }
    
    private void setCalendarDateTime(Calendar calendar, Date date, int hour, int minute, int second) {
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
    }
    
    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withNano(0);
    }
}
