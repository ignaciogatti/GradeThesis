package test;

import java.util.ArrayList;
import java.util.List;

import analasisGrupo.AnalisisDiscriminacionTipo;

import configuracion.DBConfigurador;
import modelo.ConjuntoGrupo;
import modelo.Grupo;
import modelo.Usuario;
import parserArchivoGrupos.ParserArchgrupos;
import restricciones.RestriccionesCorreMumTiposPsico;
import restricciones.RestriccionesRolBalanceado;
import restricciones.RestriccionesRoles;
import restricciones.RestriccionesTiposPsicologicos;

public class Analisis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ParserArchgrupos parser = new ParserArchgrupos("C:\\Users\\gatti\\Desktop\\Ignacio\\Ing. Sistemas\\Proyecto Beca\\Grupos\\salida8428800.csv");
		
		DBConfigurador conf = new DBConfigurador("C:\\Users\\gatti\\Desktop\\Ignacio\\Ing. Sistemas\\Proyecto Beca\\perfiles.csv");
		
		List<Usuario> usuarios = conf.getUsuarios();
		ConjuntoGrupo grupos = parser.getGrupos();
		int cantIntegrantes = parser.getCantIntegrantes();
		int cantGrupos = parser.getCantGrupos();
		System.out.println("Cant grupos : " + cantGrupos);
		System.out.println("Cant integrantes: " + cantIntegrantes);
		System.out.println();
		for (Grupo g : grupos.getGrupos() ){
			System.out.println(g);	
			System.out.println("Tamanio : " + g.getIntegrantes().size());
		}
		grupos.calcPen();
		System.out.println("Penalizacion: " + grupos.getPenalizacion() );
		
		System.out.println();
		System.out.println("-----------------------------------------------------------------------");
		
//		RestriccionesRoles restriccionesRoles = 
//				new RestriccionesRoles(cantIntegrantes, usuarios);

		RestriccionesTiposPsicologicos restriccionesTiposPsico = 
				new RestriccionesTiposPsicologicos(cantIntegrantes, cantGrupos, usuarios);

		RestriccionesCorreMumTiposPsico restriccionesCorrelacion = 
				new RestriccionesCorreMumTiposPsico(usuarios, cantGrupos);
		
		List< Usuario[] > listaCorrelacion = new ArrayList<>();
		listaCorrelacion.add( restriccionesCorrelacion.getUsuariosPorCorrelacion() );
		
		RestriccionesRolBalanceado restriccionesRolBalanceado = 
				new RestriccionesRolBalanceado(usuarios, cantGrupos);
		
		List< Usuario[] > listaBalanceados = new ArrayList<>();
		listaBalanceados.add( restriccionesRolBalanceado.getUsuariosPorRolesBalanceados() );
		System.out.println("------------------------Lista de usuarios Correlacionados--------------------------------");
		
		for( Usuario u : restriccionesRolBalanceado.getUsuariosPorRolesBalanceados() ){
			System.out.println( u );
		}
		
		AnalisisDiscriminacionTipo discriminacionPorRoles = 
				new AnalisisDiscriminacionTipo(listaBalanceados, "Analisis por Correlacion");
		int i  = 0;
		for (Grupo g : grupos.getGrupos() ){
			System.out.println("Analisis Grupo " + i);
			System.out.println();
			System.out.println( discriminacionPorRoles.analizarGrupo(g) );
			System.out.println();
			i++;
		}
		

	}

}
