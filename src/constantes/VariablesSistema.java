package constantes;

public class VariablesSistema {

	private static int variableAuxiliar = 0;
	
	public static int getVariableAuxiliar(){
		
		return variableAuxiliar++;
	}
	
	public static void setVariableAuxliar( int aux){
		
		variableAuxiliar = aux;
	}
}
