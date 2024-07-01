package main;

import java.util.List;
import java.util.Stack;

public class Backtracking {
	
	private Solucion mejorSolucion;
	private int cantidadEstadosGenerados;
	

	public Backtracking() {
		this.mejorSolucion = new Solucion();
		this.cantidadEstadosGenerados = 0;
	}

	public Backtracking(List<Procesador> procesadores) {
		this.mejorSolucion = new Solucion(procesadores);
		this.cantidadEstadosGenerados = 0;
	}

	public int getCantidadEstadosGenerados() {
		return this.cantidadEstadosGenerados;
	}

	// Complejidad: en el peor de los casos O(m^n) donde m es el número de procesadores y n es el número de tareas
	public Solucion resolver(int tiempoMaximoNoRefrigerados, List<Procesador> listaProcesadores, Stack<Tarea> pilaTareas) {
		Solucion asignacionActual = new Solucion(listaProcesadores);
		
		int cantTotalTareas = pilaTareas.size();

		this.backtrackingAsignarTareas(tiempoMaximoNoRefrigerados, pilaTareas, listaProcesadores, asignacionActual,cantTotalTareas);

		return this.mejorSolucion;
	}

	private void backtrackingAsignarTareas(int tiempoMaximoNoRefrigerados, Stack<Tarea> pilaTareas, List<Procesador> listaProcesadores,
			Solucion asignacionActual,  int cantTotalTareas) {

		this.cantidadEstadosGenerados++;

		if (pilaTareas.empty()){
			if (this.mejorSolucion == null || (this.mejorSolucion.getTiempoMaximo() > asignacionActual.getTiempoMaximo() && cantTotalTareas == asignacionActual.getCantTareasAsignadas())){
				this.mejorSolucion.setAsignacion(asignacionActual);
			}
		} else {
			Tarea tareaActual = pilaTareas.pop(); // Tomar la tarea actual de la lista de tareas y la borro

			for (Procesador procesadorActual : listaProcesadores) {

				if (asignacionActual.puedeAsignar(procesadorActual, tareaActual, tiempoMaximoNoRefrigerados)) {
					// Asignar la tarea al procesador
					asignacionActual.asignarTarea(procesadorActual, tareaActual);

					if ( this.mejorSolucion == null || (asignacionActual.getTiempoMaximo() < this.mejorSolucion.getTiempoMaximo())){
						backtrackingAsignarTareas(tiempoMaximoNoRefrigerados,pilaTareas,listaProcesadores, asignacionActual,cantTotalTareas);
					}
					// Desasignar la tarea cuando regresa de la recursión
					asignacionActual.desasignarTarea(procesadorActual,tareaActual);
				}
			}
			pilaTareas.push(tareaActual);
		}
	}
}

