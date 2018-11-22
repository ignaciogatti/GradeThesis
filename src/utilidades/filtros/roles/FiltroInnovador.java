package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroInnovador extends FiltroMayor{

	public FiltroInnovador(int c){
		super(c);
	}
	
	public FiltroInnovador(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValInnovador();
	}


}
