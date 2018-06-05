package automato;

public class TokenClassificado {
	private String token;
	private int ultimoEstado;
	
	public TokenClassificado(String token, int ultimoEstado) {
		this.token = token;
		this.ultimoEstado = ultimoEstado;
	}

	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public int getUltimoEstado() {
		return ultimoEstado;
	}
	
	public void setUltimoEstado(int ultimoEstado) {
		this.ultimoEstado = ultimoEstado;
	}
}
