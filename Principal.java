package automato;

import java.util.ArrayList;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Automato exemplo = new Automato();
		
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
		exemplo.determinizar();
	}

}
