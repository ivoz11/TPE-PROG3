package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backtracking {

	//VARIABLES DE INSTACIA PARA ALMACENAR LA ASIGNACION DE TAREAS, TIEMPO MAXIMO EJECUCION Y LA CANTIDAD DE ESTADOS GENERADOS
	private HashMap<Procesador,ArrayList<Tarea>>asignacionTareas;
	private int tiempoMaximoEjecucion;
	private int cantidadEstadosGenerados;	


	// CONSTRUCTOR QUE INICIALIZA LAS VARIABLES DE INSTANCIA
	public Backtracking() {
		this.asignacionTareas = new HashMap<>();
		this.tiempoMaximoEjecucion = Integer.MAX_VALUE; //SE INICIALIZA CON UN VALOR ALTO PARA PODER ENCONTRAR EL MINIMO
		this.cantidadEstadosGenerados = 0;
		
	}

	//METODO PUBLICO PARA ASIGNAR TAREAS A LOS PROCESADORES
	public void asignarTareas(HashMap<String,Procesador> procesadores, HashMap<String,Tarea> tareas, int tiempoMaximoNoRefrigerados) {
		int tareaActual = 0; // INICIALIZO LA POSICION LA POSICION DE LA TAREA ACTUAL

		//CONVIERTO LOS VALORES DE LOS HASHMAP DE ENTRADA EN ARRAYLIST
		ArrayList<Procesador> listaProcesadores = new ArrayList<>(procesadores.values()) ;
		ArrayList<Tarea> listaTareas = new ArrayList<>(tareas.values());

		//INICIALIZO EL HASHMAP PARA LA ASIGNACION ACTUAL DE TAREAS
		HashMap<Procesador, ArrayList<Tarea>> asignacionTareasActual = new HashMap<>();
		for (Procesador procesador : listaProcesadores) {
			asignacionTareasActual.put(procesador, new ArrayList<>()); // INICIALIZO CADA PROCESADOR CON UNA LISTA VACIA DE TAREAS
		}

		//LLAMO AL METODO DE BACKTRACKING PARA ENCONTRAR LA MEJOR ASIGNACION
		backtracking(listaProcesadores, listaTareas, tiempoMaximoNoRefrigerados, tareaActual, asignacionTareasActual); // LLAMO LA FUNCION BACKTRACKING

	}
	//METODO BACKTRACKING PARA PODER EXPLORAR TODAS LAS POSIBLES ASIGNACIONES DE TAREAS
	private void backtracking(ArrayList<Procesador> listaProcesadores, ArrayList<Tarea> listaTareas, int tiempoMaximoNoRefrigerados, int tareaActual, HashMap<Procesador, ArrayList<Tarea>> asignacionTareasActual) {

		cantidadEstadosGenerados++; //INCREMENTA EL CONTADOR DE ESTADOS GENERADOS

		//VERIFICA SI SE ASIGNARON TODAS LAS TAREAS
		if (tareaActual == listaTareas.size()) {

			int tiempoActual = calcularTiempoEjecucion(asignacionTareasActual); //CALCULA EL TIEMPO DE EJECUCION ACTUAL

			//SI EL TIEMPO ACTUAL ES MEJOR QUE EL TIEMPO MAXIMO DE EJECUCION, ACTUALIZA LA MEJOR ASIGNACION
			if (tiempoActual < tiempoMaximoEjecucion) {
				tiempoMaximoEjecucion = tiempoActual;
				asignacionTareas = copiaAsignacion(asignacionTareasActual); //CREA UNA COPIA DE LA ASIGNACION ACTUAL COMO LA MEJOR ASIGNACION ENCONTRADA
			}
		} else {
			//TOMO LA TAREA ACTUAL DE LA LISTA DE TAREAS
			Tarea tarea = listaTareas.get(tareaActual); 

			// ITERA SOBRE TODOS LOS PROCESADORES PARA INTENTAR ASIGNARLES LA TAREA ACTUAL
			for (Procesador procesador : listaProcesadores) {

				//VERIFICA SI PUEDO ASIGNAR LA TAREA AL PROCESADOR ACTUAL
				if (puedoAsignar(tarea, procesador, asignacionTareasActual.get(procesador), tiempoMaximoNoRefrigerados)) {
					asignarTarea(tarea, procesador, asignacionTareasActual);//ASIGNA LA TAREA AL PROCESADOR
					backtracking(listaProcesadores, listaTareas, tiempoMaximoNoRefrigerados, tareaActual + 1, asignacionTareasActual); //LLAMO RECURSIVAMENTE PARA LA SIGUIENTE TAREA
					desasignarTarea(tarea, procesador, asignacionTareasActual); //DESASIGNA LA TAREA CUANDO REGRESA DE LA RECURSION
				}
			}
		}
	}
	//VERIFICA SI SE PUEDE ASIGNAR UNA TAREA A UN PROCESADOR DADO LAS RESTRICCIONES
	private boolean puedoAsignar(Tarea tarea, Procesador procesador, ArrayList<Tarea> tareasAsignadas, int tiempoMaximoNoRefrigerados) {
		if(tareasAsignadas == null) tareasAsignadas = new ArrayList<>();

		//VERIFICA SI EL PROCESADOR YA TIENE MAS DE DOS TAREAS CRITICAS ASIGNADAS
		if (procesador.tieneMasDeDosTareasCriticas(tareasAsignadas)) {
			return false;
		}
		//VERIFICA SI EL PROCESADOR ES REFRIGERADO
		if (procesador.getRefrigerado()) {
			return true;
		}//SI ES NO REFRIGUERADO 
		else {
			//CALCULA EL TIEMPO TOTAL DE EJECUCION SI SE ANADE LA NUEVA TAREA
			int tiempoTotal = calcularTiempoTotal(tareasAsignadas) + tarea.getTiempo();
			return tiempoTotal <= tiempoMaximoNoRefrigerados; //VERIFICA SI EL TIEMPO TOTAL NO EXCEDE EL MAXIMO PERMITIDO SIN REFRIGERACION
		}
	}

	//ASIGNA UNA TAREA A UN PROCESADOR 
	private void asignarTarea(Tarea tarea, Procesador procesador, HashMap<Procesador, ArrayList<Tarea>> asignacionTareasActual) {
		asignacionTareasActual.get(procesador).add(tarea);
		// tiempoMaximoEjecucion = calcularTiempoEjecucion(asignacionTareasActual);
	}

	//DESASIGNA UNA TAREA DE UN PROCESADOR 
	private void desasignarTarea(Tarea tarea, Procesador procesador, HashMap<Procesador, ArrayList<Tarea>> asignacionTareasActual) {
		asignacionTareasActual.get(procesador).remove(tarea);
		//tiempoMaximoEjecucion = calcularTiempoEjecucion(asignacionTareasActual);
	}

	//CALCULA EL TIEMPO TOTAL DE EJECUCION DE UNA LISTA DE TAREAS
	private int calcularTiempoTotal(ArrayList<Tarea> tareas) {
		int tiempoTotal = 0;
		for(Tarea tarea:tareas) {
			tiempoTotal += tarea.getTiempo();
		}
		return tiempoTotal;
	}

	//CALCULA EL TIEMPO TOTAL DE EJECUCION ENTRE TODOS LOS PROCESADORES
	private int calcularTiempoEjecucion(HashMap<Procesador, ArrayList<Tarea>> asignacionTareasActual) {
		int maxTiempo = 0;
		for(ArrayList<Tarea> tareas: asignacionTareasActual.values()) {
			int tiempoTotal = calcularTiempoTotal(tareas);
			if(tiempoTotal > maxTiempo) {
				maxTiempo = tiempoTotal;
			}
		}
		return maxTiempo;
	}
	//CREA UNA COPIA DE LA ASIGNACION DE TAREAS
	private HashMap<Procesador, ArrayList<Tarea>> copiaAsignacion(HashMap<Procesador, ArrayList<Tarea>> original) {
		HashMap<Procesador, ArrayList<Tarea>> copia = new HashMap<>();
		for (Map.Entry<Procesador, ArrayList<Tarea>> entry : original.entrySet()) {
			copia.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return copia;
	}

	//IMPRIME LA MEJOR ASIGNACION DE TAREAS ENCONTRADA
	public void imprimirMejorAsignacion() {

		System.out.println("Mejor Asignación de Tareas:");
		for (Map.Entry<Procesador, ArrayList<Tarea>> entry : asignacionTareas.entrySet()) {
			Procesador procesador = entry.getKey();
			ArrayList<Tarea> tareasAsignadas = entry.getValue();
			
			System.out.println("Procesador: "+procesador.getId_procesadores());
			System.out.println("Se le asignaron las tareas: ");
			for (Tarea tarea : tareasAsignadas) {
				
				System.out.println("\tNombre tarea: " + tarea.getNombre() + " - Tiempo ejecucion: " + tarea.getTiempo());
			}
		}
		System.out.println("Tiempo Máximo de Ejecución: " + tiempoMaximoEjecucion);
		System.out.println("Cantidad de Estados Generados: " + cantidadEstadosGenerados);
	}
}
