package restricciones;

import java.util.List;

import modelo.Grupo;
import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

public class RestriccionesUsuariosDescartados implements IRestricciones{

	private List<Grupo> gruposAcompletar;
	private List<Usuario> lusuarios;
	private boolean ejecutada = false;
	private int cantGrupos;
	
	public RestriccionesUsuariosDescartados(List<Usuario> lusuarios, List<Grupo> grupos, 
			int cantGrupos ) {

		this.gruposAcompletar = grupos;
		this.lusuarios =lusuarios;
		this.cantGrupos = cantGrupos;
	}
	
	
	public void setUsuariosParaGrupo( List<Grupo> unGrupo ){
		
		this.gruposAcompletar = unGrupo;
	}


	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper)
			throws ContradictionException {

		for( int i = 0; i < this.cantGrupos; i++ ){
			for( Grupo g : this.gruposAcompletar ){
				for( Usuario u : g.getIntegrantes() ){
					helper.setFalse(u, "Usuario " + u.getId() );
				}
			}
		}
		
	}


	@Override
	public void definirRestriccionesParaNGrupos(
			DependencyHelper<Integer, String> helper) throws ContradictionException {

		this.ejecutada = true;
		for( int i = 0; i < this.cantGrupos; i++){
			for( Grupo g : this.gruposAcompletar ){
				for( Usuario u : g.getIntegrantes() ){
					helper.setFalse( u.getId() + ( i*this.lusuarios.size() ), "Usuario " + u.getId() );
				}
			}
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
		
		return "Restriccion Descartar usuarios \n";
	}

}
