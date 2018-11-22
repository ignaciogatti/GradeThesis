package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import constantes.Constantes;
import modelo.Usuario;

public class FiltroLider extends FiltroMayor{

	public FiltroLider(int c) {
		
		super(c);
	}
	
	public FiltroLider(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValLider();
	}

}
