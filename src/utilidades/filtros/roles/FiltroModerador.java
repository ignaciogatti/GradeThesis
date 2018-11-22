package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroModerador extends FiltroMayor{


	public FiltroModerador(int c){
		super(c);
	}
	
	public FiltroModerador(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValModerador();
	}

}
