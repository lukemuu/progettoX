
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.EntityOrdine;
import entity.EntityOrdine.StatoOrdine;
import exception.DAOException;
import exception.DBConnectionException;

public class OrdineDAO {

    public static EntityOrdine readOrdine(int idOrdine) throws DAOException, DBConnectionException {
        EntityOrdine ordine = null;

        try {
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM ORDINE WHERE IDORDINE=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idOrdine);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    ordine = new EntityOrdine(
                        result.getInt("IDRISTORANTE"),
                        result.getInt("IDPESCHERIA"),
                        result.getInt("IDPRODOTTO"),
                        result.getDate("DATA"),
                        result.getDouble("QTA"),
                        result.getFloat("PREZZO")
                    );
                    ordine.setStato(StatoOrdine.valueOf(result.getString("STATO")));
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura ordine");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return ordine;
    }

    public static boolean createOrdine(EntityOrdine ordine) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "INSERT INTO ORDINE (IDORDINE, IDRISTORANTE, IDPESCHERIA, DATA, IDPRODOTTO, QTA, QTAAGGIORNATA, PREZZO, STATO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, ordine.getIdOrdine());
                stmt.setInt(2, ordine.getIdRistorante());
                stmt.setInt(3, ordine.getIdPescheria());
                stmt.setDate(4, new java.sql.Date(ordine.getData().getTime()));
                stmt.setInt(5, ordine.getIdProdotto());
                stmt.setDouble(6, ordine.getQta());
                stmt.setDouble(7, ordine.getQtaAggiornata());
                stmt.setFloat(8, ordine.getPrezzo());
                stmt.setString(9, ordine.getStato().name());

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore creazione ordine");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static boolean updateOrdine(EntityOrdine ordine) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "UPDATE ORDINE SET IDRISTORANTE=?, IDPESCHERIA=?, DATA=?, IDPRODOTTO=?, QTA=?, QTAAGGIORNATA=?, STATO=? WHERE IDORDINE=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, ordine.getIdRistorante());
                stmt.setInt(2, ordine.getIdPescheria());
                stmt.setDate(3, new java.sql.Date(ordine.getData().getTime()));
                stmt.setInt(4, ordine.getIdProdotto());
                stmt.setDouble(5, ordine.getQta());
                stmt.setDouble(6, ordine.getQtaAggiornata());
                stmt.setString(7, ordine.getStato().name());
                stmt.setInt(8, ordine.getIdOrdine());

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore aggiornamento ordine");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static boolean deleteOrdine(int idOrdine) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "DELETE FROM ORDINE WHERE IDORDINE=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idOrdine);

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore eliminazione ordine");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static List<EntityOrdine> readOrdini() throws DAOException, DBConnectionException {
        List<EntityOrdine> ordini = new ArrayList<>();

        try {
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM ORDINE;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    EntityOrdine ordine = new EntityOrdine(
                        result.getInt("IDRISTORANTE"),
                        result.getInt("IDPESCHERIA"),
                        result.getInt("IDPRODOTTO"),
                        result.getDate("DATA"),
                        result.getDouble("QTA"),
                        result.getFloat("PREZZO")
                    );
                    ordine.setStato(StatoOrdine.valueOf(result.getString("STATO")));
                    ordini.add(ordine);
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura ordini");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return ordini;
    }

    public static List<EntityOrdine> readOrdinibyPescheria_inTrattativa(int idPescheria) throws DAOException, DBConnectionException {
        List<EntityOrdine> ordini = new ArrayList<>();

        try {
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM ORDINE WHERE IDPESCHERIA=? AND STATO=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idPescheria);
                stmt.setString(2, StatoOrdine.IN_TRATTATIVA.name());
                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    EntityOrdine ordine = new EntityOrdine(
                        result.getInt("IDRISTORANTE"),
                        result.getInt("IDPESCHERIA"),
                        result.getInt("IDPRODOTTO"),
                        result.getDate("DATA"),
                        result.getDouble("QTA"),
                        result.getFloat("PREZZO")
                    );
                    ordine.setStato(StatoOrdine.valueOf(result.getString("STATO")));
                    ordini.add(ordine);
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura ordini per pescheria");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return ordini;
    }

	
	public static List<EntityOrdine> readOrdiniReport() throws DAOException, DBConnectionException {
	    List<EntityOrdine> ordini = new ArrayList<>();
	
	    try {
	        Connection conn = DBManager.getConnection();
	        String query = "SELECT IDORDINE, IDRISTORANTE, IDPESCHERIA, DATA, IDPRODOTTO, QTA, QTAAGGIORNATA, PREZZO, STATO " +
	                       "FROM ORDINE WHERE DATA >= ? AND STATO != ?;";
	
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Imposta la data di 7 giorni fa
	            stmt.setDate(1, new java.sql.Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000));
	            stmt.setString(2, StatoOrdine.IN_TRATTATIVA.name());
	
	            ResultSet result = stmt.executeQuery();
	
	            while (result.next()) {
	                EntityOrdine ordine = new EntityOrdine(
	                    result.getInt("IDRISTORANTE"),
	                    result.getInt("IDPESCHERIA"),
	                    result.getInt("IDPRODOTTO"),
	                    result.getDate("DATA"),
	                    result.getDouble("QTA"),
	                    result.getFloat("PREZZO")
	                );
	                ordine.setIdOrdine(result.getInt("IDORDINE"));
	                ordine.setQtaAggiornata(result.getDouble("QTAAGGIORNATA"));
	                ordine.setStato(StatoOrdine.valueOf(result.getString("STATO")));
	                ordini.add(ordine);
	            }
	
	            System.out.println("Ordini trovati: " + ordini.size());
	        } catch (SQLException e) {
	            throw new DAOException("Errore lettura ordini dell'ultima settimana: " + e.getMessage(), e);
	        } finally {
	            DBManager.closeConnection();
	        }
	
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore di connessione al database: " + e.getMessage(), e);
	    }
	
	    return ordini;
	}


    

	public static List<EntityOrdine> readOrdiniUltimoGiorno() throws DAOException, DBConnectionException {
	    List<EntityOrdine> ordini = new ArrayList<>();

	    try {
	        Connection conn = DBManager.getConnection();
	        String query = "SELECT * FROM ORDINE WHERE DATA = CURRENT_DATE AND STATO = ?;";

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Imposta il parametro della query
	            stmt.setString(1, StatoOrdine.CONFERMATO.name());

	            // Esegui la query
	            try (ResultSet result = stmt.executeQuery()) {
	                while (result.next()) {
	                    EntityOrdine ordine = new EntityOrdine(
	                        result.getInt("IDRISTORANTE"),
	                        result.getInt("IDPESCHERIA"),
	                        result.getInt("IDPRODOTTO"),
	                        result.getDate("DATA"),
	                        result.getDouble("QTA"),
	                        result.getFloat("PREZZO")
	                    );
	                    ordine.setIdOrdine(result.getInt("IDORDINE"));
	                    ordine.setStato(StatoOrdine.valueOf(result.getString("STATO")));
	                    ordini.add(ordine);
	                }
	            }
	        } catch (SQLException e) {
	            throw new DAOException("Errore lettura ordini dell'ultimo giorno: " + e.getMessage(), e);
	        }
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore di connessione al database: " + e.getMessage(), e);
	    }

	    return ordini;
	}

    public static boolean updateStatoOrdine(int idOrdine, StatoOrdine nuovoStato) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "UPDATE ORDINE SET STATO=? WHERE IDORDINE=?;";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nuovoStato.name());
                stmt.setInt(2, idOrdine);

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore aggiornamento stato ordine: " + e.getMessage(), e);
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione al database: " + e.getMessage(), e);
        }

        return success;
    }
}