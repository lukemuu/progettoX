package entity;

import java.util.Date;

import database.OrdineDAO;
import exception.DAOException;
import exception.DBConnectionException;

public class EntityOrdine {
	
	private int IdOrdine;
	private int IdRistorante;
	private int IdPescheria;
	private Date data;
	private int IdProdotto;
	private double qta;
	private double qtaAggiornata;
	
	
	public EntityOrdine(int idOrdine, int idRistorante, int idPescheria, Date data, int idProdotto, double qta) {
		super();
		IdOrdine = idOrdine;
		IdRistorante = idRistorante;
		IdPescheria = idPescheria;
		this.data = data;
		IdProdotto = idProdotto;
		this.qta = qta;
	}
	
	public EntityOrdine() {
		super();
	}
	
	
	public int getIdOrdine() {
		return IdOrdine;
	}
	public void setIdOrdine(int idOrdine) {
		IdOrdine = idOrdine;
	}
	public int getIdRistorante() {
		return IdRistorante;
	}
	public void setIdRistorante(int idRistorante) {
		IdRistorante = idRistorante;
	}
	public int getIdPescheria() {
		return IdPescheria;
	}
	public void setIdPescheria(int idPescheria) {
		IdPescheria = idPescheria;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getIdProdotto() {
		return IdProdotto;
	}
	public void setIdProdotto(int idProdotto) {
		IdProdotto = idProdotto;
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
	
	public void saveOrdine() throws DAOException, DBConnectionException {
	    OrdineDAO.createOrdine(this);
	}
	

}
