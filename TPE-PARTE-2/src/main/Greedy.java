package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Greedy{
	
	//VARIABLES DE INSTACIA PARA ALMACENAR LA ASIGNACION DE TAREAS, TIEMPO MAXIMO EJECUCION Y LA CANTIDAD DE CANDIDATOS CONSIDERADOS
	private HashMap<Procesador,ArrayList<Tarea>>asignacionTareas;
	private int tiempoMaximoEjecucion;
	private int cantidadCandidatosConsiderados;	
	
	// CONSTRUCTOR QUE INICIALIZA LAS VARIABLES DE INSTANCIA
    public Greedy() {
    	this.asignacionTareas = new HashMap<>();
    	this.tiempoMaximoEjecucion = Integer.MAX_VALUE; //SE INICIALIZA CON UN VALOR ALTO PARA PODER ENCONTRAR EL MINIMO
		this.cantidadCandidatosConsiderados = 0;
    }
    
    
}
