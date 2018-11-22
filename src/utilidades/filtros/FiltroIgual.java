package utilidades.filtros;

import modelo.Usuario;

public abstract class FiltroIgual implements Ifiltro{
	
private int cota;
	
	public FiltroIgual(int c){
		this.cota = c;
	}
	

	public abstract int getValor(Usuario u);
	
	@Override
	public boolean cumple(Usuario u) {

		if(getValor(u) == cota)
			return true;
		return false;
	}


}
