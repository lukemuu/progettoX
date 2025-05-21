package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.EntityOrdine;
import entity.EntityFattorino;
import exception.DAOException;
import exception.DBConnectionException;

import control.GestioneOrdini;

public class ConsegnaDAO {


	public static boolean creaConsegna(java.sql.Date dataOdierna) throws DAOException, DBConnectionException {
	    boolean success = false;
	
	    try {
	        // Recupera ordine e fattorino selezionati da GestioneOrdini
	        EntityOrdine ordineSelezionato = GestioneOrdini.getOrdineSelezionato();
	        EntityFattorino fattorinoSelezionato = GestioneOrdini.getFattorinoSelezionato();
	
	        if (ordineSelezionato == null || fattorinoSelezionato == null) {
	            throw new DAOException("Ordine o fattorino selezionato non valido.");
	        }
	
	        Connection conn = DBManager.getConnection();
	        String query = "INSERT INTO CONSEGNA (IDORDINE, IDFATTORINO, DATA_CONSEGNA) VALUES (?, ?, ?);";
	
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Imposta i parametri della query
	            stmt.setInt(1, ordineSelezionato.getIdOrdine());
	            stmt.setInt(2, fattorinoSelezionato.getIdFattorino());
	            stmt.setDate(3, dataOdierna);
	
	            // Esegui l'operazione di inserimento
	            success = stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            throw new DAOException("Errore durante la creazione della consegna: " + e.getMessage(), e);
	        } finally {
	            DBManager.closeConnection();
	        }
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore di connessione al database: " + e.getMessage(), e);
	    }
	
	    return success;
	}

}


