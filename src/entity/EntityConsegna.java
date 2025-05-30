package entity;

import java.util.Date;

public class EntityConsegna {
	
	private static int contatoreIdFattorino = 1; 
	
	private int idConsegna;
	private int idFattorino;
	private Date data;
	private int[] idOrdini;
	
	
	public EntityConsegna() {
		
	}
	
	public EntityConsegna(int idFattorino, Date data, int[] idOrdini) {
		
		this.idFattorino = contatoreIdFattorino++; // Incrementa il contatore per ogni nuova consegna;
		this.data = data;
		this.setIdOrdini(idOrdini);
	}

	public int getIdConsegna() {
		return idConsegna;
	}

	public void setIdConsegna(int idConsegna) {
		this.idConsegna = idConsegna;
	}

	public int getIdFattorino() {
		return idFattorino;
	}
	
	public void setIdFattorino(int idFattorino) {
		this.idFattorino = idFattorino;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int[] getIdOrdini() {
		return idOrdini;
	}

	public void setIdOrdini(int[] idOrdini) {
		this.idOrdini = idOrdini;
	}
	
	
}