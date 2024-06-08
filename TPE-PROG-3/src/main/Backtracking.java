package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Backtracking {

	private int cantidadEstadosGenerados;

	public Backtracking() {
		this.cantidadEstadosGenerados = 0;
	}
	public int getCantidadEstadosGenerados() {
		return cantidadEstadosGenerados;
	}

	public Solucion resolver(int tiempoMaximoNoRefrigerados,ArrayList<Procesador>listaProcesadores,ArrayList<Tarea> listaTareas) {

		int nroTareaActual = 0;
		
		Map<Procesador, List<Tarea>> asignacionInicial = new HashMap<>();

		for (Procesador procesador : listaProcesadores) {
			asignacionInicial.put(procesador, new ArrayList<>());
		}

		Solucion s = new Solucion(asignacionInicial,Integer.MAX_VALUE, 0); // Inicializamos con valores máximos para encontrar una solución mejor

		backtrackingAsignarTareas(nroTareaActual,tiempoMaximoNoRefrigerados,listaTareas,listaProcesadores,asignacionInicial,s);

		return s;

	}

	private void backtrackingAsignarTareas(int nroTareaActual,int tiempoMaximoNoRefrigerados,ArrayList<Tarea>listaTareas,ArrayList<Procesador>listaProcesadores, 
			Map<Procesador, List<Tarea>>asignacionActual,Solucion mejorSolucion) {
		
		this.cantidadEstadosGenerados++;
		mejorSolucion.setCostoSolucion(this.cantidadEstadosGenerados);
		
		if(nroTareaActual == listaTareas.size()) {

			int tiempoActual = calcularTiempoEjecucion(asignacionActual); //CALCULA EL TIEMPO DE EJECUCION ACTUAL

			if(tiempoActual < mejorSolucion.getTiempoMaximo()) {
				mejorSolucion.actualizarAsignacion(asignacionActual);
				mejorSolucion.setTiempoMaximo(tiempoActual);
			}


		}else {
			Tarea tareaActual = listaTareas.get(nroTareaActual); //Tomo la tarea a ctual de la lista de tareas 
			
			for (Procesador procesadorActual : listaProcesadores) {
				//ESTABA ACA HOY TAREA ACTUAL
				ArrayList<Tarea>tareasAsignadas = (ArrayList<Tarea>) asignacionActual.get(procesadorActual); //Tomo la lista de tareas que se le asigno hasta el momento

				if(puedoAsignar(tareaActual,procesadorActual,tareasAsignadas,tiempoMaximoNoRefrigerados)) {
					asignarTarea(tareaActual, procesadorActual, asignacionActual);//ASIGNA LA TAREA AL PROCESADOR
					backtrackingAsignarTareas(nroTareaActual +1,tiempoMaximoNoRefrigerados,listaTareas,listaProcesadores,asignacionActual,mejorSolucion); //LLAMO RECURSIVAMENTE PARA LA SIGUIENTE TAREA
					desasignarTarea(tareaActual, procesadorActual, asignacionActual); //DESASIGNA LA TAREA CUANDO REGRESA DE LA RECURSION
				}
			}
		}
	}
	private int calcularTiempoEjecucion(Map<Procesador,List<Tarea>>asignacionActual) {
		int maxTiempo = 0;

		for(List<Tarea> tareas: asignacionActual.values()) {
			int tiempoTotal = calcularTiempoTotal(tareas);
			if(tiempoTotal > maxTiempo) {
				maxTiempo = tiempoTotal;
			}
		}
		return maxTiempo;
	}


	private void desasignarTarea(Tarea tarea, Procesador procesador, Map<Procesador, List<Tarea>> asignacionActual) {
		asignacionActual.get(procesador).remove(tarea);
	}
	private void asignarTarea(Tarea tarea, Procesador procesador, Map<Procesador, List<Tarea>> asignacionActual) {
		asignacionActual.get(procesador).add(tarea);

	}
	//VERIFICA SI SE PUEDE ASIGNAR UNA TAREA A UN PROCESADOR DADO LAS RESTRICCIONES
	private boolean puedoAsignar(Tarea tarea, Procesador procesador,ArrayList<Tarea>tareasAsignadas, int tiempoMaximoNoRefrigerados) {

		if(tareasAsignadas == null) tareasAsignadas = new ArrayList<>();

		//VERIFICA SI EL PROCESADOR YA TIENE MAS DE DOS TAREAS CRITICAS ASIGNADAS
		if (tieneMasDeDosTareasCriticas(tareasAsignadas)) {
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
	
	 public boolean tieneMasDeDosTareasCriticas(ArrayList<Tarea> tareasAsignadas) {
			int cantidadTareasCriticas = 0;
			for(Tarea tarea: tareasAsignadas) {
				if(tarea.getCritica() == true) {
					cantidadTareasCriticas++;
					if(cantidadTareasCriticas >= 2) {
						return true;
					}
				}
			}
			return false;
		}
	
	private int calcularTiempoTotal(List<Tarea> tareas) {
		int tiempoTotal = 0;

		for(Tarea t:tareas) {
			tiempoTotal += t.getTiempo();
		}
		return tiempoTotal;
	}

	public String toString() {
		return "Backtracking{" + super.toString() +
				"Costo de la solución (cantidad de estados generados): " + cantidadEstadosGenerados +
				'}';
	}
}