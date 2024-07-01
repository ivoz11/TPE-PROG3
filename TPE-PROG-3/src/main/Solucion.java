package main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Solucion {
	private Map<Procesador, List<Tarea>> asignacion;
	private int tiempoMaximo;

	public Solucion() {
		this.asignacion = new HashMap<>(asignacion);
		this.tiempoMaximo = Integer.MAX_VALUE;
	}
	   public Solucion(List<Procesador> procesadores){
	        this.tiempoMaximo = Integer.MAX_VALUE;
	        this.asignacion = new HashMap<>();
	        for (Procesador p : procesadores){
	            asignacion.put(p, new LinkedList<>());
	        }
	    }

	public Map<Procesador, List<Tarea>> getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(Solucion solucion) {
		   this.tiempoMaximo = solucion.tiempoMaximo;
	        Map<Procesador, List<Tarea>> copia = new HashMap<>();
	        for (Procesador p : solucion.asignacion.keySet()){
	            copia.put(p, new LinkedList<>());
	            for (Tarea t : solucion.asignacion.get(p)){
	                copia.get(p).add(t);
	            }
	        }
	        this.asignacion = copia;
	}
	public int getTiempoMaximo() {
		return tiempoMaximo;
	}

	public void setTiempoMaximo(int tiempoMaximo) {
		this.tiempoMaximo = tiempoMaximo;
	}

	//Se fija la cantidad de tareas asignadas de cada procesador para la solucion
	public int getCantTareasAsignadas() {
		int res = 0;
		for (Procesador p : this.asignacion.keySet()){
			res += this.asignacion.get(p).size();
		}

		return res;
	}

	//DESDE ACA

	//MODIFICADO
	protected void desasignarTarea(Procesador p,Tarea t) {
		if (this.asignacion.containsKey(p)){
			asignacion.get(p).remove(t);
			this.updateTiempoMaximo();
		}
	}

	//MODIFICADO
	protected void asignarTarea(Procesador p, Tarea t) {
		if (this.asignacion.containsKey(p) && !asignacion.get(p).contains(t)){
			List<Tarea> listaTareas = asignacion.get(p);
			listaTareas.add(t);
			this.asignacion.put(p,listaTareas);
			this.updateTiempoMaximo();
		}
	}
	private void updateTiempoMaximo(){
		int tiempoActual = 0;
		for (List<Tarea> list : asignacion.values()){
			int tiempoParcialTarea = 0;
			for (Tarea t : list){
				tiempoParcialTarea += t.getTiempo();
			}
			if (tiempoParcialTarea > tiempoActual){
				tiempoActual = tiempoParcialTarea;
			}
		}

		this.setTiempoMaximo(tiempoActual);
	}

	//MODIFICADO
	protected boolean puedeAsignar(Procesador p, Tarea t, int tiempoMaxNoRefrigerado) {

		if (!t.getCritica() && p.getRefrigerado()){
			return true;
		}else if(t.getCritica() && p.getRefrigerado()) {
			return !tieneMasDeDosTareasCriticas(p, t);
		}else if (t.getCritica() && !p.getRefrigerado()){
			return !tieneMasDeDosTareasCriticas(p, t) && calcularTiempoTotal(p, t, tiempoMaxNoRefrigerado);
		}

		return true;		
	}

	//MODIFICADO
	private boolean tieneMasDeDosTareasCriticas(Procesador p, Tarea t) {
		int tareasCriticas = 0;
		for (Tarea tActual : this.asignacion.get(p)){
			if (tActual.getCritica())
				tareasCriticas++;
		}
		return tareasCriticas > 2;
	}
	//MODIFICADO
	private boolean calcularTiempoTotal(Procesador p, Tarea t, int tiempoMaxNoRefrigerado){
		int tiempoProcesador = 0;
		for (Tarea tActual : this.asignacion.get(p)){
			tiempoProcesador += tActual.getTiempo();
		}
		return ((tiempoProcesador + t.getTiempo()) <= tiempoMaxNoRefrigerado);
	}

	// Complejidad: O(1) ya que solo se itera sobre los procesadores
	protected int calcularTiempoEjecucionTareas(Procesador p) {
		
		   int tiempo = 0;
	        List<Tarea> tareas = this.asignacion.get(p);
	        for (Tarea t : tareas){
	            tiempo += t.getTiempo();
	        }
	        return tiempo;
	}
	
	public int getTiempoEjecucionTareas(){
        int res = 0;
        if (this.asignacion != null){
            for (Procesador p : this.asignacion.keySet()){
                if (this.calcularTiempoEjecucionTareas(p) > res){
                    res = this.calcularTiempoEjecucionTareas(p);
                }
            }
        }
        return res;
    }
	//HASTA ACA

	  @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<Procesador, List<Tarea>> entry : asignacion.entrySet()) {
	            Procesador procesador = entry.getKey();
	            List<Tarea> tareas = entry.getValue();

	            sb.append(procesador.toString()).append(" -> "); // Información del procesador

	            for (Tarea tarea : tareas) {
	                sb.append(tarea.toString()); // Información de cada tarea
	            }

	            sb.append("\n"); // Salto de línea entre procesadores
	        }

	        sb.append("Tiempo máximo de ejecución: ").append(this.getTiempoEjecucionTareas());

	        return sb.toString();
	    }
}
