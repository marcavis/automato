package automato;

public class RegraDeProducao<T> {
	private int estadoOrigem;
	private int estadoFim;
	private T simbolo;
	
	public RegraDeProducao() {
	}
	
	public RegraDeProducao(int estadoOrigem, int estadoFim, T simbolo) {
		this.estadoOrigem = estadoOrigem;
		this.estadoFim = estadoFim;
		this.simbolo = simbolo;
	}
	
	public boolean equals(RegraDeProducao<T> regra) {
		return regra.getEstadoOrigem() == estadoOrigem &&
				regra.getEstadoFim() == estadoFim &&
				regra.getSimbolo().equals(simbolo);
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
	
	public T getSimbolo() {
		return simbolo;
	}
	
	public void setSimbolo(T simbolo) {
		this.simbolo = simbolo;
	}
	
	public String getSimboloEscapado() {
		if (getSimbolo().toString().charAt(0) == '\t')
			return "\\t";
		if (getSimbolo().toString().charAt(0) == '\n')
			return "\\n";
		return getSimbolo().toString();
	}
	
	@Override
	public String toString() {
		return "δ(q" + getEstadoOrigem() + ", " + getSimboloEscapado() +
				") = q" + getEstadoFim();
	}
}
