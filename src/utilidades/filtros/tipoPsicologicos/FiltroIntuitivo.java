package utilidades.filtros.tipoPsicologicos;

import modelo.Usuario;
import utilidades.filtros.Ifiltro;

public class FiltroIntuitivo implements Ifiltro{

	private FiltroPercepcion fp;
	
	public FiltroIntuitivo(){
		this.fp = new FiltroPercepcion();
	}
	@Override
	public boolean cumple(Usuario u) {

		return (!this.fp.cumple(u));
	}

}
