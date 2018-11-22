package utilidades.filtros;

import modelo.Usuario;

public abstract class FiltroMayor implements Ifiltro{
	
	private int cota;
	
	public FiltroMayor(int c){
		this.cota = c;
	}
	
	public abstract int getValor(Usuario u);

	@Override
	public boolean cumple(Usuario u) {

		if(getValor(u) > cota)
			return true;
		return false;
	}

}
