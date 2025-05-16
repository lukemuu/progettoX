package entity;

public class EntityFattorino {
	
	private int idFattorino;
	private String nome;
	private String username;
	private String password;
	
	public EntityFattorino() {
		super();
	}
	

	public EntityFattorino(int idFattorino, String nome, String username, String password) {
		super();
		this.idFattorino = idFattorino;
		this.nome = nome;
		this.username = username;
		this.password = password;
	}


	public int getIdRistoranteRegistrato() {
		return idFattorino;
	}


	public void setIdRistoranteRegistrato(int idFattorino) {
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
	

}
