package entity;

public class EntityRistoranteRegistrato {
	
	private static int contatoreIdRistorante = 1; 
	
	private int idRistoranteRegistrato;
	private String nome;
	private String username;
	private String password;
	private String indirizzo;
	private String email;
	
	public EntityRistoranteRegistrato() {

	}


	public EntityRistoranteRegistrato(String nome, String username, String password, String indirizzo, String email) {

		this.idRistoranteRegistrato = contatoreIdRistorante++;
		this.nome = nome;
		this.username = username;
		this.password = password;
		this.indirizzo = indirizzo;
		this.email = email;
	}


	public int getIdRistoranteRegistrato() {
		return idRistoranteRegistrato;
	}


	public void setIdRistoranteRegistrato(int idRistoranteRegistrato) {
		this.idRistoranteRegistrato = idRistoranteRegistrato;
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


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

}