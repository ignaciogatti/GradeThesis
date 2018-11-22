package utilidades.filtros;

import modelo.Usuario;

public class FiltroNot implements Ifiltro{
	
	private Ifiltro f;
	
	public FiltroNot(Ifiltro f){
		
		this.f = f;
	}

	@Override
	public boolean cumple(Usuario u) {

		return (!this.f.cumple(u));
	}

}
