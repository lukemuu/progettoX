
package boundary;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import control.GestioneOrdini;

public class BoundaryTempo {
	
	GestioneOrdini gestioneOrdini = GestioneOrdini.getInstance();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void avviaScheduler() {
        Runnable task = () -> {
            if (LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY) {
                gestioneOrdini.inviaReport();
            }
        };

        long initialDelay = calcolaRitardoIniziale();
        scheduler.scheduleAtFixedRate(task, initialDelay, 7, TimeUnit.DAYS);
    }

    private long calcolaRitardoIniziale() {
        LocalDate oggi = LocalDate.now();
        LocalTime oraCorrente = LocalTime.now();
        LocalTime oraEsecuzione = LocalTime.of(9, 0); // Esempio: esegui alle 9:00

        long ritardo = 0;
        if (oggi.getDayOfWeek() != DayOfWeek.MONDAY) {
            ritardo = TimeUnit.DAYS.toMillis(DayOfWeek.MONDAY.getValue() - oggi.getDayOfWeek().getValue());
        } else if (oraCorrente.isAfter(oraEsecuzione)) {
            ritardo = TimeUnit.DAYS.toMillis(7); // Pianifica per il prossimo lunedì
        }

        return ritardo + TimeUnit.MILLISECONDS.convert(oraEsecuzione.toSecondOfDay() - oraCorrente.toSecondOfDay(), TimeUnit.SECONDS);
    }

    public void fermaScheduler() {
        scheduler.shutdown();
    }
}
