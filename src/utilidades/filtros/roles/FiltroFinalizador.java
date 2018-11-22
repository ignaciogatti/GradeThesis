package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroFinalizador extends FiltroMayor{

	public FiltroFinalizador(int c) {
		
		super(c);
	}
	
	public FiltroFinalizador(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValFinalizador();
	}


}
