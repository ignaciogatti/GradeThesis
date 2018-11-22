package utilidades.filtros.tipoPsicologicos;

import utilidades.filtros.FiltroIgual;
import utilidades.filtros.FiltroMayor;
import utilidades.filtros.FiltroMayorIgual;
import constantes.Constantes;
import modelo.Usuario;

public class FiltroActitud extends FiltroMayorIgual{

	
	public FiltroActitud(){
		
		this.fi = new FiltroIgual(Constantes.COTA_DICOTOMIA_MB) {
			
			@Override
			public int getValor(Usuario u) {

				return u.getEstilo().getAttitude();
			}
		};
		this.fm = new FiltroMayor(Constantes.COTA_DICOTOMIA_MB) {
			
			@Override
			public int getValor(Usuario u) {

				return u.getEstilo().getAttitude();
			}
		};
			
		}
	public boolean cumple(Usuario u){
		return super.cumple(u);
	}
		

}
