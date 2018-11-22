package restricciones;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.pb.tools.WeightedObject;
import org.sat4j.specs.ContradictionException;

import constantes.VariablesSistema;


import solvers.PseudoBoolean.otroPB.ProblemDefinition;
import utilidades.filtros.Ifiltro;
import utilidades.filtros.tipoPsicologicos.FiltroActitud;
import utilidades.filtros.tipoPsicologicos.FiltroDecision;
import utilidades.filtros.tipoPsicologicos.FiltroEmocional;
import utilidades.filtros.tipoPsicologicos.FiltroEstiloVida;
import utilidades.filtros.tipoPsicologicos.FiltroIntroversion;
import utilidades.filtros.tipoPsicologicos.FiltroIntuitivo;
import utilidades.filtros.tipoPsicologicos.FiltroPercepcion;
import utilidades.filtros.tipoPsicologicos.FiltroPerceptivo;

/*
 * 
 * Restricciones sobre los perfiles psicologicos. Para cada dicotomia se debe cumplir:
 * xi+...+xj = #usuarios/2
 * xz+...+xt = #usuarios/2
 *
 * En caso de ser una restriccion soft:
 * -Cota(o un numero grande que haga cierta la desigualdad)*xaux + xi +...+ xj <= Cota
 * Fcion objetivo : ...+ Costo * xaux

 */


public class RestriccionesTiposPsicologicos extends RestriccionesNGruposTemplate{

	private int cantIntegrantes;
	
	private enum TIPOS_PSICOLOGICOS { 	EXTROVERTIDO(0),
										INTROVERTIDO(1),
										SENSORIAL(2),
										INTUITIVO(3),
									   	RACIONAL(4),
									   	EMOCIONAL(5),
									   	CALIFICADOR(6),
										PERCEPTIVO(7);
									
										private int posicion;
										TIPOS_PSICOLOGICOS(int p){
											posicion = p;
										}
										
										public int getPosicion() {return posicion;}
									};
		
	private  Ifiltro[] tipoPsicologico = {new FiltroActitud(), new FiltroIntroversion(), new FiltroPercepcion(), 
										new FiltroIntuitivo(), new FiltroDecision(), new FiltroEmocional(),
										new FiltroEstiloVida(), new FiltroPerceptivo()};


	private ArrayList<Usuario[]> usuariosPorTipoPsico = new ArrayList<>();
	
		
	
	public RestriccionesTiposPsicologicos(int cantIntegrantes, int idGrupos, List<Usuario> lusuarios) {

		super(lusuarios, idGrupos);
		this.cantIntegrantes = cantIntegrantes;
		
		this.soft = false;
		this.costo = 0;
	}



	public boolean esPosibleFormarNgrupos(){
		 
		int ajuste = 0;
		if((cantIntegrantes % 2) == 1) ajuste = 2;
		for(int i = TIPOS_PSICOLOGICOS.EXTROVERTIDO.getPosicion(); i <= TIPOS_PSICOLOGICOS.PERCEPTIVO.getPosicion(); i++){
			Usuario[] tipo_psicologico = this.getUsuariosTipoPsicologico(lusuarios, i);
			if( ( (cantIntegrantes/2 + ajuste) * this.cantIntegrantes ) >= tipo_psicologico.length ){
				System.out.println("No se cumple el tipo psicologico " + i);
				return false;
			}
		}
		return true;
		
	}
	
	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper) 
			throws ContradictionException {
		
		this.ejecutada = true;
		int ajuste = 0;
		if((cantIntegrantes % 2) == 1) ajuste = 2;
		
		for(int i = TIPOS_PSICOLOGICOS.EXTROVERTIDO.getPosicion(); i <= TIPOS_PSICOLOGICOS.PERCEPTIVO.getPosicion(); i++){
			Usuario[] tipo_psicologico = this.getUsuariosTipoPsicologico(lusuarios, i);
			int maxCant = cantIntegrantes/2 + ajuste;
			int minCant = cantIntegrantes/2 - ajuste;
			helper.atMost("Max cantidad tipo " + i, maxCant, tipo_psicologico);
			helper.atLeast("Max cantidad tipo " + i, minCant, tipo_psicologico);
		}

	}
	
	
	private Usuario[] getUsuariosTipoPsicologico(List<Usuario> lusr, int p){
		ArrayList<Usuario> resultado = new ArrayList<>();
		for(Usuario u : lusr){
			if(tipoPsicologico[p].cumple(u))
				resultado.add(u);
		}
		Usuario[] usuarios = new Usuario[resultado.size()];
		resultado.toArray(usuarios);
		return usuarios;		
	}


	@Override
	public void escribirRestricciones(ProblemDefinition pd) {
		
		this.ejecutada = true;
		int ajuste = 0;
		if((cantIntegrantes % 2) == 1) ajuste = 2;

		for(int i = TIPOS_PSICOLOGICOS.EXTROVERTIDO.getPosicion(); i <= TIPOS_PSICOLOGICOS.PERCEPTIVO.getPosicion(); i++){
			Usuario[] tipo_psicologico = this.getUsuariosTipoPsicologico(lusuarios, i);
			int maxCant = cantIntegrantes/2 + ajuste;
			int minCant = cantIntegrantes/2 - ajuste;
			pd.agregarRestriccionLinealMayorIgual(tipo_psicologico, minCant);
			pd.agregarRestriccionLinealMenorIgual(tipo_psicologico, maxCant);
		}
	}


	@Override
	public String toString() {
		
		return "";
	}

	public String getUsuarios() {
		
		String restricciones = "";
		for(int i = TIPOS_PSICOLOGICOS.EXTROVERTIDO.getPosicion(); i <= TIPOS_PSICOLOGICOS.PERCEPTIVO.getPosicion(); i++){
			Usuario[] perfil = this.getUsuariosTipoPsicologico(lusuarios, i);
			restricciones += "Tipo psicologioc " + i + "\n";
			for(Usuario u : perfil){
				restricciones += u.toString() + "\n";
			}
			restricciones += "Cantidad: " + perfil.length + " \n\n";
		}
		return restricciones;
	}


	private void calcularUsuariosPorTipoPsico(){
		
		if( this.usuariosPorTipoPsico.isEmpty()){
			for(int i = TIPOS_PSICOLOGICOS.EXTROVERTIDO.getPosicion(); 
					i <= TIPOS_PSICOLOGICOS.PERCEPTIVO.getPosicion(); i++){
				Usuario[] perfil = this.getUsuariosTipoPsicologico(lusuarios, i);
				this.usuariosPorTipoPsico.add(perfil);
			}
		}
	}
	
	public List<Usuario[]> getUsuariosTiposPsico(){
		
		if( this.usuariosPorTipoPsico.isEmpty() ){
			this.calcularUsuariosPorTipoPsico();
		}
		return this.usuariosPorTipoPsico;
	}


	@Override
	public void definirRestriccionesParaNGrupos(
			DependencyHelper<Integer, String> helper)
			throws ContradictionException {

		this.calcularUsuariosPorTipoPsico();
		super.definirRestriccionesParaNGrupos(helper);
		
	}


	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		if(this.idGrupo == id){
			this.ejecutada = true;
			if ( soft ){
				int costo = 0;
				for( Usuario[] perfil : this.usuariosPorTipoPsico){
					int cantUsuarios = 0;
					for( int j = 0; j < perfil.length; j++ ){
						if ( grupo.contains( perfil[ j ] ) )
							cantUsuarios++;
					}
					if ( cantUsuarios > this.cota)
						costo += this.costo;
				}
				return costo;
			}
		}
		return 0;
	}

	@Override
	protected void definirRestriccionSoft( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		int ajuste = 0;
		int i = 0;
		if((cantIntegrantes % 2) == 1) ajuste = 2;
		for( Usuario[] perfil : this.usuariosPorTipoPsico){

			@SuppressWarnings("unchecked") 
			WeightedObject<Integer>[] tipo_psicologico = 
										new WeightedObject[ perfil.length + 1 ];
			Integer auxiliar = VariablesSistema.getVariableAuxiliar();
			tipo_psicologico[0] = WeightedObject.newWO( auxiliar, (-1) * perfil.length );

			for( int j = 1; j < tipo_psicologico.length; j++ ){
				tipo_psicologico[j] = 
						WeightedObject.newWO( (idGrupo * lusuarios.size()) + perfil[j -1 ].getId(), 1 ) ;
			}
			int maxCant = cantIntegrantes/2 + ajuste;
			helper.atMost("Max cantidad tipo " + i, maxCant, tipo_psicologico);
			helper.addToObjectiveFunction(auxiliar, this.costo);
			i++;
		}

	}



	@Override
	protected void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		int ajuste = 0;
		int i = 0;
		if((cantIntegrantes % 2) == 1) ajuste = 2;
		for( Usuario[] perfil : this.usuariosPorTipoPsico){
			Integer[] tipo_psicologico = new Integer[perfil.length];
			for( int j = 0; j < perfil.length; j++ ){
				tipo_psicologico[j] = (idGrupo *lusuarios.size()) + perfil[j].getId();
			}
			int maxCant = cantIntegrantes/2 + ajuste;
			int minCant = cantIntegrantes/2 - ajuste;
			helper.atMost("Max cantidad tipo " + i, maxCant, tipo_psicologico);
			helper.atLeast("Max cantidad tipo " + i, minCant, tipo_psicologico);
			i++;
		}

	}
	
	

}
