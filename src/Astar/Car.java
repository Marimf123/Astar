package Astar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Car class includes attributes and state information about Car.
 */
public class Car {

	Node currentLocation;
	LinkedList<Node> route;
	List<Alumno> alumno;

	public int Capacidad;

	Estado distance;
	int Ocupado;

	public Car(Node currentLocation, int Capacidad) {
		this.Capacidad=Capacidad;
		this.distance = distance;
		this.currentLocation = currentLocation;
		alumno = new ArrayList<Alumno>();
	}

	public void asignarAlumno(Alumno alumno1) {
		if (Capacidad > 0) {
			alumno.add(alumno1);
			currentLocation.alumnos.remove(alumno1);
		}
		Capacidad = Capacidad - 1;
	}

	public void removeAlumno() {
		for (int i = 0; i < alumno.size(); i++) {
			if (alumno.get(i).escuela.placement.equals(currentLocation)) {
				currentLocation.alumnos.add(alumno.get(i));
				alumno.remove(i);
				Capacidad = Capacidad + 1;
			}
		}
	}

	public String toString() {
		return currentLocation + " "/* currentLocation.toString() */;
	}

}
