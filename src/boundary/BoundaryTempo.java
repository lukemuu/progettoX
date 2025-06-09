
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
	        System.out.println("Esecuzione dello scheduler: " + LocalDate.now() + " " + LocalTime.now());
	        gestioneOrdini.inviaReport();
	    };
	
	    long initialDelay = calcolaRitardoIniziale();
	    System.out.println("Scheduler avviato con ritardo iniziale di: " + initialDelay + " millisecondi.");
	    scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toMillis(7), TimeUnit.MILLISECONDS);
	}
	
	private long calcolaRitardoIniziale() {
	    LocalDate oggi = LocalDate.now();
	    LocalTime oraCorrente = LocalTime.now();
	    LocalTime oraEsecuzione = LocalTime.of(9, 0); // Orario impostato alle 9:00
	
	    // Calcola i giorni di differenza fino al prossimo lunedì
	    int giorniFinoAlProssimoLunedi = (DayOfWeek.MONDAY.getValue() - oggi.getDayOfWeek().getValue() + 7) % 7;
	    if (giorniFinoAlProssimoLunedi == 0 && oraCorrente.isAfter(oraEsecuzione)) {
	        giorniFinoAlProssimoLunedi = 7; // Pianifica per il lunedì successivo se l'orario è già passato
	    }
	
	    // Calcola il ritardo in millisecondi
	    long ritardoInSecondi = giorniFinoAlProssimoLunedi * 24 * 60 * 60
	            + oraEsecuzione.toSecondOfDay() - oraCorrente.toSecondOfDay();
	    long ritardoInMillis = TimeUnit.SECONDS.toMillis(ritardoInSecondi);
	
	    System.out.println("Ritardo iniziale calcolato: " + ritardoInMillis + " millisecondi.");
	    return ritardoInMillis;
	}


    public void fermaScheduler() {
        scheduler.shutdown();
    }
}
