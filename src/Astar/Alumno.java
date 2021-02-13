package Astar;
import java.util.List;

public class Alumno {

	public Escuela escuela;
	public Node parada;

	public Alumno(Escuela destination){
		this.escuela = destination;
	}
	
	public Escuela getDropOffLocation(){
		return escuela;}
	
	public String toString(){
		return escuela.toString();
	}
	
	public String Bajan(){
		return escuela.toString();
	}
	
	public String Suben(){
		return escuela.toString();
	}
	
}
