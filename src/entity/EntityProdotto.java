package entity;
 
public class EntityProdotto {
	
	private static int contatoreIdProdotto = 1; // Contatore per generare ID unici
	
	private int idProdotto;
	private String categoria;
	private String tipologia;
	private String descrizione;
	private float prezzo;
	private String codicePaese;
	
	public EntityProdotto(int codice, String categoria, String tipologia, String descrizione, float prezzo,
			String codicePaese) {
		super();
		this.idProdotto = contatoreIdProdotto++; // Incrementa il contatore per ogni nuovo prodotto;
		this.categoria = categoria;
		this.tipologia = tipologia;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
		this.codicePaese = codicePaese;
	}
 
	public int getIdProdotto() {
		return idProdotto;
	}
 
	public void setCodice(int idProdotto) {
		this.idProdotto = idProdotto;
	}
 
	public String getCategoria() {
		return categoria;
	}
 
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
 
	public String getTipologia() {
		return tipologia;
	}
 
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
 
	public String getDescrizione() {
		return descrizione;
	}
 
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
 
	public float getPrezzo() {
		return prezzo;
	}
 
	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}
 
	public String getCodicePaese() {
		return codicePaese;
	}
 
	public void setCodicePaese(String codicePaese) {
		this.codicePaese = codicePaese;
	}
	
}
