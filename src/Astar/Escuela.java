package Astar;

public class Escuela {
		String escuela;
		Node placement;
			//La escuela y su parada
		public Escuela(String escuela,Node placement){
			this.escuela = escuela;
			this.placement=placement;
		}
		
		public String toString(){
			return escuela; 
		}

}
