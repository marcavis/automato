package automato;

import java.util.ArrayList;
import java.util.Collections;

public class Automato {
	private ArrayList<Integer> estados;
	private ArrayList<Boolean> finais;
	private ArrayList<Character> alfabeto;
	private ArrayList<RegraDeProducao> regras;
	
	private int estadoInicial;
	
	public Automato() {
		this.estados = new ArrayList<Integer>();
		this.finais = new ArrayList<Boolean>();
		for (int i = 0; i < estados.size(); i++) {
			finais.add(false);
		}
		this.alfabeto = new ArrayList<Character>();
		this.regras = new ArrayList<RegraDeProducao>();
		this.estadoInicial = -1;
	}
	
	public Automato(ArrayList<Integer> estados, ArrayList<Character> alfabeto, ArrayList<RegraDeProducao> regras, int estadoInicial) {
		this.estados = estados;
		this.finais = new ArrayList<Boolean>();
		for (int i = 0; i < estados.size(); i++) {
			finais.add(false);
		}
		this.alfabeto = alfabeto;
		this.regras = regras;
		this.estadoInicial = estadoInicial;
	}

	public ArrayList<Integer> getEstados() {
		return estados;
	}
	
	public void setEstados(ArrayList<Integer> estados) {
		this.estados = estados;
	}
	
	public ArrayList<Boolean> getFinais() {
		return finais;
	}

	public void setFinais(ArrayList<Boolean> finais) {
		this.finais = finais;
	}

	public ArrayList<Character> getAlfabeto() {
		return alfabeto;
	}
	
	public void setAlfabeto(ArrayList<Character> alfabeto) {
		this.alfabeto = alfabeto;
	}
	
	public ArrayList<RegraDeProducao> getRegras() {
		return regras;
	}
	
	public void setRegras(ArrayList<RegraDeProducao> regras) {
		this.regras = regras;
	}
	
	public int getEstadoInicial() {
		return estadoInicial;
	}
	
	public void setEstadoInicial(int estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	
	public void setFinal(int estado, boolean seFinal) {
		this.finais.set(estado, seFinal);
	}
	
	public void criaEstadosIniciais(int quantidade) {
		this.estados = new ArrayList<Integer>();
		for (int i = 0; i < quantidade; i++) {
			estados.add(i);
			finais.add(false);
		}
	}

	public void adicionaRegra(RegraDeProducao regraDeProducao) {
		this.regras.add(regraDeProducao);
	}
	
	public void adicionaRegra(int origem, int destino, char conteudo) throws Exception {
		int estadoDeOrigem = -1, estadoDeDestino = -1;
		char novoSimbolo = ' ';
		boolean semSimbolo = true;
		for (int e : this.getEstados()) {
			if (e == origem)
				estadoDeOrigem = e;
			if (e == destino)
				estadoDeDestino = e;
		}
		for (char s : alfabeto) {
			if (s == conteudo) {
				novoSimbolo = s;
				semSimbolo = false;
			}
		}
		if (estadoDeOrigem == -1 || estadoDeDestino == -1)
			throw new Exception("estados não existem neste autômato");
		if (semSimbolo)
			throw new Exception("símbolo não faz parte do alfabeto");
		this.regras.add(new RegraDeProducao(estadoDeOrigem, estadoDeDestino, novoSimbolo));
	}
	
	public void executar(String cadeia) {
		System.out.println("Testando cadeia " + cadeia + " com o autômato\n" + this.toString());
		int estado = estadoInicial;
		boolean falha = false; //indica se o autômato leu um símbolo sem estado de destino
		for (int j = 0; j < cadeia.length(); j++) {
			char esteSimbolo = cadeia.charAt(j);
			RegraDeProducao regraAtivada = new RegraDeProducao();
			int regra = -1;
			for (int i = 0; i < regras.size(); i++) {
				if (regras.get(i).getEstadoOrigem() == estado && regras.get(i).getSimbolo() == esteSimbolo) {
					regra = i;
				}
			}
			if(regra >= 0) {
				regraAtivada = regras.get(regra);
				estado = regraAtivada.getEstadoFim();
				System.out.println(regraAtivada);
			} else {
				System.out.println("Entrada inválida: " + esteSimbolo + " no estado " + estado);
				falha = true;
			}
		}
		
		//trocar para getFinal()
		System.out.println(getFinais());
		if (!falha && getFinais().get(estado))
			System.out.println(cadeia + " é uma cadeia reconhecida");
		else
			System.out.println(cadeia + " não é uma cadeia reconhecida");
	}
	
	public ArrayList<Boolean> getEstadosFinais() {
		return finais;
	}
	
	@Override
	public String toString() {
		String stringEstados = "";
		for (int e : getEstados()) {
			stringEstados += "q" + e + ", ";
		}
		stringEstados = stringEstados.substring(0, stringEstados.length() - 2);
		
		String stringSimbolos = "";
		for (char simbolo: getAlfabeto()) {
			stringSimbolos += simbolo + ", ";
		}
		stringSimbolos = stringSimbolos.substring(0, stringSimbolos.length() - 2);
		
		String listaFinais = "";
		if (getEstadosFinais().size() > 0) {
			for (int e = 0; e < getEstadosFinais().size(); e++) {
				if(getEstadosFinais().get(e)) {
					listaFinais += "q" + e + ", ";
				}
			}
			listaFinais = listaFinais.substring(0, listaFinais.length() - 2);
		} else {
			listaFinais = "{}";
		}
			
		String saida = "L = {{" + stringEstados + "}, {" + stringSimbolos + "}, δ, " + getEstadoInicial() + ", {" + listaFinais + "}}";
		String delta = "δ      |";
		for (char simbolo : getAlfabeto()) {
			delta += "      " + simbolo + "      |";
		}
		for (int e : getEstados()) {
			delta += "\n" + String.format("%6s", "q" + e + (getFinais().get(e) ? "*":"")) + " |";
			for (char s : getAlfabeto()) {
				ArrayList<Integer> estaCelula = new ArrayList<Integer>();
				for (RegraDeProducao regra : getRegras()) {
					if (regra.getEstadoOrigem() == e && regra.getSimbolo() == s)
						estaCelula.add(regra.getEstadoFim());
				}
				if (estaCelula.size() == 0)
					estaCelula.add(-1); //representa λ, ausência de estado válido
				delta += centralizar(13, estaCelula) + "|";
			}
		}
		return saida + "\n" + delta + "\n";
	}
	
	public String centralizar(int tamanho, ArrayList<Integer> s) {
		String saida = "";
		if (s.get(0) == -1) {
			saida += "λ";
		} else {
			Collections.sort(s);;
			for (int i: s) {
				saida += "q" + i + (getFinais().get(i) ? "*":"");
			}
		}
		String espaco = "";
		for (int i = 0; i < (tamanho - saida.length())/2; i++) {
			espaco += " ";
		}
		return espaco + saida + ((tamanho - s.size())%2 == 0 ? "":" ") + espaco;
	}
	
	public String tipoDeAutomato() {
		//int[][] controle = new int[estados.size()][estados.size()];
		for (int i = 0; i < getRegras().size() - 1; i++) {
			for (int j = i + 1; j < getRegras().size(); j++) {
				if (getRegras().get(i).getEstadoOrigem() == getRegras().get(j).getEstadoOrigem() &&
						getRegras().get(i).getSimbolo() == getRegras().get(j).getSimbolo()) {
					return "AFND";
				}
					
			}
		}
		return "AFD";
	}
}
