package utilidades.filtros.tipoPsicologicos;

import modelo.Usuario;
import utilidades.filtros.Ifiltro;

public class FiltroEmocional implements Ifiltro{
	
	private FiltroDecision fd;
	
	public FiltroEmocional(){
		this.fd = new FiltroDecision();
	}

	@Override
	public boolean cumple(Usuario u) {

		return (!this.fd.cumple(u));
	}

}
