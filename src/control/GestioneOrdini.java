
package control;

import database.OrdineDAO;
import database.PescheriaDAO;
import entity.EntityOrdine;
import entity.EntityPescheria;

import exception.DAOException;
import exception.DBConnectionException;

import java.util.ArrayList;
import java.util.List;

public class GestioneOrdini {
	
	private static GestioneOrdini gO = null;
	
	protected GestioneOrdini(){

	}

	public static GestioneOrdini getInstance() 
	{ 
		if (gO == null) 
			gO = new GestioneOrdini(); 

		return gO; 
	}
	
	

    public void inviaReport() {
        try {
            // Ottieni la lista degli ordini degli ultimi 7 giorni
            List<EntityOrdine> listaOrdini = OrdineDAO.readOrdiniUltimaSettimana();

            // Ottieni la lista delle pescherie
            List<EntityPescheria> listaPescherie = PescheriaDAO.readPescherie();

            // Per ogni pescheria, filtra gli ordini corrispondenti e invia il report
            for (EntityPescheria pescheria : listaPescherie) {
                String email = pescheria.getEmail();
                int IdPescheria = pescheria.getIdPescheria();

                List<EntityOrdine> ordiniPescheria = new ArrayList<>();

                // Filtra gli ordini per questa pescheria
                for (EntityOrdine ordine : listaOrdini) {
                    if (ordine.getIdPescheria() == IdPescheria) {
                        ordiniPescheria.add(ordine);
                    }
                }

                // Logica per inviare il report (da implementare)
                System.out.println("Report inviato a: " + email);
            }
        } catch (DAOException | DBConnectionException e) {
            System.err.println("Errore durante l'elaborazione del report: " + e.getMessage());
        }
    }
}
