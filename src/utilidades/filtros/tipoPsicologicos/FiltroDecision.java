package utilidades.filtros.tipoPsicologicos;

import utilidades.filtros.FiltroIgual;
import utilidades.filtros.FiltroMayor;
import utilidades.filtros.FiltroMayorIgual;
import modelo.Usuario;
import constantes.Constantes;

public class FiltroDecision extends FiltroMayorIgual{
	
	public FiltroDecision(){
		this.fi = new FiltroIgual(Constantes.COTA_DICOTOMIA_MB) {
			
			@Override
			public int getValor(Usuario u) {

				return u.getEstilo().getJudging();
			}
		};
		this.fm = new FiltroMayor(Constantes.COTA_DICOTOMIA_MB) {
			
			@Override
			public int getValor(Usuario u) {

				return u.getEstilo().getJudging();
			}
		};

	}

}
