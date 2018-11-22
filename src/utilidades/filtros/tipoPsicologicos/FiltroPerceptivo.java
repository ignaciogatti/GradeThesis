package utilidades.filtros.tipoPsicologicos;

import modelo.Usuario;
import utilidades.filtros.Ifiltro;

public class FiltroPerceptivo implements Ifiltro{

	private FiltroEstiloVida fv;
	
	public FiltroPerceptivo(){
		this.fv = new FiltroEstiloVida();
	}
	@Override
	public boolean cumple(Usuario u) {

		return (!this.fv.cumple(u));
	}
	

}
