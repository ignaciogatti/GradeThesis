package configuracion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.csvreader.CsvReader;

import modelo.Estilo;
import modelo.Interactuan;
import modelo.InteractuanId;
import modelo.Rol;
import modelo.Topico;
import modelo.Usuario;

public class CSVConfigurador implements Configurador{
	
	private CsvReader reader;
	private ArrayList<Usuario> usuarios;
		
	public CSVConfigurador(String path) {
		this.usuarios = new ArrayList<Usuario>();
		try {
			Charset charset = Charset.forName("UTF-8");
			reader = new CsvReader(path,';',charset);
			parsear();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parsear() throws IOException {

		Vector<Topico> topicos = new Vector<Topico>();
		Vector<Interactuan> interacciones = new Vector<Interactuan>();
	    reader.readRecord();
	    int i = 0;
			while (reader.readRecord()) {
				String[] record = reader.getValues();
				Usuario usuario = new Usuario();
				usuario.setId(i++);
				usuario.setNombre(record[0]);
				usuario.setMail(record[1]);
				usuario.setIdGrupo(-1);
				Estilo estilo = getEstilo(record[2],record[3],record[4],record[5]);
				Rol rol = getRol(record[6],record[7],record[8],record[9],record[10],record[11],record[12],record[13]);
				usuario.setEstilo(estilo);
				usuario.setRol(rol);
				this.usuarios.add(usuario);
				this.getInteracciones(interacciones,record);
				rol.setIdUsr(usuario.getId());
				estilo.setIdUsr(usuario.getId());
			}
		topicos = getTopicos(interacciones);	
/*		for (Topico topico : topicos) {
			session.save(topico);
		}
		session.getTransaction().commit();
	    session = getSession();

		for (Interactuan interactuan : interacciones) {
			String u1 = interactuan.getNombreUsr1();
			String u2 = interactuan.getNombreUsr2();
			int idUsr1 = (Integer) session.createQuery("select id from Usuario where nombre= ?").setString(0,u1).uniqueResult();
			int idUsr2 = (Integer) session.createQuery("select id from Usuario where nombre= ?").setString(0,u2).uniqueResult();
			InteractuanId id = new InteractuanId();
			id.setIdUsr1(idUsr1);
			id.setIdUsr2(idUsr2);
			id.setTopico(interactuan.getTopicoAux());
			interactuan.setId(id);
			session.save(interactuan);
		}
		session.getTransaction().commit();
*/	}
	
	
	
	private Vector<Topico> getTopicos(Vector<Interactuan> interacciones) {
		Vector<Topico> retorno = new Vector<Topico>();
		for (Interactuan interaccion : interacciones){
			Topico t = new Topico(interaccion.getTopicoAux());
			if (!retorno.contains(t))
				retorno.add(t);
		} 
			
		return retorno;
	}

	private Vector<Interactuan> getInteracciones(Vector<Interactuan> interacciones, String[] record) {
		for (int i=14; i < record.length && !record[i].isEmpty(); i=i+2){
			Interactuan interactuan = getInteractuan(record[0], record[i], record[i+1]);
			Interactuan inversa = getInteractuan(record[i], record[0], record[i+1]);
			if (!interacciones.contains(inversa))  
				interacciones.add(interactuan);
		} 
		return interacciones;
	}

	public Interactuan getInteractuan(String usr1, String usr2, String topico) {
		Interactuan retorno = new Interactuan();
		retorno.setNombreUsr1(usr1);
		retorno.setNombreUsr2(usr2);
		retorno.setTopicoAux(topico);
		return retorno;
	}

	public Estilo getEstilo(String attitude, String judging,String perceiving, String lifestyle){
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
	
	public Rol getRol(String valLider, String valModerador, String valCreador, String valInnovador, String valManager, String valOrganizador, String valEvaluador, String valFinalizador) {
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

	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lusuarios = new ArrayList<>(this.usuarios);
		return lusuarios;
	}

}
