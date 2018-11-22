package restricciones;

import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

/*
 * Interfaz comun para todas las restricciones que se definan
 */

public interface IRestricciones {

	public abstract void definirRestricciones(
			DependencyHelper<Usuario, String> helper) throws ContradictionException; 

	
	public abstract void definirRestriccionesParaNGrupos(
			DependencyHelper<Integer, String> helper) throws ContradictionException; 
	
	public abstract void escribirRestricciones(ProblemDefinition pd);
	
	public abstract boolean esPosibleFormarNgrupos();
	
	public abstract boolean fueEjecutada();
	
	public abstract void reset();
	
	public abstract int getCosto( List<Usuario> grupo, int id );
	
	
}
