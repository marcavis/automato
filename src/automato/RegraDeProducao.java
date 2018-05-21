package automato;

public class RegraDeProducao {
	private int estadoOrigem;
	private int estadoFim;
	private char simbolo;
	
	public RegraDeProducao() {
	}
	
	public RegraDeProducao(int estadoOrigem, int estadoFim, char simbolo) {
		this.estadoOrigem = estadoOrigem;
		this.estadoFim = estadoFim;
		this.simbolo = simbolo;
	}
	
	public boolean equals(RegraDeProducao regra) {
		return regra.getEstadoOrigem() == estadoOrigem &&
				regra.getEstadoFim() == estadoFim &&
				regra.getSimbolo() == simbolo;
	}

	public int getEstadoOrigem() {
		return estadoOrigem;
	}
	
	public void setEstadoOrigem(int estadoOrigem) {
		this.estadoOrigem = estadoOrigem;
	}
	
	public int getEstadoFim() {
		return estadoFim;
	}
	
	public void setEstadoFim(int estadoFim) {
		this.estadoFim = estadoFim;
	}
	
	public char getSimbolo() {
		return simbolo;
	}
	
	public void setSimbolo(char simbolo) {
		this.simbolo = simbolo;
	}
	
	@Override
	public String toString() {
		return "δ(q" + getEstadoOrigem() + ", " + getSimbolo() +
				") = q" + getEstadoFim();
	}
}
