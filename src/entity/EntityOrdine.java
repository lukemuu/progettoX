package entity;

import java.util.Date;

public class EntityOrdine {
	
	private int IdOrdine;
	private int IdRistorante;
	private int IdPescheria;
	private Date data;
	private int IdProdotto;
	private float qta;
	private float qtaAggiornata;
	
	
	public EntityOrdine(int idOrdine, int idRistorante, int idPescheria, Date data, int idProdotto, float qta) {
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
	public float getQta() {
		return qta;
	}
	public void setQta(float qta) {
		this.qta = qta;
	}
	public float getQtaAggiornata() {
		return qtaAggiornata;
	}
	public void setQtaAggiornata(float qtaAggiornata) {
		this.qtaAggiornata = qtaAggiornata;
	}
	
	
	
	

}
