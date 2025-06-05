package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.EntityFattorino;
import exception.DAOException;
import exception.DBConnectionException;

import java.util.ArrayList;
import java.util.List;

public class FattorinoDAO {

    public static EntityFattorino readFattorino(int idFattorino) throws DAOException, DBConnectionException {
        EntityFattorino fattorino = null;

        try {
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM FATTORINO WHERE IDFATTORINO=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idFattorino);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    fattorino = new EntityFattorino(result.getString("NOME"), result.getString("USERNAME"), result.getString("PASSWORD"));
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura fattorino");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return fattorino;
    }

    public static boolean createFattorino(EntityFattorino fattorino) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "INSERT INTO FATTORINO (IDFATTORINO, NOME, USERNAME, PASSWORD, STATO) VALUES (?, ?, ? , ?, ? );";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, fattorino.getIdFattorino());
                stmt.setString(2, fattorino.getNome());
                stmt.setString(3, fattorino.getUsername());
                stmt.setString(4, fattorino.getPassword());
                stmt.setString(5, fattorino.getStato());
                

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore creazione fattorino");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static boolean updateFattorino(EntityFattorino fattorino) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "UPDATE FATTORINO SET IDFATTORINO=?, NOME=?, USERNAME =?, PASSWORD =?, STATO =?,   WHERE IDFATTORINO=?;"; //IDFATTORINO chiave primaria

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, fattorino.getIdFattorino());
                stmt.setString(2, fattorino.getNome());
                stmt.setString(3, fattorino.getUsername());
                stmt.setString(4, fattorino.getPassword());
                stmt.setString(5, fattorino.getStato());
                

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore aggiornamento fattorino");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }

    public static boolean deleteFattorino(int idFattorino) throws DAOException, DBConnectionException {
        boolean success = false;

        try {
            Connection conn = DBManager.getConnection();
            String query = "DELETE FROM FATTORINO WHERE IDFATTORINO=?;";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idFattorino);

                success = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException("Errore eliminazione fattorino");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return success;
    }
    
    public static List<EntityFattorino> readAllFattorini() throws DAOException, DBConnectionException {
        List<EntityFattorino> fattorini = new ArrayList<>();

        try {
            Connection conn = DBManager.getConnection();
            String query = "SELECT * FROM FATTORINO;";

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet result = stmt.executeQuery()) {

                while (result.next()) {
                    EntityFattorino fattorino = new EntityFattorino(
                        result.getString("NOME"),
                        result.getString("USERNAME"),
                        result.getString("PASSWORD")
                    );
                    fattorini.add(fattorino);
                }
            } catch (SQLException e) {
                throw new DAOException("Errore lettura fattorini: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB: " + e.getMessage(), e);
        }

        return fattorini;
    }
    
}