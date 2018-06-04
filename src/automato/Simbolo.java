package automato;

public class Simbolo<T> {
	private T conteudo;

	
	
	@Override
	public String toString() {
		return conteudo.toString();
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Simbolo<T> other = (Simbolo<T>) obj;
		if (conteudo == null) {
			if (other.conteudo != null)
				return false;
		} else if (!conteudo.equals(other.conteudo))
			return false;
		return true;
	}
	
	
}
