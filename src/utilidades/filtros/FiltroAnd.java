package utilidades.filtros;

import modelo.Usuario;

public class FiltroAnd implements Ifiltro{
	
	private Ifiltro f1;
	private Ifiltro f2;
	
	public FiltroAnd(Ifiltro f1, Ifiltro f2){
		this.f1 = f1;
		this.f2 = f2;
	}

	@Override
	public boolean cumple(Usuario u) {

		return (this.f1.cumple(u) && this.f2.cumple(u));
	}

}
