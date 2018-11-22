package restricciones;

import java.util.List;

import modelo.Usuario;

import org.sat4j.pb.tools.DependencyHelper;
import org.sat4j.specs.ContradictionException;

public abstract class RestriccionesNGruposTemplate implements IRestricciones{
	
	protected List<Usuario> lusuarios;
	protected boolean ejecutada = false;
	protected boolean soft;
	protected int costo;
	protected int cota;
	protected int idGrupo;
	
	
	public RestriccionesNGruposTemplate( List<Usuario> lusuarios, int idGrupo){
		
		this.lusuarios = lusuarios;
		this.idGrupo = idGrupo;
	}
	
	
	protected abstract void definirRestriccionSoft( DependencyHelper<Integer, String> helper )
			throws ContradictionException ;
	
	protected abstract void definirRestriccionHard( DependencyHelper<Integer, String> helper )
			throws ContradictionException ;
	
	
	public void definirRestriccionesParaNGrupos(DependencyHelper<Integer, String> helper) 
			throws ContradictionException{
		
		ejecutada = true;
		if ( soft ){
			definirRestriccionSoft( helper );
		}else{
			definirRestriccionHard( helper );
		}
	}
	
	@Override
	public boolean fueEjecutada() {

		return this.ejecutada;
	}



	@Override
	public void reset() {

		this.ejecutada = false;
	}
	
	public void setSoft(boolean soft) {

		this.soft = soft;
		
	}

	public void setCosto(int costo) {

		this.costo = costo;
	}

	public void setCota(int cota){
		
		this.cota = cota;
	}


}
