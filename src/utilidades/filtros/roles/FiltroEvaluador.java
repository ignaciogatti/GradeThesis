package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroEvaluador extends FiltroMayor{

	public FiltroEvaluador(int c) {

		super(c);
	}
	
	public FiltroEvaluador(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {
		
		return u.getRol().getValEvaluador();
	}


}
