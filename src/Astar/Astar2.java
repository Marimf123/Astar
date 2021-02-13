package Astar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Astar2 {
	public static void main(String[] args) throws Exception {
		  if(args.length == 0) {
	            System.exit(0);}
		  //Se sacan los valores pasados
		  String programa=args[0];
		  String heuristica1=args[1];
		  int heuristica=Integer.parseInt(heuristica1);
		  System.out.println(programa);
		//SE crean los alumnos, coche, escuelas y adyacencias del problema
		  List<Alumno> waitingList = new ArrayList<Alumno>();
	
		Car bus = null;
		List<Edge> edges = new ArrayList<Edge>();
		List<Escuela> escuelas = new ArrayList<Escuela>();
		BufferedReader bf=null;
		try {
		bf = new BufferedReader(new FileReader(programa));
		}catch (FileNotFoundException e) {
            System.out.println("File Not Found.");

        }
		
		int lNumeroLineas = 0;
//Se saca la informacion del archivo
		StringTokenizer st = new StringTokenizer(bf.readLine());
		int contador = st.countTokens();
		Node nodes[] = new Node[st.countTokens()];

		for (int i = 0; i < contador; i++) {
			nodes[i] = new Node(st.nextToken());
		}
		int n = contador;
		while (n > 0) {
			st = new StringTokenizer(bf.readLine());
			String b = st.nextToken();
			for (int i = 0; i < nodes.length; i++) {
				b = st.nextToken();
				if (isNumeric(b)) {
					int numEntero = Integer.parseInt(b);
					Edge e = new Edge(nodes[lNumeroLineas], nodes[i], numEntero);
					edges.add(e);
					nodes[lNumeroLineas].adjacencies.add(e);
				}
			}

			lNumeroLineas++;
			n = n - 1;
		}

		// Escuelas
		st = new StringTokenizer(bf.readLine());
		int line = st.countTokens();
		int c = 0;
		String stringCole = null;
		String stringNodo = null;
		while (line > 0) {
			String string = st.nextToken();
			if (line % 2 == 0) {
				String[] parts1 = string.split(":");
				stringCole = parts1[0];

				// System.out.println(st.countTokens());
				// System.out.println(stringCole);
			} else {
				String[] parts2 = string.split(";");
				stringNodo = parts2[0];
				// System.out.println(stringNodo);
			}
			c++;
			if (c == 2) {
				for (int i = 0; i < nodes.length; i++) {
					if (nodes[i].name.equals(stringNodo)) {
						Escuela es = new Escuela(stringCole, nodes[i]);
						escuelas.add(es);
						nodes[i].escuela = es;
						c = 0;
						break;
					}
				}

			}
			line = line - 1;
		}

		Scanner scanner = new Scanner(bf.readLine());
		String al = scanner.nextLine();
		// System.out.println(al);
		String[] tokens = al.split(";");

		String[][] array = new String[tokens.length][];

		for (int i = 0; i < tokens.length; i++) {
			array[i] = tokens[i].split(":|\\,|\\ ");
		}

		int[] number1 = new int[tokens.length];

		for (int i = 0; i < tokens.length; i++) {
			for (int j = 0; j < array[i].length; j++) {

				for (int s = 0; s < nodes.length; s++) {
					if (nodes[s].name.equals(array[i][j])) {
						number1[i] = s;
						// System.out.println(s);
					}
				}

				for (int s = 0; s < escuelas.size(); s++) {
					if (escuelas.get(s).escuela.equals(array[i][j])) {
						if (isNumeric(array[i][j - 1])) {
							int numEntero = Integer.parseInt(array[i][j - 1]);
							while (numEntero > 0) {
								Alumno alum1 = new Alumno(escuelas.get(s));
								waitingList.add(alum1);
								nodes[number1[i]].alumnos.add(alum1);
								alum1.parada = nodes[number1[i]];
								numEntero--;
							}
						}
					}
				}

			}

		}

		// Bus
		st = new StringTokenizer(bf.readLine());
		String cole = st.nextToken();
		String parada = st.nextToken();
		int Capacidad = Integer.parseInt(st.nextToken());
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].name.equals(parada)) {
				bus = new Car(nodes[i], Capacidad);
				break;
			}
		}
//Se crea el estado inicial
		//Se pone el cronometro
		long inicio = System.currentTimeMillis();

		Estado inicial = new Estado();
		inicial.deep = 0;
		inicial.posicion = bus.currentLocation;
		inicial.g = 0;
		inicial.f = inicial.g + inicial.h;
		inicial.parent = null;
		inicial.Capacidad = bus.Capacidad;
		inicial.h = H_Score(nodes, inicial, waitingList, heuristica, inicial);

		if (nodes.length == 0) {
			System.out.println("Hay un error en el formato de los nodos");
		}
		if (escuelas.isEmpty()) {
			System.out.println("Hay un error en el formato de las escuelas");
		}
		if (waitingList.isEmpty()) {
			System.out.println("Hay un error en el formato de los alumnos");
		}

		//Se saca el problema con el metodo
		Estado finEstate=AstarSearch(inicial, bus, nodes, waitingList, escuelas, heuristica);

		scanner.close();
		bf.close();
		long fin = System.currentTimeMillis();
		double tiempo = (double) ((fin - inicio));
		//Se crean los archivos de destino
		String camino=printPath(finEstate, waitingList, escuelas);
		byte[] contentInBytes = camino.getBytes();
		 int b=printNodosExpandidos(finEstate, waitingList, escuelas);
		String ruta =programa+".output";
        File archivo = new File(ruta);
		if(archivo.exists()) {
			archivo.delete();
        } 
        
        try {
            File inFile = new File(programa);
            File outFile = new File(ruta);

            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile,true);

            int c1;
            while( (c1 = in.read() ) != -1) {
                out.write(c1);}
            out.write(contentInBytes);

            in.close();
            out.close();
        } catch(IOException e) {
            System.err.println("Hubo un error en el archivo de output!!!");
        } 
     
    
        String rutastat =programa+".statistics";
        String estadisticas="Tiempo total: " + (double)(tiempo / 1000)+"\n"+"Coste total: " + finEstate.g+"\n"+"Paradas visitadas: " + b
        		+"\n"+"Nodos expandidos: " +finEstate.nodosexpandidos;
		byte[] contentInBytesstatic = estadisticas.getBytes();
        File archivostatic = new File(rutastat);
		if(archivostatic.exists()) {
			archivostatic.delete();
        } 
		BufferedWriter bw;
        try {
        	 bw = new BufferedWriter(new FileWriter(rutastat));
             bw.write(estadisticas);
             bw.close();
        } catch(IOException e) {
            System.err.println("Hubo un error en el archivo de statitistic!!!");
        } 
	}

	public static Estado AstarSearch(Estado source, Car bus, Node[] nodes, List<Alumno> waitingList,
			List<Escuela> escuelas, int heuristica) {
		// Se crean las dos listas
		Set<Estado> closeList = new HashSet<Estado>();
		PriorityQueue<Estado> openList = new PriorityQueue<Estado>(20, new Comparator<Estado>() {
			// override compare method
			public int compare(Estado i, Estado j) {
				if (i.f > j.f) {
					return 1;
				}

				else if (i.f < j.f) {
					return -1;
				}

				else {
					return 0;
				}
			}

		});

		openList.add(source);
		boolean found = false;

		while (!openList.isEmpty() && !(found)) {

			// Se saca de la lista abierta
			Estado current = openList.poll();
			// Se mete en la lista cerrada
			closeList.add(current);
//Si llegamos al estado final
			if ((current.posicion.equals(source.posicion)) && (current.Capacidad == bus.Capacidad)
					&& (current.entregados.size() == waitingList.size())) {
				found = true;
				current.nodosexpandidos=closeList.size();
				return current;

			} else {
				//Se expande nodos si no se llega al final
				Expand(current, openList, closeList, nodes, bus, waitingList, heuristica, source);
			}
		}

		if (!found) {
			System.out.println("\nNo solution!");
		}

		return null;
	}

	public static void Expand(Estado estado, PriorityQueue<Estado> openList, Set<Estado> closeList, Node[] nodes,
			Car bus, List<Alumno> waitingList, int heuristica, Estado inicial) {

		// bajar niño si hay en la parada
		if (Llegada_Alumno(estado, estado.posicion)) {
			Estado temp1 = new Estado();
			//Se creal es estado y se le pasan los niños recogidos y entregados
			for (int i = 0; i < estado.recogidos.size(); i++) {
				temp1.recogidos.add(estado.recogidos.get(i));
			}
			for (int i = 0; i < estado.entregados.size(); i++) {
				temp1.entregados.add(estado.entregados.get(i));
			}
			//Se pasa la informacion del estado anterior 
			temp1.posicion = estado.posicion;
			temp1.g = estado.g;
			temp1.parent = estado;
			//Se hace la funcion heuristica
			temp1.h = H_Score(nodes, temp1, waitingList, heuristica, inicial);
			temp1.f = temp1.g + temp1.h;
			temp1.distancia = temp1.h;
			temp1.Capacidad = estado.Capacidad;
			//Se bajan todos los alumnos a la vez
			for (int j = 0; j < temp1.recogidos.size(); j++) {
				if (temp1.recogidos.get(j).escuela.equals(temp1.posicion.escuela)
						&& !(temp1.entregados.contains(temp1.recogidos.get(j)))) {
					temp1.entregados.add(temp1.recogidos.get(j));
					temp1.Capacidad = temp1.Capacidad + 1;
					temp1.bajan.add(temp1.recogidos.get(j));
				}
			}
			//Si la lista cerrada no contine el nuevo estado se añade a la lista abierta
			if ((Contain_Estado(temp1, closeList))) {
				openList.add(temp1);
			}

		}

		else {
//Si no hay niños por bajar se hacen los vertces adyacentes
			for (Edge e : estado.posicion.adjacencies) {
				// Node child creando la posicion de destino
				Estado temp1 = new Estado();
				temp1.posicion = e.to;
				int cost = e.distance;
				temp1.Capacidad = estado.Capacidad;

				for (int i = 0; i < estado.recogidos.size(); i++) {
					temp1.recogidos.add(estado.recogidos.get(i));
				}
				for (int i = 0; i < estado.entregados.size(); i++) {
					temp1.entregados.add(estado.entregados.get(i));
				}
//Se coge el coste de la adyacencia
				temp1.g = estado.g + cost;
				temp1.parent = estado;
				temp1.h = H_Score(nodes, temp1, waitingList, heuristica, inicial);
				temp1.distancia = temp1.h;
				temp1.f = temp1.g + temp1.h;

				//Si no lo contiene se mete el estado
				if ((Contain_Estado(temp1, closeList))) {
					openList.add(temp1);
				}

			}

			// operacione subir niño
			if ((estado.Capacidad > 0) && !Contain_Alumno(estado, estado.posicion)) {

				for (int j = 0; j < estado.posicion.alumnos.size(); j++) {
					if ((estado.Capacidad > 0) && !estado.recogidos.contains(estado.posicion.alumnos.get(j))) {
						Estado temp1 = new Estado();
						//Se crea el estado y se le pasa la informacion del estado padre
						for (int i = 0; i < estado.recogidos.size(); i++) {
							temp1.recogidos.add(estado.recogidos.get(i));
						}
						for (int i = 0; i < estado.entregados.size(); i++) {
							temp1.entregados.add(estado.entregados.get(i));
						}
						for (int i = 0; i < estado.suben.size(); i++) {
							temp1.suben.add(estado.suben.get(i));
						}
						//Se pasa las mismas cosas del padre que no cambian
						temp1.posicion = estado.posicion;
						temp1.g = estado.g;
						temp1.parent = estado;
						temp1.h = H_Score(nodes, temp1, waitingList, heuristica, inicial);
						temp1.f = temp1.g + temp1.h;
						temp1.distancia = temp1.h;
						temp1.recogidos.add(estado.posicion.alumnos.get(j));
						//Se crea estado por cada niño subido y se baja la capacidad del bus
						temp1.Capacidad = estado.Capacidad - 1;
						temp1.suben.add(temp1.posicion.alumnos.get(j));

						//Si no lo contienen se mete
						if ((Contain_Estado(temp1, closeList))) {
							openList.add(temp1);
						}

					}

				}
			}
		}
	}

	public static int H_Score(Node nodes[], Estado e, List<Alumno> waitinglist, int heuristica, Estado inicial) {
		int h_score = 0;
		if (heuristica == 1) {
//heuritica que busca el numero de paradas que quedan con niños recorriendo que la lista de recogisdos y de las paradas
			boolean h = false;
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].alumnos.size(); j++) {
					if (!e.recogidos.contains(nodes[i].alumnos.get(j))) {
						h = true;
					}
				}
				if (h) {
					h_score++;
				}
				h = false;
			}
		}

		//Dijktra
		if (heuristica == 3) {
			h_score = 0;
		}
		//Se ve el camino que falta hasta el inicio
		if (heuristica == 2) {
			int contador = 0;
			e.distancia = 0;
			if (inicial.posicion.equals(e.posicion)) {
				return 0;
			} else {
//Se expande si no estamos en la parada inicial
				contador = expandirn(e, inicial);
			}

			h_score = contador;
		}

		return h_score;
	}

	public static int expandirn(Estado e, Estado inicial) {
		//Se va expandiendo y comparando si llegamos al estado inicial
		Estado temp1 = null;
		boolean b = false;
		List<Estado> estados = new ArrayList<Estado>();
		estados.add(e);
		int cuenta = 0;
		while (!estados.isEmpty() && !b) {
			for (int i = 0; i < estados.size(); i++) {
				for (Edge edge : estados.get(i).posicion.adjacencies) {
					// Node child
					temp1 = new Estado();
					temp1.posicion = edge.to;
					temp1.distancia = estados.get(i).distancia + 1;
					//Si estamos en el inicial retirnanos el numero de pasos
					if (inicial.posicion.equals(temp1.posicion)) {
						cuenta = temp1.distancia;
						b = true;
						return cuenta;
					} else {
						estados.add(temp1);
					}
				}
				estados.remove(i);
				break;
			}

		}
		return cuenta;

	}
	
	public static int printNodosExpandidos(Estado target, List<Alumno> a, List<Escuela> escuelas) {
		List<Estado> path = new ArrayList<Estado>();
//Se va exopandiendo el numero de paradas pasadas por el mapa
		for (Estado estado = target; estado != null; estado = estado.parent) {
			path.add(estado);
		}
		Collections.reverse(path);

		int contador = 0;
		for (int i = 0; i < path.size(); i++) {
			if ((i < path.size() - 1)) {
				if (!(path.get(i).posicion.equals(path.get(i + 1).posicion))) {
					contador++;
				}
			}

		}
		return contador;
	}

	public static String printPath(Estado target, List<Alumno> a, List<Escuela> escuelas) {
		String ruta;
		//imprimimos el resultado de secuencia de paradas
		List<Estado> path = new ArrayList<Estado>();

		for (Estado estado = target; estado != null; estado = estado.parent) {
			path.add(estado);
		}
		Collections.reverse(path);

		ruta="\n";
		int valor=10000;
		//Se imprime los resultados del camino
		for (int i = 0; i < path.size(); i++) {
			List<String> sub = new ArrayList<String>();
			if(i<path.size()-1) {
				if(path.get(i).posicion.equals(path.get(i+1).posicion)) {
					if(path.get(i).bajan.isEmpty() & path.get(i).suben.isEmpty()) {
						continue;
					}
					if(!path.get(i).suben.isEmpty() & !path.get(i+1).suben.isEmpty()) {
						valor++;
						continue;
					}
					if(!path.get(i).suben.isEmpty() & !path.get(i+1).bajan.isEmpty()) {
						valor=i+1;
					}
					if(!path.get(i).bajan.isEmpty() & !path.get(i+1).suben.isEmpty()) {
						valor=i+1;
					}
				}
			}
			if(valor!=i) {
				ruta=ruta+path.get(i).posicion.toString() + " ";
			}
			//Los que suben
			if (!path.get(i).suben.isEmpty()) {
				ruta=ruta+"(S: ";
				for (int k = 0; k < escuelas.size(); k++) {
					int numero = 0;
					for (int j = 0; j < path.get(i).suben.size(); j++) {
						if (path.get(i).suben.get(j).escuela.equals(escuelas.get(k))) {
							numero++;
						}
					}
					if (numero != 0) {
						String string = numero + " " + escuelas.get(k).escuela;
						sub.add(string);
					}
				}
				for (int j = 0; j < sub.size(); j++) {
					ruta=ruta+sub.get(j);
					if (j < sub.size() - 1) {
						ruta=ruta+", ";
					}
				}
				ruta=ruta+") ";
			}

			if (!path.get(i).bajan.isEmpty()) {
				//LOs que bajan
				ruta=ruta+"(B: " + path.get(i).bajan.size() + " " + path.get(i).bajan.get(0).toString() + ") ";
			}
			if ((i != path.size() - 1) & (valor-1!=i)){
				ruta=ruta+"-> ";
				valor=1000;

			}
		}

		return ruta;
	}

	private static boolean isNumeric(String cadena) {
		try {
			//Comprueba que es un numero
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private static boolean Llegada_Alumno(Estado e, Node n) {
		//Comprueba que haya algun nuñopor repartir
		for (int j = 0; j < e.recogidos.size(); j++) {
			if (e.recogidos.get(j).escuela.equals(n.escuela) & !(e.entregados.contains(e.recogidos.get(j)))) {
				return true;
			}
		}
		return false;
	}

	private static boolean Contain_Alumno(Estado e, Node n) {
		//Comprueba si tenemos esos alumnos
		for (int j = 0; j < n.alumnos.size(); j++) {
			if (!e.recogidos.contains(n.alumnos.get(j))) {
				return false;
			}
		}
		return true;
	}

	//Compara que las listas sean iguales y que este en la misma posicion
	private static boolean Contain_Estado(Estado e, Set<Estado> closeList) {
		for (Estado stock : closeList) {
			if (e.posicion.equals(stock.posicion) && (Compare_Estado(e, stock))) {
				return false;
			}
		}
		return true;
	}

	//Compara que dos listas sean iguales a la vez la de recogidos y la de entregados
	private static boolean Compare_Estado(Estado e, Estado h) {
		for (int i = 0; i < h.recogidos.size(); i++) {
			if (!(e.recogidos.contains(h.recogidos.get(i)))) {
				return false;
			}
		}
		for (int j = 0; j < e.recogidos.size(); j++) {
			if (!(h.recogidos.contains(e.recogidos.get(j)))) {
				return false;
			}
		}

		for (int i = 0; i < h.entregados.size(); i++) {
			if (!(e.entregados.contains(h.entregados.get(i)))) {
				return false;
			}
		}

		for (int i = 0; i < e.entregados.size(); i++) {
			if (!(h.entregados.contains(e.entregados.get(i)))) {
				return false;
			}
		}
		return true;
	}
	

}
