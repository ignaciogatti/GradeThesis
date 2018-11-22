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
import utilidades.filtros.roles.FiltroCreador;
import utilidades.filtros.roles.FiltroEvaluador;
import utilidades.filtros.roles.FiltroFinalizador;
import utilidades.filtros.roles.FiltroInnovador;
import utilidades.filtros.roles.FiltroLider;
import utilidades.filtros.roles.FiltroManager;
import utilidades.filtros.roles.FiltroModerador;
import utilidades.filtros.roles.FiltroOrganizador;

/*
 * 
 * Restricciones sobre los roles. Para cada rol se debe cumplir:
 * 1 <= xi+...+xj <= #usuarios/2
 * 
 * En caso de ser una restriccion soft:
 * -Cota(o un numero grande que haga cierta la desigualdad)*xaux + xi +...+ xj <= Cota
 * Fcion objetivo : ...+ Costo * xaux
 * 
 */


public class RestriccionesRoles extends RestriccionesNGruposTemplate{

	private int cantIntegrantes;
	
	private enum ROLES { 	LIDER(0), 
							MODERADOR(1), 
							CREADOR(2), 
							INNOVADOR(3), 
							MANAGER(4), 
							ORGANIZADOR(5),
							EVALUADOR(6),
							FINALIZADOR(7);
				
							private int posicion;
							
							ROLES(int p){
								posicion = p;
							}
							public int getPosicion(){ return posicion;}								
						};
					
	private  Ifiltro[] roles = {new FiltroLider(), new FiltroModerador(), new FiltroCreador(), 
								new FiltroInnovador(),new FiltroManager(),new FiltroOrganizador(), 
								new FiltroEvaluador(), new FiltroFinalizador()};

	private ArrayList<Usuario[]> usuariosPorRoles = new ArrayList<>();


	public RestriccionesRoles(int cantIntegrantes, int idGrupos,
			List<Usuario> lusuarios) {

		super(lusuarios, idGrupos);
		this.cantIntegrantes = cantIntegrantes;
		

		this.soft = false;
		this.costo = 0;
	}



	public boolean esPosibleFormarNgrupos(){

		for(int i = ROLES.LIDER.getPosicion(); i <= ROLES.FINALIZADOR.getPosicion();i++){
			Usuario[] rol = this.getUsuariosRol(lusuarios, i);
			if( this.cantIntegrantes > rol.length ){
				System.out.println("No se cumple el rol" + i);
				return false;
			}
		}
		return true;
	}
	

	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper) 
			throws ContradictionException {
		
		this.ejecutada = true;
		int maxCant = 0;
		
		for(int i = ROLES.LIDER.getPosicion(); i <= ROLES.FINALIZADOR.getPosicion();i++){
			Usuario[] rol = this.getUsuariosRol(lusuarios, i);
			maxCant = cantIntegrantes/2 + (cantIntegrantes %2);	
			//la suma del modulo es para el caso de los grupos impares
			helper.atLeast("Cubrir rol " + i, 1, rol);
			helper.atMost("Max cantidad rol " + i, maxCant, rol);
		}
		
	}
	
	
	private Usuario[] getUsuariosRol(List<Usuario> lusr, int r){
		ArrayList<Usuario> resultado = new ArrayList<>();
		for(Usuario u : lusr){
			if(this.roles[r].cumple(u))
				resultado.add(u);
		}
		Usuario[] usuarios = new Usuario[resultado.size()];
		resultado.toArray(usuarios);
		return usuarios;
		
	}

	@Override
	public void escribirRestricciones(ProblemDefinition pd) {
		
		this.ejecutada = true;
		int maxCant = 0;
		for(int i = ROLES.LIDER.getPosicion(); i <= ROLES.FINALIZADOR.getPosicion();i++){
			Usuario[] rol = this.getUsuariosRol(lusuarios, i);

			maxCant = cantIntegrantes/2 + (cantIntegrantes %2);	
			pd.agregarRestriccionLinealMayorIgual(rol, 1);
			pd.agregarRestriccionLinealMenorIgual(rol, maxCant);
		}
	}

	@Override
	public String toString() {
		
		return "";
	}

	public String getUsuarios() {
		
		String restricciones = "";
		for(int i = ROLES.LIDER.getPosicion(); i <= ROLES.FINALIZADOR.getPosicion(); i++){
			Usuario[] rol = this.getUsuariosRol(lusuarios, i);
			restricciones += "Rol " + i + "\n";
			for(Usuario u : rol){
				restricciones += u.toString() + " \n";
			}
			restricciones += "Cantidad: " + rol.length + " \n\n"; 
		}

		return restricciones;
	}
	
	private void calcularUsuariosPorRoles(){
		
		if( this.usuariosPorRoles.isEmpty()){
			for(int i = ROLES.LIDER.getPosicion(); 
					i <= ROLES.FINALIZADOR.getPosicion(); i++){
				Usuario[] rol = this.getUsuariosRol(lusuarios, i);
				this.usuariosPorRoles.add(rol);
			}
		}
	}

	public List<Usuario[]> getUsuariosPorRoles(){
		
		if( this.usuariosPorRoles.isEmpty() ){
			this.calcularUsuariosPorRoles();
		}
		return this.usuariosPorRoles;
	}


	@Override
	public void definirRestriccionesParaNGrupos(
			DependencyHelper<Integer, String> helper)
			throws ContradictionException {

		this.calcularUsuariosPorRoles();
		super.definirRestriccionesParaNGrupos(helper);
		
	}

	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		if( this.idGrupo == id){
			this.ejecutada = true;
			if ( soft ){
				int costo = 0;
				for(Usuario[] usuariosRol : this.usuariosPorRoles){
					int cantUsuarios = 0;
					for( int j = 0; j < usuariosRol.length; j++ ){
						if (grupo.contains(usuariosRol[ j ] ) )
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
		
		int maxCant = 0;
		int i = 0;
		for(Usuario[] usuariosRol : this.usuariosPorRoles){
			@SuppressWarnings("unchecked") 
			WeightedObject<Integer>[] rol = 
										new WeightedObject[ usuariosRol.length + 1 ];
			Integer auxiliar = VariablesSistema.getVariableAuxiliar();
			rol[0] = WeightedObject.newWO( auxiliar, (-1) * usuariosRol.length );

			for( int j = 1; j < rol.length; j++ ){
				rol[j] = 
						WeightedObject.newWO( (idGrupo*lusuarios.size()) + usuariosRol[j - 1].getId(), 1 );
			}
			maxCant = cantIntegrantes/2 + (cantIntegrantes %2);	
			//la suma del modulo es para el caso de los grupos impares
			this.cota = maxCant;
			helper.atMost("Max cantidad rol " + i, maxCant, rol);
			helper.addToObjectiveFunction( auxiliar, this.costo );
			}

		
	}



	@Override
	protected void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException {
		
		int maxCant = 0;
		int i = 0;
		for(Usuario[] usuariosRol : this.usuariosPorRoles){
			Integer[] rol = new Integer[usuariosRol.length];
			for( int j = 0; j < usuariosRol.length; j++ ){
				rol[j] = ( idGrupo * lusuarios.size() ) + usuariosRol[j].getId();
			}
			maxCant = cantIntegrantes/2 + (cantIntegrantes %2);	
			//la suma del modulo es para el caso de los grupos impares
			helper.atLeast("Cubrir rol " + i, 1, rol);
			helper.atMost("Max cantidad rol " + i, maxCant, rol);
			i++;
		}

	}




}
