/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package mas.alan.simulation;

import java.util.Calendar;
import java.util.Date;
import mas.alan.simulation.entity.Station;
import mas.alan.simulation.entity.TablePackage;
import mas.alan.simulation.entity.TableTransaction;
import mas.alan.simulation.model.Calculation;

/**
 *
 * @author maulana
 */
public class Simulation {

    public static void main(String[] args) {
        final TablePackage multiRatePackage = newMultiRateTablePackage();
        Calendar cal = Calendar.getInstance();

        /*
        Transaction 1 : 
        - start  = 2023-12-30 16:00:00
        - target = 2 jam 30 menit
         */
        System.out.println("timer start from 2023-12-30 16:03:00");
        System.out.println("timer stop at 2023-12-30 18:30:00");
        System.out.println("------------------------------------");
        cal.set(2023, 12, 30, 16, 3, 0);
        TableTransaction trs1 = newTransaction(station1(), multiRatePackage, cal.getTime(), 2, 30);

        // timer up to 2023-12-30 16:05:00
        cal.set(2023, 12, 30, 16, 5, 0);
        System.out.println("timer up to 2023-12-30 16:05:00");
        Calculation calc5Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc5Min.toString() + "\n");

        // timer up to 2023-12-30 16:06:00
        cal.set(2023, 12, 30, 16, 6, 0);
        System.out.println("timer up to 2023-12-30 16:06:00");
        Calculation calc6Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc6Min.toString() + "\n");

        // timer up to 2023-12-30 16:30:00
        cal.set(2023, 12, 30, 16, 30, 0);
        System.out.println("timer up to 2023-12-30 16:30:00");
        Calculation calc30Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc30Min.toString() + "\n");

        // timer up to 2023-12-30 16:45:00
        cal.set(2023, 12, 30, 16, 45, 0);
        System.out.println("timer up to 2023-12-30 16:45:00");
        Calculation calc45Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc45Min.toString() + "\n");

        // timer up to 2023-12-30 17:10:00
        cal.set(2023, 12, 30, 17, 10, 0);
        System.out.println("timer up to 2023-12-30 17:10:00");
        Calculation calc70Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc70Min.toString() + "\n");

        // timer up to 2023-12-30 18:05:00
        cal.set(2023, 12, 30, 18, 5, 0);
        System.out.println("timer up to 2023-12-30 18:05:00");
        Calculation calc125Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc125Min.toString() + "\n");

        // timer up to 2023-12-30 18:30:00
        cal.set(2023, 12, 30, 18, 30, 0);
        System.out.println("timer up to 2023-12-30 18:30:00");
        Calculation calc150Min = new Calculation(trs1, cal.getTime());
        System.out.println(calc150Min.toString() + "\n");
        
        /*
        Transaction 2 : 
        - start  = 2023-12-30 15:00:00
        - target = 1 jam 0 menit
         */
        System.out.println("timer start from 2023-12-30 15:00:00");
        System.out.println("timer stop at 2023-12-30 16:00:00");
        System.out.println("------------------------------------");
        cal.set(2023, 12, 30, 15, 0, 0);
        TableTransaction trs2 = newTransaction(station1(), multiRatePackage, cal.getTime(), 1, 0);

        // timer up to 2023-12-30 16:01:00
        cal.set(2023, 12, 30, 16, 1, 0);
        System.out.println("timer up to 2023-12-30 16:01:00");
        Calculation calc61Min = new Calculation(trs2, cal.getTime());
        System.out.println(calc61Min.toString() + "\n");
        
        /*
        Transaction 3 : 
        - start  = 2023-12-30 23:02:00
        - target = 4 jam 30 menit
         */
        System.out.println("timer start from 2023-12-30 23:02:00");
        System.out.println("timer stop at 2023-12-31 03:32:00");
        System.out.println("------------------------------------");
        cal.set(2023, 12, 30, 23, 2, 0);
        TableTransaction trs3 = newTransaction(station1(), multiRatePackage, cal.getTime(), 4, 30);

        // timer up to 2023-12-31 02:45:00
        cal.set(2023, 12, 31, 2, 45, 0);
        System.out.println("timer up to 2023-12-31 02:45:00");
        Calculation calc225Min = new Calculation(trs3, cal.getTime());
        System.out.println(calc225Min.toString() + "\n");
    }

    private static TablePackage newMultiRateTablePackage() {
        /*
        START      FINISH       RATE        MINRATE     EVERY
        11:00:00 - 17:59:59     Rp666.6     Rp40000     00:01:00
        18:00:00 - 23:59:59     Rp833.3     Rp50000     00:01:00
        00:00:00 - 10:59:59     Rp833.3     Rp50000     00:01:00
         */

        TablePackage tablePackage = new TablePackage();
        tablePackage.setId(41);
        tablePackage.setName("Regular");
        tablePackage.setPrice("3000");
        tablePackage.setMultiRate("666.6666666666666~833.3333333333334~833.3333333333334");
        tablePackage.setMultiEvery("00:05:00,00:05:00,00:05:00");
        tablePackage.setMultiMinRate("40000.0,50000.0,50000.0");
        tablePackage.setMFrom("11:00:00,18:00:00,00:00:00");
        tablePackage.setMTo("17:59:59,23:59:59,10:59:59");
        tablePackage.setStations("{\"stations\": [1,2,4,7,8,9,10,11,12,18,19,20,21,22,23]}");
        tablePackage.setDays("{\"days\": [\"Sunday\",\"Monday\",\"Tuesday\",\"Wednesday\",\"Thursday\",\"Friday\",\"Saturday\"]}");
        tablePackage.setStopSetelah("00:00:00");
        tablePackage.setDeleted(Short.parseShort("0"));
        tablePackage.setPembulatan("300");

        return tablePackage;
    }

    private static Station station1() {
        Station station = new Station();
        station.setId(1);
        station.setName("Meja 1");
        return station;
    }

    private static TableTransaction newTransaction(Station station, TablePackage tablePackage, Date start, int hour, int minute) {
        TableTransaction transaction = new TableTransaction();
        transaction.setId(1);
        transaction.setStation(station);
        transaction.setTablePackage(tablePackage);
        transaction.setStart(start);
        transaction.setAmount("0");
        transaction.setPowerFailure(Short.parseShort("0"));
        transaction.setTarget(String.valueOf(hour * 3600 + minute * 60));
        return transaction;
    }
}
