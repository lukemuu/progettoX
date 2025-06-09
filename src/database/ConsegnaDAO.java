package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.EntityOrdine;
import exception.DAOException;
import exception.DBConnectionException;

public class ConsegnaDAO {

    public static boolean creaConsegna(int idOrdine, int idFattorino, java.sql.Date dataOdierna) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "INSERT INTO CONSEGNA (IDORDINE, IDFATTORINO, DATA) VALUES (?, ?, ?);";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                // Imposta i parametri della query
                stmt.setInt(1, idOrdine);
                stmt.setInt(2, idFattorino);
                stmt.setDate(3, dataOdierna);

                // Esegui l'operazione di inserimento
                success = stmt.executeUpdate() > 0;

                // Se la consegna è stata creata con successo, aggiorna lo stato dell'ordine
                if (success) {
                    boolean statoAggiornato = OrdineDAO.updateStatoOrdine(
                        idOrdine,
                        EntityOrdine.StatoOrdine.ASSEGNATO // Conversione corretta
                    );

                    if (!statoAggiornato) {
                        throw new DAOException("Errore durante l'aggiornamento dello stato dell'ordine.");
                    }
                }
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
