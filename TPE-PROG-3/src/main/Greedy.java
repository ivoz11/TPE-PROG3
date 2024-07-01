package main;

import java.util.Collections;
import java.util.List;

public class Greedy {
	
	private Solucion mejorSolucion;
    private int cantidadCandidatosConsiderados;

    public Greedy(List<Procesador>procesadores) {
    	this.mejorSolucion = new Solucion(procesadores);
        cantidadCandidatosConsiderados = 0;
    }

    public int getCantidadCandidatosConsiderados() {
        return cantidadCandidatosConsiderados;
    }

    // Complejidad: O(n * m) donde n es el número de tareas y m es el número de procesadores
    public Solucion resolver(int tiempoMaximoNoRefrigerado, List<Tarea> listaTareas) {
    	
    	Collections.sort(listaTareas);
    	
        // Inicializar las asignaciones y los tiempos de procesadores
        while (!listaTareas.isEmpty()) {
            Tarea mejorTarea = listaTareas.get(0);
            Procesador procesadorOptimo = this.getProcesadorOptimo(mejorTarea,tiempoMaximoNoRefrigerado);
            
            if(procesadorOptimo != null) {
            	  this.mejorSolucion.asignarTarea(procesadorOptimo, mejorTarea);
                  listaTareas.remove(mejorTarea);
            }else {
            	
            	return null;
            }
            this.cantidadCandidatosConsiderados++;
        }
        
        return mejorSolucion;
    }
    
    private Procesador getProcesadorOptimo(Tarea t, int tiempoMaximoNoRefrigerado) {
    	Procesador procesadorOptimo = null;
    	
    	int menorTiempo = Integer.MAX_VALUE;
    	
    	for(Procesador p : this.mejorSolucion.getAsignacion().keySet()) {
    		int tiempoActual  = this.mejorSolucion.calcularTiempoEjecucionTareas(p);
    		
    		if(this.mejorSolucion.puedeAsignar(p, t, tiempoMaximoNoRefrigerado)&& tiempoActual < menorTiempo) {
    			menorTiempo = tiempoActual;
    			procesadorOptimo = p;
    		}
    	}
    	return procesadorOptimo;
    }
}
