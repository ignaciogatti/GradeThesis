package restricciones;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.pb.tools.WeightedObject;
import org.sat4j.specs.ContradictionException;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

import constantes.Constantes;


/*
 *Definición de la restriccion para que haya roles balanceado en un grupo:
 *Si es hard:
 *			Ci*xi + ...+ Cj*xj <= Costo máximo
 *Si es soft:
 *			fcion Objetivo += Ci*xi + ...+ Cj*xj 
 */

public class RestriccionesRolBalanceado extends RestriccionesNGruposTemplate{

	
	
	public RestriccionesRolBalanceado( List<Usuario> usuarios, int idGrupos ){
		
		super( usuarios, idGrupos);
		this.soft = true;
		this.costo = Constantes.PENALIZACION_BALANCEO;
	}

	
	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper) throws ContradictionException {
		
		this.ejecutada = true;
		 for (Usuario u : lusuarios) {
			 helper.addToObjectiveFunction(u, this.penalizacionRolBalanceado(u));
//			 objective[index] = WeightedObject.newWO(u, this.penalizacionRolBalanceado(u)); 
		}

	}
	
	
	private int penalizacionRolBalanceado(Usuario usr){
		
		if (!(esBalanceado(usr.getRol().getValCreador()) &&
				esBalanceado(usr.getRol().getValEvaluador()) &&
				esBalanceado(usr.getRol().getValFinalizador()) &&
				esBalanceado(usr.getRol().getValInnovador()) &&
				esBalanceado(usr.getRol().getValLider()) &&
				esBalanceado(usr.getRol().getValManager()) &&
				esBalanceado(usr.getRol().getValModerador()) &&
				esBalanceado(usr.getRol().getValOrganizador())))
			{
				usr.setPenBalanceo( this.costo );
				return this.costo;	
				}
			usr.setPenBalanceo(0);	
			return 0;

	}
	
	private boolean esBalanceado(int valor){
		
		if((Constantes.COTAINF_BALANCEADO <= valor) && (Constantes.COTASUP_BALANCEADO >= valor))
			return true;
		return false;
	}



	@Override
	public void escribirRestricciones(ProblemDefinition pd) {
		
		this.ejecutada = true;
		for (int i = 0; i < lusuarios.size(); i++) {
			 pd.agregarAObjetivo(lusuarios.get(i), 
					 this.penalizacionRolBalanceado(lusuarios.get(i)));
		}
	}


	@Override
	public boolean esPosibleFormarNgrupos() {

		return true;
	}


	public String toString(){
		
		return "";
	}

	public Usuario[] getUsuariosPorRolesBalanceados(){
		
		List< Usuario > ususariosBalanceados = new ArrayList<>();
		for( Usuario u : this.lusuarios ){
			if( this.penalizacionRolBalanceado(u) == 0 ){
				ususariosBalanceados.add(u);
			}
		}
		Usuario[] resultado = new Usuario[ ususariosBalanceados.size() ];
		return ususariosBalanceados.toArray( resultado );
	}


	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		if( this.idGrupo == id){
		this.ejecutada = true;
			if ( soft ){
				int costo = 0;
				for( Usuario u : grupo){
					costo += this.penalizacionRolBalanceado(u);
				}
				return costo;
			}
		}
		return 0;
	}


	@Override
	protected void definirRestriccionSoft( DependencyHelper<Integer, String> helper )
			throws ContradictionException {
		
		for ( Usuario u : lusuarios ) {
			helper.addToObjectiveFunction(( idGrupo * lusuarios.size()) + u.getId(), 
				 this.penalizacionRolBalanceado(u));
			}

	}


	@Override
	protected void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException {
		
		@SuppressWarnings("unchecked") 
		WeightedObject<Integer>[] uPesados = new WeightedObject[ this.lusuarios.size() ];
		for ( Usuario u : lusuarios ){
			uPesados[ u.getId() ] = WeightedObject.newWO(( idGrupo * lusuarios.size()) + u.getId() ,
							this.penalizacionRolBalanceado(u) );
		}
		helper.atMost("Restriccion Rol Balanceado", this.cota , uPesados);

	}
}
