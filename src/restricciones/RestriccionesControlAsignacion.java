package restricciones;

import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import formacionDeGrupos.RequisitoGrupo;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

public class RestriccionesControlAsignacion implements IRestricciones{

	private boolean ejecutada = false;
//	private List< RequisitoGrupo > gruposAFormar;
	private int cantGrupos;
	private List< Usuario > lusuarios;
	

/*
	public RestriccionesControlAsignacion(List<RequisitoGrupo> gruposAFormar, 
			List< Usuario > lusuarios) {

		this.gruposAFormar = gruposAFormar;
		this.lusuarios = lusuarios;
	}
*/

	public RestriccionesControlAsignacion(int cantGrupos, List<Usuario> lusuarios) {

		this.cantGrupos = cantGrupos;
		this.lusuarios = lusuarios;
}

	
	@Override
	public void definirRestricciones(DependencyHelper<Usuario, String> helper)
			throws ContradictionException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void definirRestriccionesParaNGrupos(DependencyHelper<Integer, String> helper)
			throws ContradictionException {

		for(Usuario u : lusuarios){
			Integer[] usuarios = new Integer[lusuarios.size()];
			int index = 0;
//			for(int i = 0; i < this.gruposAFormar.size(); i++){
			for(int i = 0; i < this.cantGrupos; i++){
				usuarios[index++] = (i*lusuarios.size()) + u.getId();
			}
			helper.atMost("Cantidad Integrantes", 1, usuarios);
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
		
		return "";
	}
}
