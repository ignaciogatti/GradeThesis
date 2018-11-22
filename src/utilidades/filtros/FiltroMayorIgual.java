package utilidades.filtros;

import modelo.Usuario;

public abstract class FiltroMayorIgual implements Ifiltro{
	
	protected FiltroMayor fm;
	protected FiltroIgual fi;

	public FiltroMayorIgual(){}


	@Override
	public boolean cumple(Usuario u) {

		return (fm.cumple(u) || fi.cumple(u));
	}

}
