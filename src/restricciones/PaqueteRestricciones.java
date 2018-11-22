package restricciones;

import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import solvers.PseudoBoolean.otroPB.ProblemDefinition;

/*
 * Para que funcione correctamente, todas las instancias de 
 * PaqueteRestricciones deben compartir las mismas instancias de 
 * restricciones
 */

public class PaqueteRestricciones implements IRestricciones{
	
	private String nombre;
	private List<IRestricciones> restricciones;

	public PaqueteRestricciones() {

	}

	public PaqueteRestricciones(String nombre,
			List<IRestricciones> restricciones) {

		this.nombre = nombre;
		this.restricciones = restricciones;
	}

	public PaqueteRestricciones(String nombre) {

		this.nombre = nombre;
		this.restricciones = new ArrayList<>();
		
	}
	
	public void agregarRestriccion(IRestricciones r){
		
		this.restricciones.add(r);
	}
	
	public void definirRestricciones( DependencyHelper<Usuario, String> helper ) 
			throws ContradictionException {
		
		for( IRestricciones r : restricciones ){
			if( !r.fueEjecutada() ){
				r.definirRestricciones( helper );
			}
		}
	}
	
	public void escribirRestricciones( ProblemDefinition pd ) {
		
		for( IRestricciones r : restricciones ){
			if( !r.fueEjecutada() ){
				r.escribirRestricciones(pd);
			}
		}
	}
	
	public void reset(){
		
		for( IRestricciones r : restricciones){
			r.reset();
		}
	}
	
	public boolean esPosibleFormarNgrupos(){
		
		 for (IRestricciones r : restricciones){
			 if( !r.esPosibleFormarNgrupos() ){
				 return false;
			 }
		 }
		 return true;
	}

	@Override
	public boolean fueEjecutada() {

		for (IRestricciones r : restricciones){
			 if( !r.fueEjecutada() ){
				 return false;
			 }
		 }
		 return true;

	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String toString(){
		
		String rta = "";
		rta += nombre + " \n ";
		for(IRestricciones r : restricciones){
			rta += r.toString();
		}
		return rta;
	}

	@Override
	public void definirRestriccionesParaNGrupos(
			DependencyHelper<Integer, String> helper)throws ContradictionException {
		
		for( IRestricciones r : restricciones ){
			if( !r.fueEjecutada() ){
				r.definirRestriccionesParaNGrupos(helper);
			}
		}

	}

	@Override
	public int getCosto(List<Usuario> grupo, int id) {

		int costo = 0;
		for( IRestricciones r : restricciones ){
			if( !r.fueEjecutada() ){
				costo += r.getCosto(grupo, id);
			}
		}
		return costo;
	}
}
