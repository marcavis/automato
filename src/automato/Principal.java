package automato;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class Principal {
	static char[] letras = new char[] {'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	static char[] digitos = new char[] {'0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9'};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//rodaExemplo1();
		rodaExemplo2();
	}
	
	public static void rodaExemplo2() {
		//começando um teste do automato que precisaremos
		Automato<Character> exemplo = new Automato<Character>();
		ArrayList<Character> simbolos = new ArrayList<Character>();
		
		//Character[] tipos = Character.values();
		for (char c : letras) {
			simbolos.add(c);
		}
		for (char c : digitos) {
			simbolos.add(c);
		}
		simbolos.add(' ');
		simbolos.add('\n');
		simbolos.add('\t');
		simbolos.add('(');
		simbolos.add('*');
		simbolos.add(')');
		simbolos.add(',');
		simbolos.add(';');
		simbolos.add('.');
		simbolos.add('[');
		simbolos.add(']');
		simbolos.add(':');
		simbolos.add('=');
		simbolos.add('"');
		simbolos.add('+');
		simbolos.add('-');
		simbolos.add('/');
		simbolos.add('>');
		simbolos.add('<');
		simbolos.add('–');
		exemplo.setAlfabeto(simbolos);
		
		exemplo.criaEstadosIniciais(21);
		exemplo.setEstadoInicial(0);
		exemplo.setFinal(0, true);
		try {
			//estado 0 - pronto para um novo token
			//estado 1 - em um identificador
			//estado 2 - em um número
			//estado 3 - em um símbolo especial
			
			adicionarVariasRegras(exemplo, 0, 1, TokenLMS.CHAR); //adiciona todas as regras que vão do estado 0 a 1 com uma letra
			adicionarVariasRegras(exemplo, 1, 1, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 1, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 1, 0, TokenLMS.ANYSPACE);
			exemplo.adicionaRegra(1, 5, ':');
			exemplo.adicionaRegra(1, 9, '('); //parênteses podem aparecer entre dois identificadores, sem problema

			
			adicionarVariasRegras(exemplo, 0, 2, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 2, 2, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 2, 0, TokenLMS.ANYSPACE);
			
			exemplo.adicionaRegra(0, 9, '(');
			adicionarVariasRegras(exemplo, 9, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 9, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 9, 2, TokenLMS.DIGIT);
			exemplo.adicionaRegra(9, 15, '*'); //entramos num comentário
			adicionarVariasRegras(exemplo, 15, 15, TokenLMS.DIGIT); //apenas asterisco começa a tirar do comentário
			adicionarVariasRegras(exemplo, 15, 15, TokenLMS.CHAR); //ainda falta botar um adicionarVariasRegras pra caracteresEspeciais
			adicionarVariasRegras(exemplo, 15, 15, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 15, 15, TokenLMS.ANY_SYMBOL_NONASTERISK);
			exemplo.adicionaRegra(15, 16, '*'); //estamos para sair de um comentário
			exemplo.adicionaRegra(16, 16, '*'); //não faz mal ter uma sequência maior de asteriscos antes de fechar o comentário
			exemplo.adicionaRegra(16, 17, ')'); //saímos
			adicionarVariasRegras(exemplo, 17, 0, TokenLMS.ANYSPACE);
			
			exemplo.adicionaRegra(0, 8, '"');
			adicionarVariasRegras(exemplo, 8, 8, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 8, 8, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 8, 8, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 8, 8, TokenLMS.ANY_SYMBOL_NONQUOTE);
			exemplo.adicionaRegra(8, 14, '"');
			exemplo.adicionaRegra(9, 8, '"');
			exemplo.adicionaRegra(14, 3, ')');
			exemplo.adicionaRegra(14, 3, ',');
			exemplo.adicionaRegra(14, 3, '[');
			exemplo.adicionaRegra(14, 3, ';');
			exemplo.adicionaRegra(14, 3, ']');
			
			exemplo.adicionaRegra(0, 3, ')');
			exemplo.adicionaRegra(0, 3, ',');
			exemplo.adicionaRegra(0, 3, '[');
			exemplo.adicionaRegra(0, 3, ';');
			exemplo.adicionaRegra(0, 3, ']');
			exemplo.adicionaRegra(1, 3, ')');
			exemplo.adicionaRegra(1, 3, ',');
			exemplo.adicionaRegra(1, 3, '[');
			exemplo.adicionaRegra(1, 3, ';');
			exemplo.adicionaRegra(1, 3, ']');
			exemplo.adicionaRegra(2, 3, ')');
			exemplo.adicionaRegra(2, 3, ',');
			exemplo.adicionaRegra(2, 3, '[');
			exemplo.adicionaRegra(2, 3, ';');
			exemplo.adicionaRegra(2, 3, ']');
			exemplo.adicionaRegra(3, 3, ')');
			exemplo.adicionaRegra(3, 3, ',');
			exemplo.adicionaRegra(3, 3, '[');
			exemplo.adicionaRegra(3, 3, ';');
			exemplo.adicionaRegra(3, 3, ']');

			exemplo.adicionaRegra(3, 9, '(');
			adicionarVariasRegras(exemplo, 3, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 3, 1, TokenLMS.CHAR); //podemos começar um novo identificador após um caractere especial
			adicionarVariasRegras(exemplo, 3, 2, TokenLMS.DIGIT);
			
			exemplo.adicionaRegra(0, 5, ':');
			exemplo.adicionaRegra(5, 18, '='); //como trabalhar quando achamos um token mas não temos um espaço delimitador?
			
			adicionarVariasRegras(exemplo, 18, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 18, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 18, 2, TokenLMS.DIGIT);
			
			//operadores
			exemplo.adicionaRegra(0, 20, '+');
			exemplo.adicionaRegra(0, 20, '-');
			exemplo.adicionaRegra(0, 20, '/');
			exemplo.adicionaRegra(0, 20, '*');
			exemplo.adicionaRegra(0, 20, '=');
			exemplo.adicionaRegra(1, 20, '+');
			exemplo.adicionaRegra(1, 20, '-');
			exemplo.adicionaRegra(1, 20, '/');
			exemplo.adicionaRegra(1, 20, '*');
			exemplo.adicionaRegra(1, 20, '=');
			exemplo.adicionaRegra(2, 20, '+');
			exemplo.adicionaRegra(2, 20, '-');
			exemplo.adicionaRegra(2, 20, '/');
			exemplo.adicionaRegra(2, 20, '*');
			exemplo.adicionaRegra(2, 20, '=');
			adicionarVariasRegras(exemplo, 20, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 20, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 20, 2, TokenLMS.DIGIT);
			
			//operadores de potencialmente mais de um caracter
			exemplo.adicionaRegra(0, 6, '<');
			exemplo.adicionaRegra(1, 6, '<');
			exemplo.adicionaRegra(2, 6, '<');
			exemplo.adicionaRegra(6, 11, '=');
			exemplo.adicionaRegra(6, 12, '>');
			exemplo.adicionaRegra(0, 7, '>');
			exemplo.adicionaRegra(1, 7, '>');
			exemplo.adicionaRegra(2, 7, '>');
			exemplo.adicionaRegra(7, 11, '=');
			exemplo.adicionaRegra(7, 13, '<');
			adicionarVariasRegras(exemplo, 6, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 6, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 6, 2, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 7, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 7, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 7, 2, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 11, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 11, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 11, 2, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 12, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 12, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 12, 2, TokenLMS.DIGIT);
			adicionarVariasRegras(exemplo, 13, 0, TokenLMS.ANYSPACE);
			adicionarVariasRegras(exemplo, 13, 1, TokenLMS.CHAR);
			adicionarVariasRegras(exemplo, 13, 2, TokenLMS.DIGIT);
			
			//o que o ponto faz? hmm
			exemplo.adicionaRegra(0, 4, '.');
			exemplo.adicionaRegra(1, 4, '.');
			exemplo.adicionaRegra(4, 10, '.');
			adicionarVariasRegras(exemplo, 4, 0, TokenLMS.ANYSPACE);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String programa = "";
		try {
			FileReader fr = new FileReader(new File("src/automato/programa.lms"));
			BufferedReader br = new BufferedReader(fr);
			String linha;
			while((linha=br.readLine())!=null) {
				programa += linha + "\n";
			}
			br.close();
			fr.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		ArrayList<TokenClassificado> listaDeTokens = exemplo.executarTokenizando(programa); 
		System.out.println("\nTokens Encontrados\n");
		for (TokenClassificado token : listaDeTokens) {
			System.out.println(String.format("%30s", token.getToken().trim()) + " | " + tipoDeToken(token));
		}
		System.out.println(exemplo.ehDeterministico());
	}
	
	public static String tipoDeToken(TokenClassificado token) {
		String tipo = "";
		int estado = token.getUltimoEstado();
		switch (estado) {
			case 1:
				//também pode ser palavra reservada
				String[] reservadas = new String[] {"and", "begin",
						"call", "case", "const", "do", "else", "end",
						"for", "if", "integer", "not", "of", "or",
						"procedure", "program", "readln", "repeat",
						"then", "to", "until", "var", "while", "writeln"};
				tipo = "identificador";
				for (String string : reservadas) {
					if (string.equals(token.getToken().trim())) {
						tipo = "palavra reservada";
					}
				}
				break;
			case 2:
				tipo = "número";
				break;
			case 3:
				tipo = "símbolo especial";
				break;
			case 4:
				tipo = "ponto final";
				break;
			case 5:
				tipo = "dois pontos";
				break;
			case 6:
				tipo = "sinal de menor";
				break;
			case 7:
				tipo = "sinal de maior";
				break;
			case 8:
				tipo = "início de literal";
				break;
			case 9:
				tipo = "abre parênteses";
				break;
			case 11:
				tipo = "sinais de menor-ou-igual ou maior-ou-igual";
				break;
			case 12:
			case 13:
				tipo = "sinal de diferença";
				break;
			case 14:
				tipo = "fim de literal";
				break;
			case 15:
				tipo = "começo de comentário";
				break;
			case 16:
				tipo = "fim de comentário";
				break;
			case 17:
				tipo = "fecha parênteses";
				break;
			case 18:
				tipo = "sinal de atribuição";
				break;
			case 20:
				tipo = "operador matemático";
				break;
			default:
				tipo = "desconhecido";
				break;
		}
		return tipo;
	}
	
	public static String tirarEspacos(String origem) {
		String resultado = "";
		for (char c : origem.toCharArray()) 
			if(c != ' ' && c != '\n' && c != '\t')
				resultado += c;
		return resultado;
	}
	
	public static void adicionarVariasRegras(Automato<Character> automato, int origem, int dest, TokenLMS t) throws Exception {
		if (t == TokenLMS.CHAR) {
			for (char c : letras) {
				automato.adicionaRegra(origem, dest, c);
			}
		} else if (t == TokenLMS.DIGIT) {
			for (char c : digitos) {
				automato.adicionaRegra(origem, dest, c);
			}
		} else if (t == TokenLMS.ANYSPACE) {
			automato.adicionaRegra(origem, dest, ' ');
			automato.adicionaRegra(origem, dest, '\n');
			automato.adicionaRegra(origem, dest, '\t');
			//System.out.println(automato.getRegras().size());
		} else if (t == TokenLMS.ANY_SYMBOL_NONASTERISK) {
			automato.adicionaRegra(origem, dest, '(');
			automato.adicionaRegra(origem, dest, ')');
			automato.adicionaRegra(origem, dest, '.');
			automato.adicionaRegra(origem, dest, ':');
			automato.adicionaRegra(origem, dest, '=');
			automato.adicionaRegra(origem, dest, '+');
			automato.adicionaRegra(origem, dest, '-');
			automato.adicionaRegra(origem, dest, '/');
			automato.adicionaRegra(origem, dest, '>');
			automato.adicionaRegra(origem, dest, '<');
			automato.adicionaRegra(origem, dest, '–');
			automato.adicionaRegra(origem, dest, ',');
			automato.adicionaRegra(origem, dest, ';');
			automato.adicionaRegra(origem, dest, '[');
			automato.adicionaRegra(origem, dest, ']');
			automato.adicionaRegra(origem, dest, '"');
		} else if (t == TokenLMS.ANY_SYMBOL_NONQUOTE) {
			automato.adicionaRegra(origem, dest, '(');
			automato.adicionaRegra(origem, dest, ')');
			automato.adicionaRegra(origem, dest, '.');
			automato.adicionaRegra(origem, dest, ':');
			automato.adicionaRegra(origem, dest, '=');
			automato.adicionaRegra(origem, dest, '+');
			automato.adicionaRegra(origem, dest, '-');
			automato.adicionaRegra(origem, dest, '/');
			automato.adicionaRegra(origem, dest, '>');
			automato.adicionaRegra(origem, dest, '<');
			automato.adicionaRegra(origem, dest, '–');
			automato.adicionaRegra(origem, dest, ',');
			automato.adicionaRegra(origem, dest, ';');
			automato.adicionaRegra(origem, dest, '[');
			automato.adicionaRegra(origem, dest, ']');
			automato.adicionaRegra(origem, dest, '*');
		}
	}
	
	public static void rodaExemplo1() {
		Automato<Character> exemplo = new Automato<Character>();
		ArrayList<Character> simbolos = new ArrayList<Character>();
		
		simbolos.add('0');
		simbolos.add('1');
		
		exemplo.setAlfabeto(simbolos);
		
		exemplo.criaEstadosIniciais(4);
		//ArrayList<RegraDeProducao> regras = new ArrayList<RegraDeProducao>();
		try {
			exemplo.adicionaRegra(0,2,'0');
			exemplo.adicionaRegra(0,0,'1');
			exemplo.adicionaRegra(1,0,'1');
			exemplo.adicionaRegra(1,3,'1');
			exemplo.adicionaRegra(2,1,'0');
			exemplo.adicionaRegra(2,3,'0');
			exemplo.adicionaRegra(3,1,'0');
			exemplo.adicionaRegra(3,2,'1');
			exemplo.setFinal(3, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		exemplo.setEstadoInicial(exemplo.getEstados().get(0));
		//exemplo.executar("0011");
		System.out.println(exemplo);
		exemplo.determinizar();
		System.out.println(exemplo);
	}

}
