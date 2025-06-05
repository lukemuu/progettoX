package entity;
 
public class EntityPescheria {
	
	private static int contatoreIdPescheria = 1; 

	private int idPescheria;
	private String nome;
	private String indirizzo;
	private String email;
	private String username;
	private String password;
	
	public EntityPescheria(String nome, String indirizzo, String email, String username, String password) {
		
		this.idPescheria = contatoreIdPescheria++;
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	public EntityPescheria() {
	}
	
	public int getIdPescheria() {
		return idPescheria;
	}

	public void setIdPescheria(int idPescheria) {
		this.idPescheria = idPescheria;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
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