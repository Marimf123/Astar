package Astar;

import java.util.ArrayList;
import java.util.List;
//VAlores de los estados
public class Estado {
	Node posicion;
	int deep;
	int g;
	int h;
	int f;	
	List<Alumno> entregados;
	List <Alumno> recogidos;
	Estado parent;
	int Capacidad;
	//LOs que subeb y bajan en ese estado
	List<Alumno> suben;
	List <Alumno> bajan;
	int distancia;
	int nodosexpandidos;
	
	public Estado() {
		entregados = new ArrayList<Alumno>();
		recogidos = new ArrayList<Alumno>();
		suben = new ArrayList<Alumno>();
		bajan = new ArrayList<Alumno>();
	}
}
