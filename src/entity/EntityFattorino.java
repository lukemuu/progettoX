package entity;

public class EntityFattorino {
	
	private static int contatoreIdFattorino = 1; 
	
	private int idFattorino;
	private String nome;
	private String username;
	private String password;
	private StatoFattorino stato; // Stato del fattorino (es. "disponibile", "occupato")
	
	public enum StatoFattorino {
		DISPONIBILE, OCCUPATO
	}
	
	public EntityFattorino() {

	}
	
	public EntityFattorino(String nome, String username, String password) {

		this.idFattorino = contatoreIdFattorino++; // Incrementa il contatore per ogni nuovo fattorino;
		this.nome = nome;
		this.username = username;
		this.password = password;
		this.stato = StatoFattorino.DISPONIBILE; // Stato iniziale del fattorino
	}
	
	public EntityFattorino(int idFattorino, String nome, String username, String password, StatoFattorino stato) {
		this.idFattorino = idFattorino;
		this.nome = nome;
		this.username = username;
		this.password = password;
		this.stato = stato;
	}

	public int getIdFattorino() {
		return idFattorino;
	}

	public void setIdFattorino(int idFattorino) {
		this.idFattorino = idFattorino;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public StatoFattorino getStato() {
		return stato;
	}

	public void setStato(StatoFattorino stato) {
		this.stato = stato;
	}


}