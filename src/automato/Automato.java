package automato;

import java.util.ArrayList;
import java.util.Collections;

public class Automato<T> {
	private ArrayList<Integer> estados;
	private ArrayList<int[]> estadosComplexos;
	private ArrayList<Boolean> finais;
	private ArrayList<T> alfabeto;
	private ArrayList<RegraDeProducao<T>> regras;
	
	private int estadoInicial;
	
	public Automato() {
		this.estados = new ArrayList<Integer>();
		this.estadosComplexos = new ArrayList<int[]>();
		this.finais = new ArrayList<Boolean>();
		for (int i = 0; i < estados.size(); i++) {
			finais.add(false);
		}
		this.alfabeto = new ArrayList<T>();
		this.regras = new ArrayList<RegraDeProducao<T>>();
		this.estadoInicial = -1;
	}
	
	public Automato(ArrayList<Integer> estados, ArrayList<T> alfabeto, ArrayList<RegraDeProducao<T>> regras, int estadoInicial) {
		this.estados = estados;
		this.estadosComplexos = new ArrayList<int[]>();
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
	
	public ArrayList<int[]> getEstadosComplexos() {
		return estadosComplexos;
	}
	
	public void setEstadosComplexos(ArrayList<int[]> estadosComplexos) {
		this.estadosComplexos = estadosComplexos;
	}
	
	public void adicionarEstado(int e) {
		estados.add(e);
		finais.add(false);
		estadosComplexos.add(new int[]{e});
	}
	
	public void adicionarEstadoComplexo(int e, int[] componentes) {
		estados.add(e);
		finais.add(false);
		for (int componente : componentes) {
			if (getFinais().get(componente)) {
				finais.set(e, true);
				//se algum dos componentes for estado final, este estado complexo será também
			}
		}
		estadosComplexos.add(componentes);
		ArrayList<RegraDeProducao<T>> novasRegras = new ArrayList<RegraDeProducao<T>>();
		for (int componente : componentes) {
			for (RegraDeProducao<T> regra : getRegras()) {
				if (regra.getEstadoOrigem() == componente) {
					try {
						novasRegras.add(new RegraDeProducao<T>(e, regra.getEstadoFim(), regra.getSimbolo()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		for (RegraDeProducao<T> novaRegra : novasRegras) {
			regras.add(novaRegra);
		}
	}
	
	public ArrayList<Boolean> getFinais() {
		return finais;
	}

	public void setFinais(ArrayList<Boolean> finais) {
		this.finais = finais;
	}

	public ArrayList<T> getAlfabeto() {
		return alfabeto;
	}
	
	public void setAlfabeto(ArrayList<T> alfabeto) {
		this.alfabeto = alfabeto;
	}
	
	public ArrayList<RegraDeProducao<T>> getRegras() {
		return regras;
	}
	
	public void setRegras(ArrayList<RegraDeProducao<T>> regras) {
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
			estadosComplexos.add(new int[]{i}); //recebe um array com apenas o número do estado dentro - estado 0 tem [0] 
			//estados complexos podem ser: estado 4, com [1,3] dentro de estadosComplexos
			finais.add(false);
		}
	}

	public void adicionaRegra(RegraDeProducao<T> regraDeProducao) {
		this.regras.add(regraDeProducao);
	}
	
	public void adicionaRegra(int origem, int destino, T conteudo) throws Exception {
		int estadoDeOrigem = -1, estadoDeDestino = -1;
		T novoSimbolo = null;
		boolean semSimbolo = true;
		for (int e : this.getEstados()) {
			if (e == origem)
				estadoDeOrigem = e;
			if (e == destino)
				estadoDeDestino = e;
		}
		for (T s : alfabeto) {
			if (s.equals(conteudo)) {
				novoSimbolo = s;
				semSimbolo = false;
			}
		}
		if (estadoDeOrigem == -1 || estadoDeDestino == -1)
			throw new Exception("estados não existem neste autômato");
		if (semSimbolo)
			throw new Exception("símbolo " + conteudo + " não faz parte do alfabeto");
		this.regras.add(new RegraDeProducao<T>(estadoDeOrigem, estadoDeDestino, novoSimbolo));
	}
	
	public void executar(String cadeia) {
		System.out.println("Testando cadeia " + cadeia + " com o autômato\n" + this.toString());
		int estado = estadoInicial;
		boolean falha = false; //indica se o autômato leu um símbolo sem estado de destino
		for (int j = 0; j < cadeia.length(); j++) {
			char esteSimbolo = cadeia.charAt(j);
			RegraDeProducao<T> regraAtivada = new RegraDeProducao<T>();
			int regra = -1;
			for (int i = 0; i < regras.size(); i++) {
				if (regras.get(i).getEstadoOrigem() == estado && regras.get(i).getSimbolo().equals(esteSimbolo)) {
					regra = i;
				}
			}
			if(regra >= 0) {
				regraAtivada = regras.get(regra);
				estado = regraAtivada.getEstadoFim();
				System.out.println(regraAtivada);
			} else {
				System.out.println("Entrada inválida: " + esteSimbolo + " no estado q" + estado );
				falha = true;
			}
		}
		
		if (!falha && getFinais().get(estado))
			System.out.println(cadeia + " é uma cadeia reconhecida");
		else
			System.out.println(cadeia + " não é uma cadeia reconhecida");
	}
	
	//igual a executar, mas retorna um ArrayList de tokens
	public ArrayList<TokenClassificado> executarTokenizando(String cadeia) {
		System.out.println("Testando e tokenizando cadeia " + cadeia + " com o autômato\n" + this.toString());
		int linha = 1;
		String tokenAtual = "";
		ArrayList<TokenClassificado> resultado = new ArrayList<TokenClassificado>();
		int estado = estadoInicial;
		boolean falha = false; //indica se o autômato leu um símbolo sem estado de destino
		for (int j = 0; j < cadeia.length(); j++) {
			char esteSimbolo = Character.toLowerCase(cadeia.charAt(j));
			RegraDeProducao<T> regraAtivada = new RegraDeProducao<T>();
			int regra = -1;
			for (int i = 0; i < regras.size(); i++) {
				if (regras.get(i).getEstadoOrigem() == estado && regras.get(i).getSimbolo().equals(esteSimbolo)) {
					regra = i;
				}
			}
			if(regra >= 0) {
				regraAtivada = regras.get(regra);
				estado = regraAtivada.getEstadoFim();
				System.out.println(regraAtivada);
				if(regraAtivada.getEstadoFim() != regraAtivada.getEstadoOrigem() &&
						!getFinais().get(regraAtivada.getEstadoOrigem())) {
					resultado.add(new TokenClassificado(tokenAtual, regraAtivada.getEstadoOrigem()));
					tokenAtual = "";
				}
				tokenAtual += esteSimbolo;
				
				//se chegamos num estado final, q0 no analisador léxico de LMS, adicionar o token reconstruído
				//atual à lista de tokens
				
			} else {
				System.out.println("Linha " + linha + ": entrada inválida: " + escapar(esteSimbolo) + " no estado q" + estado);
				falha = true;
			}
			if (cadeia.charAt(j) == '\n') {
				linha++;
				System.out.println("linha: " + linha);
			}
		}
		
		System.out.println();
		if (!falha && getFinais().get(estado))
			System.out.println(cadeia + " é uma cadeia reconhecida");
		else
			System.out.println(cadeia + " não é uma cadeia reconhecida");
		
		return resultado;
	}
	
	public String escapar (char c) {
		if (c == '\n')
			return "\\n";
		if (c == '\t')
			return "\\t";
		return ""+c;
	}
	
	public ArrayList<Boolean> getEstadosFinais() {
		return finais;
	}
	
	//complexo demais
	public void determinizar() {
		if (ehDeterministico()) {
			System.out.println("Autômato já é determinístico");
			return;
		}
		
		int indice = 0;
		while (indice < getEstados().size()) {
			//existem estados depois desse se ainda faltam alguns a serem percorridos, ou se acabamos de criar um novo
			int esteEstado = getEstados().get(indice);
			for (int iSimb = 0; iSimb < getAlfabeto().size(); iSimb++) {
				boolean[] destinos = new boolean[getEstados().size()];
				for (int i = 0; i <  destinos.length; i++) {
					destinos[i] = false;
				}
				for (int iRegra = 0; iRegra < getRegras().size(); iRegra++) {
					RegraDeProducao<T> estaRegra = getRegras().get(iRegra);
					if (estaRegra.getEstadoOrigem() == esteEstado && estaRegra.getSimbolo() == getAlfabeto().get(iSimb)) {
						destinos[estaRegra.getEstadoFim()] = true;
					}
					
				}
				int qtEstadosComponentes = 0;
				for (boolean b : destinos) {
					if (b) {
						qtEstadosComponentes++;
					}
				}
				int[] componentes = new int[qtEstadosComponentes];
				int indiceDestino = 0;
				for (int i = 0; i < destinos.length; i++) {
					if (destinos[i]) {
						componentes[indiceDestino] = i;
						indiceDestino++;
					}
				}

				if (indiceDestino > 1) {
					String saida = "";
					for (int i : componentes) {
						saida += i + ",";
					}
					System.out.println(saida);
					//criar um novo estado, a não ser que já exista
					if (!existeEstado(getEstados().size(), componentes)) {
						adicionarEstadoComplexo(getEstados().size(), componentes);
					}
					
				}
			}
			indice++;
		}
		
		System.out.println(this);
//		for (RegraDeProducao r : regras) {
//			System.out.println(r);
//		}
		
		//agora que temos os nomes dos novos estados, limpar regras que causam não determinismo
		for (int i = 0; i < getEstados().size(); i++) {
			for (int j = 0; j < getAlfabeto().size(); j++) {
				//remover todas as regras com este estado i de origem e este símbolo j, a não ser que exista apenas 1 (ou 0)
				boolean[] aRemover = new boolean[getRegras().size()];
				for (int k = 0; k <  aRemover.length; k++) {
					aRemover[k] = false;
				}
				int qtARemover = 0;
				for (int k = 0; k < getRegras().size(); k++) {
					int esteEstado = getEstados().get(i);
					T esteSimbolo = getAlfabeto().get(j);
					RegraDeProducao<T> estaRegra = getRegras().get(k);
					if (estaRegra.getEstadoOrigem() == esteEstado && estaRegra.getSimbolo() == esteSimbolo) {
						aRemover[k] = true;
						qtARemover++;
					}
				}
				if (qtARemover > 1) {
					ArrayList<RegraDeProducao<T>> regrasARemover = new ArrayList<RegraDeProducao<T>>();
					for (int k = 0; k < getRegras().size(); k++) {
						if (aRemover[k]) {
							regrasARemover.add(getRegras().get(k));
						}
					}
					//coletar quais os estados componentes do estado que iremos adicionar
					//como destino para o estado i e símbolo j
					int[] componentesDoEstadoAReadicionar = new int[regrasARemover.size()];
					for (int k = 0; k < regrasARemover.size(); k++) {
						componentesDoEstadoAReadicionar[k] = regrasARemover.get(k).getEstadoFim();
					}
					
					//está removendo com sucesso, basta reincluir uma regra que adicione o destino equivalente à união
					//de destinos que havia antes
					getRegras().removeAll(regrasARemover);
					int estadoDestinoDaNovaRegra = encontrarEstadoComplexo(componentesDoEstadoAReadicionar);
					getRegras().add(new RegraDeProducao<T>(getEstados().get(i), estadoDestinoDaNovaRegra, getAlfabeto().get(j)));
				}
			}
		}
	}
	
	public int encontrarEstadoComplexo(int[] componentes) {
		//System.out.println(estado + "x" + mostraLista(componentes));
		for(int e = 0; e < getEstados().size(); e++) {
			int componentesIguais = 0;
			for(int f = 0; f < getEstadosComplexos().get(e).length; f++) {
				for(int g = 0; g < componentes.length; g++) {
					if (componentes[g] == getEstadosComplexos().get(e)[f]) {
						componentesIguais++;
					}
				}
			}
			if (componentesIguais == componentes.length) {
				return getEstados().get(e);
			}
		}
		return -1;	
	}

	public String mostraLista(int[] lista) {
		String saida = "(";
		for (int i : lista) {
			saida += i + ",";
		}
		return saida + ")";
	}
	
	public boolean existeEstado(int estado, int[] componentes) {
		//System.out.println(estado + "x" + mostraLista(componentes));
		for(int e = 0; e < getEstados().size(); e++) {
			int componentesIguais = 0;
			for(int f = 0; f < getEstadosComplexos().get(e).length; f++) {
				for(int g = 0; g < componentes.length; g++) {
					if (componentes[g] == getEstadosComplexos().get(e)[f]) {
						componentesIguais++;
					}
				}
			}
			if (componentesIguais == componentes.length) {
				return true;
			}
		}
		return false;	
	}
	
	public int[] uniao(int[] a, int[] b) {
		int[] resultado = new int[a.length + b.length];
		int contador = 0;
		boolean[] contidoEmA = new boolean[getEstados().size()];
		boolean[] contidoEmB = new boolean[getEstados().size()];
		for (int i = 0; i < getEstados().size(); i++) {
			for (int j = 0; j < a.length; j++) {
				if (a[j] == i) {
					contidoEmA[i] = true;
				}
			}
			for (int j = 0; j < b.length; j++) {
				if (b[j] == i) {
					contidoEmB[i] = true;
				}
			}
			if (contidoEmA[i] || contidoEmB[i]) {
				resultado[contador] = i;
				contador++;
			}
		}
		return resultado;
	}
	
	@Override
	public String toString() {
		String stringEstados = "";
		if(getEstados().size() > 0) {
			for (int e : getEstados()) {
				stringEstados += "q" + e + ", ";
			}
			stringEstados = stringEstados.substring(0, stringEstados.length() - 2);
		}
		
		String stringSimbolos = "";
		for (T simbolo: getAlfabeto()) {
			stringSimbolos += simbolo + ", ";
		}
		stringSimbolos = stringSimbolos.substring(0, stringSimbolos.length() - 2);
		
		String listaFinais = "";
		int contagemEstadosFinais = 0;
		for (boolean b : getEstadosFinais()) {
			if (b) {
				contagemEstadosFinais++;
			}
		}
		if (contagemEstadosFinais > 0) {
			//System.out.println(getEstadosFinais().size());
			for (int e = 0; e < getEstadosFinais().size(); e++) {
				if(getEstadosFinais().get(e)) {
					listaFinais += "q" + e + ", ";
				}
			}
			listaFinais = listaFinais.substring(0, listaFinais.length() - 2);
		} else {
			listaFinais = "{}";
		}
			
		String saida = "L = {{" + stringEstados + "}, {" + stringSimbolos + "}, δ, q" + getEstadoInicial() + ", {" + listaFinais + "}}";
		String delta = "               δ |";
		for (T simbolo : getAlfabeto()) {
			String simboloNaColuna = " " + simbolo + " ";
			if (simbolo.toString().equals("\t"))
				simboloNaColuna = " \\t";
			if (simbolo.toString().equals("\n"))
				simboloNaColuna = " \\n";
			delta += "       " + simboloNaColuna + "        |";
		}
		for (int e : getEstados()) {
			delta += "\n" + String.format("%16s", representar(e)) + " |";
			for (T s : getAlfabeto()) {
				boolean[] destinos = new boolean[getEstados().size()];
				for (int i = 0; i <  destinos.length; i++) {
					destinos[i] = false;
				}
				for (int iRegra = 0; iRegra < getRegras().size(); iRegra++) {
					RegraDeProducao<T> estaRegra = getRegras().get(iRegra);
					if (estaRegra.getEstadoOrigem() == e && estaRegra.getSimbolo() == s) {
						destinos[estaRegra.getEstadoFim()] = true;
					}
				}
				ArrayList<Integer> estaCelula = new ArrayList<Integer>();
				for (int i = 0; i < destinos.length; i++) {
					if (destinos[i]) {
						estaCelula.add(i);
					}
				}
//				for (int f: getEstadosComplexos().get(e)) {
//					
//					for (RegraDeProducao regra : getRegras()) {
//						if (regra.getEstadoOrigem() == f && regra.getSimbolo() == s)
//							estaCelula.add(regra.getEstadoFim());
//					}
//				}
				if (estaCelula.size() == 0)
					estaCelula.add(-1); //representa λ, ausência de estado válido
				delta += centralizar(18, estaCelula) + "|";
			}
			delta += " (q" + e + ")";
		}
		
		return saida + "\n" + delta + "\n";
	}
	
	public String representar(int estado) {
		String resultado = "";
		for (int parte : getEstadosComplexos().get(estado)) {
			resultado += "q" + parte + (getFinais().get(parte) ? "*":"");
		}
		return resultado;
	}
	
	public String centralizar(int tamanho, ArrayList<Integer> s) {
		String saida = "";
		if (s.get(0) == -1) {
			saida += "λ";
		} else {
			Collections.sort(s);
			for (int i: s) {
				saida += "q" + i + (getFinais().get(i) ? "*":"");
			}
		}
		String espaco = "";
		for (int i = 0; i < (tamanho - saida.length())/2; i++) {
			espaco += " ";
		}
		return espaco + saida + ((tamanho - saida.length())%2 == 0 ? "":" ") + espaco;
	}
	
	public boolean ehDeterministico() {
		//int[][] controle = new int[estados.size()][estados.size()];
		for (int i = 0; i < getRegras().size() - 1; i++) {
			for (int j = i + 1; j < getRegras().size(); j++) {
				if (getRegras().get(i).getEstadoOrigem() == getRegras().get(j).getEstadoOrigem() &&
						getRegras().get(i).getSimbolo() == getRegras().get(j).getSimbolo()) {
					return false;
				}
			}
		}
		return true;
	}
}
