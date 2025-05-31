
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.EntityOrdine;
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
                        result.getDouble("QTA")
                    );
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
            String query = "INSERT INTO ORDINE (IDORDINE, IDRISTORANTE, IDPESCHERIA, DATA, IDPRODOTTO, QTA, QTAAGGIORNATA) VALUES (?, ?, ?, ?, ?, ?, ?);";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, ordine.getIdOrdine());
                stmt.setInt(2, ordine.getIdRistorante());
                stmt.setInt(3, ordine.getIdPescheria());
                stmt.setDate(4, new java.sql.Date(ordine.getData().getTime()));
                stmt.setInt(5, ordine.getIdProdotto());
                stmt.setDouble(6, ordine.getQta());
                stmt.setDouble(7, ordine.getQtaAggiornata());

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
            String query = "UPDATE ORDINE SET IDRISTORANTE=?, IDPESCHERIA=?, DATA=?, IDPRODOTTO=?, QTA=?, QTAAGGIORNATA=? WHERE IDORDINE=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, ordine.getIdRistorante());
                stmt.setInt(2, ordine.getIdPescheria());
                stmt.setDate(3, new java.sql.Date(ordine.getData().getTime()));
                stmt.setInt(4, ordine.getIdProdotto());
                stmt.setDouble(5, ordine.getQta());
                stmt.setDouble(6, ordine.getQtaAggiornata());
                stmt.setInt(7, ordine.getIdOrdine());

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
                        result.getFloat("QTA")
                    );
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
    

	public static List<EntityOrdine> readOrdiniUltimaSettimana() throws DAOException, DBConnectionException {
	    List<EntityOrdine> ordini = new ArrayList<>();
	
	    try {
	        Connection conn = DBManager.getConnection();
	        String query = "SELECT * FROM ORDINE WHERE DATA >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY);";
	
	        try {
	            PreparedStatement stmt = conn.prepareStatement(query);
	            ResultSet result = stmt.executeQuery();
	
	            while (result.next()) {
	                EntityOrdine ordine = new EntityOrdine(
	                    result.getInt("IDRISTORANTE"),
	                    result.getInt("IDPESCHERIA"),
	                    result.getInt("IDPRODOTTO"),
	                    result.getDate("DATA"),
	                    result.getDouble("QTA")
	                );
	                ordini.add(ordine);
	            }
	        } catch (SQLException e) {
	            throw new DAOException("Errore lettura ordini dell'ultima settimana");
	        } finally {
	            DBManager.closeConnection();
	        }
	
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore di connessione DB");
	    }
	
	    return ordini;
	}
	
	public static List<EntityOrdine> readOrdiniUltimoGiorno() throws DAOException, DBConnectionException {
	    List<EntityOrdine> ordini = new ArrayList<>();

	    try {
	        Connection conn = DBManager.getConnection();
	        String query = "SELECT * FROM ORDINE WHERE DATA = CURRENT_DATE;";

	        try (PreparedStatement stmt = conn.prepareStatement(query);
	             ResultSet result = stmt.executeQuery()) {

	            while (result.next()) {
	                EntityOrdine ordine = new EntityOrdine(
	                    result.getInt("IDRISTORANTE"),
	                    result.getInt("IDPESCHERIA"),
	                    result.getInt("IDPRODOTTO"),
	                    result.getDate("DATA"),
	                    result.getDouble("QTA")
	                );
	                ordini.add(ordine);
	            }
	        } catch (SQLException e) {
	        	throw new DAOException("Errore lettura ordini dell'ultimo giorno: " + e.getMessage(), e);
	        }
	     
	    } catch (SQLException e) {
	        throw new DBConnectionException("Errore di connessione al database: " + e.getMessage(), e);
	    }

	    return ordini;
	}
	
	public static boolean updateStatoOrdine(int idOrdine, String nuovoStato) throws DAOException, DBConnectionException {
	    boolean success = false;

	    try {
	        Connection conn = DBManager.getConnection();
	        String query = "UPDATE ORDINE SET STATO=? WHERE IDORDINE=?;";

	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setString(1, nuovoStato);
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

