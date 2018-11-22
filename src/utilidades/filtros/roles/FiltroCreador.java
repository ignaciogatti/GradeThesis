package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroCreador extends FiltroMayor{

	public FiltroCreador(int c){
		super(c);
	}
	
	public FiltroCreador(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValCreador();
	}


}
