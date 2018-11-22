package utilidades.filtros;

import modelo.Usuario;

public interface Ifiltro {
	
	public abstract boolean cumple(Usuario u);
}
