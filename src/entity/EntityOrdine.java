package entity;

import java.util.Date;

import database.OrdineDAO;
import exception.DAOException;
import exception.DBConnectionException;

public class EntityOrdine {
	
	private static int contatoreIdOrdine = 1; // Contatore per generare ID unici
	
	private int idOrdine;
	private int idRistorante;
	private int idPescheria;
	private int idProdotto;
	private Date data;
	private double qta;
	private double qtaAggiornata;
	private String stato;
	
	
	public EntityOrdine(int idRistorante, int idPescheria, int idProdotto, Date data, double qta) {
		
		this.idOrdine = contatoreIdOrdine++; // Incrementa il contatore per ogni nuovo ordine
		this.idRistorante = idRistorante;
		this.idPescheria = idPescheria;
		this.data = data;
		this.idProdotto = idProdotto;
		this.qta = qta;
		this.stato = "In trattativa";
	}
	
	public EntityOrdine() {
		
	}
	
	
	public int getIdOrdine() {
		return idOrdine;
	}
	
	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}
	
	public int getIdRistorante() {
		return idRistorante;
	}
	
	public void setIdRistorante(int idRistorante) {
		this.idRistorante = idRistorante;
	}
	
	public int getIdPescheria() {
		return idPescheria;
	}
	
	public void setIdPescheria(int idPescheria) {
		this.idPescheria = idPescheria;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public int getIdProdotto() {
		return idProdotto;
	}
	
	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	public double getQta() {
		return qta;
	}
	
	public void setQta(double qta) {
		this.qta = qta;
	}
	
	public double getQtaAggiornata() {
		return qtaAggiornata;
	}
	
	public void setQtaAggiornata(double qtaAggiornata) {
		this.qtaAggiornata = qtaAggiornata;
	}
	
	public String getStato() {
		return stato;
	}
	
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	public void saveOrdine() throws DAOException, DBConnectionException {
	    OrdineDAO.createOrdine(this);
	}
	

}
