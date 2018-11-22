package configuracion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constantes.Constantes;

import db.DBInterface;
import db.MySQLDBInterface;

import modelo.ConjuntoGrupo;
import modelo.Estilo;
import modelo.Grupo;
import modelo.InteractuanId;
import modelo.Rol;
import modelo.Usuario;
import modelo.formacionGrupos.CompetenciasTecnicas;

public class DBConfigurador implements Configurador{
	
	private List<InteractuanId> interacciones = new ArrayList<>();
	private List<Usuario> usuarios = new ArrayList<>();
	private MySQLDBInterface db;
	
	public DBConfigurador( String path ) {
		
		try {
			this.db = new MySQLDBInterface();
			this.db.connect(Constantes.DB_NAME, Constantes.DB_PORT, Constantes.DB_USER, "");
			this.cargarUsuarios();
			this.cargarInteracciones();
			this.db.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	

	@Override
	public List<Usuario> getUsuarios() {

		ArrayList<Usuario> lusuarios = new ArrayList<>(this.usuarios);
		return lusuarios;
	}
	
	public List<InteractuanId> getInteracciones(){
		
		return this.interacciones;
	}
	
	protected void cargarInteracciones() throws SQLException{
		
		ResultSet result = db.executeQuery( String.format("select * from interactuan"));
		while (result.next()) {
			 InteractuanId interactuan = new InteractuanId(result.getInt(1),result.getInt(2),result.getString(3));
			 interacciones.add(interactuan);
		 }
	}
	
	protected void cargarUsuarios() throws SQLException{
		
		ResultSet result = db.executeQuery( String.format("select t1.nombre, t1.id, t2.attitude, t2.perceiving, t2.judging, t2.lifestyle, t3.valModerador, t3.valCreador, t3.valInnovador, t3.valManager, t3.valOrganizador, t3.valEvaluador, t3.valFinalizador, t3.valLider from usuario t1 join estilo t2 on t1.id = t2.idUsr join rol t3 on t1.id = t3.idUsr"));
	    int i = 0;
		while (result.next()) {
			Usuario u = new Usuario();
			u.setId(i++);
			u.setNombre( result.getString(1) );
			u.setIdGrupo(-1);
			Estilo estilo = getEstilo( result.getInt("attitude"), result.getInt("judging"),
					result.getInt("perceiving"), result.getInt("lifestyle") );
			Rol rol = getRol( result.getInt("valLider"), result.getInt("valModerador"),
					result.getInt("valCreador"), result.getInt("valInnovador"),
					result.getInt("valManager"), result.getInt("valOrganizador"), 
					result.getInt("valEvaluador"), result.getInt("valFinalizador"));
			u.setEstilo(estilo);
			u.setRol(rol);
			this.getCompetencia( result.getInt("id"), u);
			this.usuarios.add(u);			
		}
	}

	private Estilo getEstilo(int attitude, int judging,int perceiving, int lifestyle){
		
		Estilo estilo = new Estilo();
		Integer att = new Integer(attitude);
		Integer per = new Integer(perceiving);
		Integer jud = new Integer(judging);
		Integer lif = new Integer(lifestyle);
		estilo.setAttitude(att);
		estilo.setJudging(jud);
		estilo.setLifestyle(lif);
		estilo.setPerceiving(per);
		return estilo;
	}
	
	
	private Rol getRol(int valLider, int valModerador, int valCreador, int valInnovador, 
			int valManager, int valOrganizador, int valEvaluador, int valFinalizador) {
		
		Rol rol = new Rol();
		rol.setValCreador(new Integer(valCreador));
		rol.setValEvaluador(new Integer(valEvaluador));
		rol.setValFinalizador(new Integer(valFinalizador));
		rol.setValInnovador(new Integer(valInnovador));
		rol.setValLider(new Integer(valLider));
		rol.setValManager(new Integer(valManager));
		rol.setValModerador(new Integer(valModerador));
		rol.setValOrganizador(new Integer(valOrganizador));
		return rol;
	}
	
	private void getCompetencia( int id, Usuario u ) throws SQLException{
		
		ResultSet result = db.executeQuery( String.format("select * from competencia where id_Usr = " + id ));
		while (result.next()) {
			u.agregarCompetencia( result.getString("competencia"), result.getInt("nivel") );
		}
	}
	
	public void guardarGrupos( ConjuntoGrupo grupos ){
		
		try {
			this.db.connect(Constantes.DB_NAME, Constantes.DB_PORT, Constantes.DB_USER, "");
			int idGrupos = 0;
			for( Grupo g : grupos.getGrupos() ){
				g.setId(idGrupos);
				String query = "INSERT INTO grupo(id, tamanio, cantUsuarios) VALUES (" + g.getId()+ " ," + g.getTamanio() + " ," + g.getCantUsuarios() +")";
				int resultado = this.db.executeUpdate( query );
				for( Usuario u : g.getIntegrantes() ){
					query = "UPDATE usuario SET idGrupo = "+ idGrupos +" where id = " + (u.getId() + 1);
					resultado = this.db.executeUpdate( query );
					System.out.println("Se modificaron: " + resultado );
				}
				idGrupos++;
			}
			this.db.commit();
			this.db.disconnect();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
