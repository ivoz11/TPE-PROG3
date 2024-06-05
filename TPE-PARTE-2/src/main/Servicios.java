package main;
import utils.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
	// Mapa para almacenar tareas identificadas por su nombre
    private Map<String, Tarea> tareas;
    
 // Mapa para almacenar procesadores identificados por su nomo
    private Map<String, Procesador> procesadores;
    
    // Lista para almacenar tareas con criticidad falsa
    private List<Tarea> tareasFalse;
    
  // Lista para almacenar tareas con criticidad verdadera
    private List<Tarea> tareasTrue;
  
    // Árbol de búsqueda binaria para almacenar tareas por prioridad
    private BinarySearchTreeTarea tareaBST;
    
    
    /**ACOMODAR
     * Complejidad temporal del constructor: O(T + P)
     * donde T es el número de tareas y P es el número de procesadores.
     */
    public Servicios(String pathTareas, String pathProcesadores) {  
        //Leer archivos CSV
    	CSVReader reader = new CSVReader();
    	
        procesadores = reader.readProcessors(pathProcesadores);
        tareas =  reader.readTasks(pathTareas);
    	
    	tareasFalse = new LinkedList<>();
        tareasTrue = new LinkedList<>();
        
        tareaBST = new BinarySearchTreeTarea();
        
        this.dividirTareasPorCriticidad(tareas);
    }

    /**
     * Complejidad temporal del servicio 1: O(1)
     */
    public Tarea servicio1(String ID) {
        return tareas.get(ID);
    }


    /**
     * Complejidad temporal del servicio 2: O(1)
     *
     * Guardamos todas las tareas TRUE en una estructura y las FALSE en otra,
     * aumentando la memoria para poder bajar la complejidad, aunque sabemos que
     * después tendremos mayor gasto de mantenimiento, porque al hacer un DELETE
     * por ejemplo, tendremos que sacarlos del Map y de la Lista.
     */
    public List<Tarea> servicio2(boolean esCritica) {
    	
        if(esCritica == true) {
        	return tareasTrue;
        }else {
        	return tareasFalse;
        }
    }
    /**
     * Complejidad temporal del servicio 3: O(N)
     * donde N es el número de nodos del árbol.
     */
    public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
        return tareaBST.getTareasEnRango(prioridadInferior, prioridadSuperior);
    }
    //Este metodo es para agregar las tareas a dos diferentes listas depende el tipo de criticidad
    private void dividirTareasPorCriticidad(Map<String, Tarea> mapTareas) {	
    	ArrayList<Tarea> listaTareas = new ArrayList<>(mapTareas.values());
    	for(int i = 0; i < listaTareas.size(); i++) {
    	   if(listaTareas.get(i).getCritica() == true) {
    	    	tareasTrue.add(listaTareas.get(i));
    	    }else {
    	    	tareasFalse.add(listaTareas.get(i));
    	    }
    	}
    }
 
}