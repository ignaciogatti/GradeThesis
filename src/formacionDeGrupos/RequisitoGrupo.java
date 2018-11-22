package formacionDeGrupos;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

import restricciones.PaqueteRestricciones;

public class RequisitoGrupo {

	private int cantIntegrantes;
	private PaqueteRestricciones restricciones;

	public RequisitoGrupo(int cantIntegrantes, PaqueteRestricciones restricciones) {

		this.cantIntegrantes = cantIntegrantes;
		this.restricciones = restricciones;
	}

	public int getCantIntegrantes() {
		return cantIntegrantes;
	}

	public void setCantIntegrantes(int cantIntegrantes) {
		this.cantIntegrantes = cantIntegrantes;
	}

	public PaqueteRestricciones getRestricciones() {
		return restricciones;
	}

	public void setRestricciones(PaqueteRestricciones restricciones) {
		this.restricciones = restricciones;
	}
	
	public void definirRequisito( DependencyHelper<Integer, String> helper ) 
			throws ContradictionException{
		
		this.restricciones.definirRestriccionesParaNGrupos(helper);
	}
}
