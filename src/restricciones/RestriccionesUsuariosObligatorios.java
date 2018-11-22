package restricciones;

import java.util.List;

import modelo.Grupo;
import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

public class RestriccionesUsuariosObligatorios implements IRestricciones{

	private Grupo grupoAcompletar;
	private List<Usuario> lusuarios;
	private boolean ejecutada = false;
	private int id;
	
	public RestriccionesUsuariosObligatorios(List<Usuario> lusuarios, Grupo grupos, int id ) {

		this.grupoAcompletar = grupos;
		this.lusuarios =lusuarios;
		this.id = id;
	}
	
	
	public void setUsuariosParaGrupo( Grupo unGrupo ){
		
		this.grupoAcompletar = unGrupo;
	}


	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper)
			throws ContradictionException {

			for( Usuario u : this.grupoAcompletar.getIntegrantes() ){
				helper.setTrue(u, "Usuario " + u.getId() );
			}
	}


	@Override
	public void definirRestriccionesParaNGrupos(
			DependencyHelper<Integer, String> helper) throws ContradictionException {
		
		this.ejecutada = true;
			for( Usuario u : this.grupoAcompletar.getIntegrantes() ){
				helper.setTrue(new Integer( u.getId() + ( this.id * this.lusuarios.size() ) ),
						"Usuario " + u.getId() );
			}
		
	}


	@Override
	public void escribirRestricciones(ProblemDefinition pd) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean esPosibleFormarNgrupos() {

		return true;
	}


	@Override
	public boolean fueEjecutada() {

		return this.ejecutada;
	}


	@Override
	public void reset() {

		this.ejecutada = false;
	}


	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		return 0;
	}
	
	public String toString(){
		
		return "Restriccion completar grupo";
	}

}
