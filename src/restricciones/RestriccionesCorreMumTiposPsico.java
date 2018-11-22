package restricciones;

import java.util.ArrayList;
import java.util.List;

import modelo.Estilo;
import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.pb.tools.WeightedObject;
import org.sat4j.specs.ContradictionException;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;
import utilidades.Utilidades;

import constantes.Constantes;
/*
 *Restriccion para que haya perfiles balanceados en un grupo
 *Si es soft:
 *			fcion Objetivo += Ci*xi + ...+ Cj*xj 
 * Si es hard:
 *			Ci*xi + ...+ Cj*xj <= Costo máximo
 */

public class RestriccionesCorreMumTiposPsico extends RestriccionesNGruposTemplate{

	
	/*
	 *Estilo psicologico ideal para cada rol 
	 * 	E=0,S=0,T=0,J=0,I=1,N=1,F=1,P=1;
	 */
	private final Estilo[] TIPO_MODERADOR ={new Estilo(0,0,1,0), new Estilo(0,1,1,0)};
	private final Estilo[] TIPO_INNOVADOR = {new Estilo(0,1,0,1), new Estilo(0,1,1,1)};
	private final Estilo[] TIPO_CREADOR = {new Estilo(1,1,0,0), new Estilo(1,1,1,0)};
	private final Estilo[] TIPO_MANAGER= {new Estilo(0,0,1,1), new Estilo(0,0,0,1), new Estilo(0,0,0,0), new Estilo(0,1,0,0)};
	private final Estilo[] TIPO_EVALUADOR = {new Estilo(1,0,0,0), new Estilo(1,0,1,0)};
	private final Estilo[] TIPO_ORGANIZADOR= {new Estilo(1,0,0,0), new Estilo(1,0,1,0), new Estilo(0,0,0,0), new Estilo(0,1,0,0)};
	private final Estilo[] TIPO_LIDER = {new Estilo(0,0,0,0), new Estilo(0,1,0,0)};
	private final Estilo[] TIPO_FINALIZADOR = {new Estilo(0,0,0,0), new Estilo(0,1,0,0)};
	

	private Estilo[][] estilo_ideal ={TIPO_LIDER, TIPO_MODERADOR, TIPO_CREADOR, TIPO_INNOVADOR,
											TIPO_MANAGER, TIPO_ORGANIZADOR, TIPO_EVALUADOR, TIPO_FINALIZADOR};

	
	public RestriccionesCorreMumTiposPsico( List<Usuario> usuarios, int idGrupo ){
		
		super(usuarios, idGrupo);
		this.soft = true;
		this.costo = Constantes.PENALIZACION_CORRELACION;
		
	}

	
	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper) throws ContradictionException {
		
		this.ejecutada = true;
		
		 for (Usuario u : lusuarios) {
			 helper.addToObjectiveFunction(u, this.penalizacionCorrelacionMBMumma(u));
//			 objective[index] = WeightedObject.newWO(u, this.penalizacionCorrelacionMBMumma(u)); 
		}
		
	}

	private int penalizacionCorrelacionMBMumma(Usuario u){
		
		int penalizacion = 0;
		boolean  correlacion = false;

		for(int i = 0; i < u.getRoles().length && !correlacion; i++){
			if(u.getRoles()[i]){
				for(int j = 0 ; j < this.estilo_ideal[i].length && !correlacion ; j++ ){
					if( Utilidades.defineEstiloStandard(u).equals(this.estilo_ideal[i][j]) ){
						correlacion = true;
						u.setPenCorrelacion(0);
						penalizacion = 0;
					}
				}
			}
		}
		if(!correlacion){
			u.setPenCorrelacion(this.costo);
			penalizacion = this.costo;
		}
		return penalizacion;
	}
		

	@Override
	public void escribirRestricciones(ProblemDefinition pd) {
		
		this.ejecutada = true;
		 for (int i = 0 ; i< lusuarios.size(); i++) {
			 pd.agregarAObjetivo(lusuarios.get(i), this.penalizacionCorrelacionMBMumma(lusuarios.get(i)));
		}
	}

	public Usuario[] getUsuariosPorCorrelacion(){
		
		List< Usuario > ususariosCorre = new ArrayList<>();
		for( Usuario u : this.lusuarios ){
			if( this.penalizacionCorrelacionMBMumma(u) == 0 ){
				ususariosCorre.add(u);
			}
		}
		Usuario[] resultado = new Usuario[ ususariosCorre.size() ];
		return ususariosCorre.toArray( resultado );
	}

	@Override
	public boolean esPosibleFormarNgrupos() {
		return true;
	}

	
	public String toString(){
		
		return "";
	}



	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		if( this.idGrupo == id){
			this.ejecutada = true;
			if ( soft ){
				int costo = 0;
				for (Usuario u : grupo) {
					 costo += this.penalizacionCorrelacionMBMumma(u);
				}
				return costo;
			}
		}
		return 0;
	}


	@Override
	protected void definirRestriccionSoft( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		for (Usuario u : lusuarios) {
			helper.addToObjectiveFunction( (this.idGrupo*lusuarios.size()) + u.getId(),
					this.penalizacionCorrelacionMBMumma(u));
//		 	objective[index] = WeightedObject.newWO(u, this.penalizacionCorrelacionMBMumma(u)); 
			}

	}

	@Override
	protected void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException {

		@SuppressWarnings("unchecked") 
		WeightedObject<Integer>[] uPesados = new WeightedObject[ this.lusuarios.size() ];
		for ( Usuario u : lusuarios ){
			uPesados[ u.getId() ] = 
					WeightedObject.newWO( (this.idGrupo * lusuarios.size()) + u.getId(),
							this.penalizacionCorrelacionMBMumma(u) );
		}
		helper.atMost("Restriccion Correlacion Mumma", this.cota , uPesados);

	}

}
