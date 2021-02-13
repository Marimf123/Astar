package Astar;

import java.util.ArrayList;
import java.util.List;

public class Node{
	/**
	 * name of location
	 */
	String name;
	List<Alumno> alumnos;
	Escuela escuela;
	 public List<Edge> adjacencies;
	 int contador;
	 Node parent;
	 Node hijo;

	public Node(String name){
		this.name = name;
		alumnos= new ArrayList<Alumno>();
		adjacencies=new ArrayList<Edge>();
	}
	
	public String toString(){
		return name;
	}
	
}
