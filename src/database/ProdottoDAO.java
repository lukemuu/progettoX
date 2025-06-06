package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.EntityProdotto;
import exception.DAOException;
import exception.DBConnectionException;

public class ProdottoDAO {

    public static EntityProdotto readProdotto(int idPescheria, int idProdotto) throws DAOException, DBConnectionException {

        EntityProdotto eP = null;

        try {
            Connection conn = DBManager.getConnection();

            try {
                String query = "SELECT * FROM PRODOTTO WHERE IDPESCHERIA = ? AND IDPRODOTTO = ?;";

                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setInt(1, idPescheria);
                stmt.setInt(2, idProdotto);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                	eP = new EntityProdotto(
                		    result.getInt("IDPRODOTTO"),         
                		    result.getString("CATEGORIA"),      
                		    result.getString("TIPOLOGIA"),      
                		    result.getString("DESCRIZIONE"),    
                		    result.getFloat("PREZZO"),          
                		    result.getString("CODICEPAESE"),
                		    result.getInt("IDPESCHERIA")
                		);
                }

            } catch (SQLException e) {
                throw new DAOException("Errore lettura prodotto");
            } finally {
                DBManager.closeConnection();
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione DB");
        }

        return eP;
    }
}



