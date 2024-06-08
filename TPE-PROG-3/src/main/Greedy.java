package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Greedy {
    private int cantidadCandidatosConsiderados;

    public Greedy() {
        cantidadCandidatosConsiderados = 0;
    }

    public int getCantidadCandidatosConsiderados() {
        return cantidadCandidatosConsiderados;
    }

    public Solucion resolver(int tiempoMaximoNoRefrigerado, ArrayList<Tarea> listaTareas, ArrayList<Procesador> listaProcesadores) {
        Map<Procesador, List<Tarea>> asignacion = new HashMap<>();

        for (Procesador procesador : listaProcesadores) {
            asignacion.put(procesador, new ArrayList<>());
        }

        Solucion s = new Solucion(asignacion, 0, 0); // Conjunto solución, inicialmente vacío

        for (Tarea tarea : listaTareas) {
            Procesador mejorProcesador = null;
            int minTiempo = Integer.MAX_VALUE;

            for (Procesador procesador : listaProcesadores) {
                cantidadCandidatosConsiderados++;

                if (puedeAsignar(tarea, procesador, tiempoMaximoNoRefrigerado, s.getAsignacion())) {
                    int tiempoProcesador = getTiempoProcesador(procesador, s.getAsignacion());

                    if (tiempoProcesador < minTiempo) {
                        minTiempo = tiempoProcesador;
                        mejorProcesador = procesador;
                    }
                }
            }

            if (mejorProcesador != null) {
                asignarTarea(tarea, mejorProcesador, s.getAsignacion());
            } else {
                System.out.println("No se pudo asignar la tarea: " + tarea);
            }
        }

        // Calcular el tiempo máximo de ejecución final
        int totalTiempoFinal = 0;

        for (Procesador procesador : listaProcesadores) {
            totalTiempoFinal = Math.max(totalTiempoFinal, getTiempoProcesador(procesador, s.getAsignacion()));
        }

        s.setTiempoMaximo(totalTiempoFinal);
        s.setCostoSolucion(cantidadCandidatosConsiderados);

        return s;
    }

    private boolean puedeAsignar(Tarea tarea, Procesador procesador, int tiempoMaximoNoRefrigerado, Map<Procesador, List<Tarea>> asignacion) {
        // Verificar si el procesador no refrigerado no excede el tiempo máximo permitido
        if (!procesador.getRefrigerado() && getTiempoProcesador(procesador, asignacion) + tarea.getTiempo() > tiempoMaximoNoRefrigerado) {
            return false;
        }

        // Verificar si la última tarea asignada al procesador es crítica
        // y si esta nueva tarea es crítica también
        if (tieneMasDeDosTareasCriticas(procesador, asignacion)) {
            return false;
        }

        return true;
    }

     public boolean tieneMasDeDosTareasCriticas(Procesador procesador, Map<Procesador, List<Tarea>> asignacion) {
			int cantidadTareasCriticas = 0;
			List<Tarea> tareasAsignadas = asignacion.get(procesador);
			
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
     
    private int getTiempoProcesador(Procesador procesador, Map<Procesador, List<Tarea>> asignacion) {
        int tiempoTotal = 0;

        for (Tarea tarea : asignacion.get(procesador)) {
            tiempoTotal += tarea.getTiempo();
        }

        return tiempoTotal;
    }

    private void asignarTarea(Tarea tarea, Procesador procesador, Map<Procesador, List<Tarea>> asignacion) {
        List<Tarea> tareasAsignadas = asignacion.get(procesador);
        tareasAsignadas.add(tarea);
    }
}