package entity;

import java.util.Date;
import java.util.Set;

public class EntityConsegna {
	
	private int idConsegna;
	private EntityFattorino fattorino;
	private Date data;
	private java.util.Set<EntityOrdine> ordini;
	
	
	public EntityConsegna() {
		
		super();
		
	}
	
	
	public EntityConsegna(EntityFattorino fattorino, Date data, Set<EntityOrdine> ordini) {
		super();
		this.fattorino = fattorino;
		this.data = data;
		this.ordini = ordini;
	}
	

	public int getIdConsegna() {
		return idConsegna;
	}


	public void setIdConsegna(int idConsegna) {
		this.idConsegna = idConsegna;
	}


	public EntityFattorino getFattorino() {
		return fattorino;
	}


	public void setFattorino(EntityFattorino fattorino) {
		this.fattorino = fattorino;
	}


	public Date getData() {
		return data;
	}


	public void setData(Date data) {
		this.data = data;
	}
	
	
	
	
}
