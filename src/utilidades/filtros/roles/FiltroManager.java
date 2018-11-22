package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroManager extends FiltroMayor{

	public FiltroManager(int c){
		
		super(c);
	}
	
	public FiltroManager(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValManager();
	}


}
