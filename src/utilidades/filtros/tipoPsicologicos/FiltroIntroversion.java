package utilidades.filtros.tipoPsicologicos;

import modelo.Usuario;
import utilidades.filtros.Ifiltro;

public class FiltroIntroversion implements Ifiltro{

	private FiltroActitud fa;
	
	public FiltroIntroversion(){
		this.fa = new FiltroActitud();
	}
	
	@Override
	public boolean cumple(Usuario u) {

		return (!this.fa.cumple(u));
	}

}
