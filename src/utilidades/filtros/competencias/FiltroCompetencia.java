package utilidades.filtros.competencias;

import utilidades.filtros.Ifiltro;
import modelo.Usuario;


public class FiltroCompetencia implements Ifiltro{

	private Integer cota;
	private String competencia;
	
	public FiltroCompetencia(){
		
	}

	public FiltroCompetencia( Integer cota, String comptencia ){
		
		this.cota = cota;
		this.competencia = comptencia;
		
	}

	
	
	public void setCota(Integer cota) {
		this.cota = cota;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public Integer getComptencia( Usuario u ) {
		
		return u.getCompetencias().getValor( competencia );
	}
	
	public boolean cumple( Usuario u ){
		
		if( this.getComptencia( u ) >= this.cota ) {
			return true;
		}
		return false;
	}
	
}
