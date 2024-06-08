package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solucion {
    private Map<Procesador, List<Tarea>> asignacion;
    private int tiempoMaximo;
    private int costoSolucion;

    public Solucion(Map<Procesador, List<Tarea>> asignacion, int tiempoMaximo, int costoSolucion) {
        this.asignacion = new HashMap<>(asignacion);
        this.tiempoMaximo = tiempoMaximo;
        this.costoSolucion = costoSolucion;
    }

    public Map<Procesador, List<Tarea>> getAsignacion() {
        return asignacion;
    }
    
    public void actualizarAsignacion(Map<Procesador, List<Tarea>> nuevaAsignacion) {
    	   this.asignacion = new HashMap<>();
           for (Map.Entry<Procesador, List<Tarea>> entry : nuevaAsignacion.entrySet()) {
               // Crea una nueva lista para cada procesador para asegurar una copia profunda
               this.asignacion.put(entry.getKey(), new ArrayList<>(entry.getValue()));
           }
	}

	public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    public void setTiempoMaximo(int tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public int getCostoSolucion() {
        return costoSolucion;
    }

    public void setCostoSolucion(int costoSolucion) {
        this.costoSolucion = costoSolucion;
    }

    @Override
    public String toString() {
        return "Cada procesador con las tareas asignadas: \n" + this.getAsignacion() +
                "\n\nTiempo máximo de ejecución: " + this.getTiempoMaximo() +
                "\n\nCosto de la solución: " + this.getCostoSolucion();
    }
}