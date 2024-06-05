package main;

import utils.CSVReader;

public class Main {
    public static void main(String[] args) {
        Servicios servicios = new Servicios("./src/datasets/Tareas.csv","./src/datasets/Procesadores.csv");
        

        // Servicio 1  : Dado un identificador de tarea obtener  toda la información de la tarea asociada.
        System.out.println("Servicio 1");
        System.out.println(servicios.servicio1("T3"));
        System.out.println();

        // Servicio 2  : Permitir que el usuario decida si quiere  ver todas las tareas críticas
        // o no críticas y generar el listado apropiado resultante.
        System.out.println("Servicio 2");
        System.out.println(servicios.servicio2(false));
        System.out.println();

        // Servicio 3  : Obtener todas las tareas entre 2 niveles  de prioridad indicados.
        System.out.println("Servicio 3");
        System.out.println(servicios.servicio3(69,100));
        System.out.println();
        
        //Instancio el CSVReader para poder traer la lista de procesadores y de tareas
        CSVReader reader = new CSVReader();
        //Instancio la clase backtracking
        Backtracking backtracking = new Backtracking();
        //Llamo la funcion asignar tareas
        backtracking.asignarTareas(reader.readProcessors("./src/datasets/Procesadores.csv"),reader.readTasks("./src/datasets/Tareas.csv"),300);
        //Imprimo la mejor asignacion que consiguio el backtracking
        backtracking.imprimirMejorAsignacion();
    }
}