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

        if (transaction.getStatus() == (short) 0) {
            if (transaction.getPowerFailure() == (short) 0) {
                // Waktu meja start
                LocalDateTime begin = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()).withNano(0);
                // Waktu sekarang
                LocalDateTime activeTimer = LocalDateTime.ofInstant(upto.toInstant(), ZoneId.systemDefault()).withNano(0);
                // Waktu meja berakhir
                LocalDateTime end = begin.plus(target.getSeconds(), ChronoUnit.SECONDS).withNano(0);
                Duration diffSisa = Duration.between(activeTimer, end);
                this.sisaWaktu = diffSisa.getSeconds();

                List<Rate> rates = mRate.getAvailableRates(begin, activeTimer);

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
                } else {
                    // multi range rate
                    for (Rate rate : rates) {
                        LocalTime from = LocalTime.parse(rate.getMFromTime());
                        LocalTime to = LocalTime.parse(rate.getMToTime());

                        // setup from date
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.set(Calendar.HOUR_OF_DAY, from.getHour());
                        calendar.set(Calendar.MINUTE, from.getMinute());
                        calendar.set(Calendar.SECOND, from.getSecond());
                        LocalDateTime fromDate = begin.toLocalTime().withNano(0).isAfter(from)
                                ? begin
                                : LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()).withNano(0);

                        // setup to date
                        calendar.setTime(upto);
                        calendar.set(Calendar.HOUR_OF_DAY, to.getHour());
                        calendar.set(Calendar.MINUTE, to.getMinute());
                        calendar.set(Calendar.SECOND, to.getSecond());
                        LocalDateTime toDate = activeTimer.toLocalTime().withNano(0).isBefore(to)
                                ? activeTimer
                                : LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault()).withNano(0);

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
            }
        }
    }
}
