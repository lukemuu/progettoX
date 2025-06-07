
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.EntityPescheria;
import exception.DAOException;
import exception.DBConnectionException;

import java.util.ArrayList;
import java.util.List;

public class PescheriaDAO {

    public static EntityPescheria readPescheria(String idPescheria) throws DAOException, DBConnectionException {
        EntityPescheria pescheria = null;

        try {
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM PESCHERIA WHERE IDPESCHERIA=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, idPescheria);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    pescheria = new EntityPescheria(
                        result.getString("NOME"),
                        result.getString("INDIRIZZO"),
                        result.getString("EMAIL"),
                        result.getString("USERNAME"),
                        result.getString("PASSWORD")
                    );
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura pescheria");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return pescheria;
    }
    
	

	public static List<EntityPescheria> readPescherie() throws DAOException {
	    List<EntityPescheria> pescherie = new ArrayList<>();
	    String query = "SELECT * FROM PESCHERIA";
	
	    try (Connection connection = DBManager.getConnection();
	         Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {
	
	        while (rs.next()) {
	            EntityPescheria pescheria = new EntityPescheria(
	                rs.getString("NOME"),
	                rs.getString("INDIRIZZO"),
	                rs.getString("EMAIL"),
	                rs.getString("USERNAME"),
	                rs.getString("PASSWORD")
	            );
	            pescherie.add(pescheria);
	        }
	
	        // Log per debug
	        System.out.println("Pescherie trovate: " + pescherie.size());
	        for (EntityPescheria p : pescherie) {
	            System.out.println("ID: " + p.getIdPescheria() + ", Nome: " + p.getNome());
	        }
	
	    } catch (Exception e) {
	        throw new DAOException("Errore durante il recupero delle pescherie: " + e.getMessage(), e);
	    }
	
	    return pescherie;
	}



    public static boolean createPescheria(EntityPescheria pescheria) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "INSERT INTO PESCHERIA (IDPESCHERIA, NOME, INDIRIZZO, EMAIL, USERNAME, PASSWORD) VALUES (?, ?, ?, ?, ?, ?);";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, pescheria.getIdPescheria());
                stmt.setString(2, pescheria.getNome());
                stmt.setString(3, pescheria.getIndirizzo());
                stmt.setString(4, pescheria.getEmail());
                stmt.setString(5, pescheria.getUsername());
                stmt.setString(6, pescheria.getPassword());

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore creazione pescheria");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static boolean updatePescheria(EntityPescheria pescheria) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "UPDATE PESCHERIA SET NOME=?, INDIRIZZO=?, EMAIL=?, USERNAME=?, PASSWORD=? WHERE IDPESCHERIA=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, pescheria.getNome());
                stmt.setString(2, pescheria.getIndirizzo());
                stmt.setString(3, pescheria.getEmail());
                stmt.setString(4, pescheria.getUsername());
                stmt.setString(5, pescheria.getPassword());
                stmt.setInt(6, pescheria.getIdPescheria());

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore aggiornamento pescheria");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static boolean deletePescheria(String idPescheria) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "DELETE FROM PESCHERIA WHERE IDPESCHERIA=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, idPescheria);

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore eliminazione pescheria");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }
}
