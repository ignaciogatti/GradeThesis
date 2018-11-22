package utilidades.filtros.roles;

import utilidades.filtros.FiltroMayor;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroOrganizador extends FiltroMayor{

	public FiltroOrganizador(int c) {
		super(c);
	}
	
	public FiltroOrganizador(){
		super(Constantes.COTA_CUMPLE_ROL);
	}

	@Override
	public int getValor(Usuario u) {

		return u.getRol().getValOrganizador();
	}


}
