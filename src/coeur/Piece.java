package coeur;

import util_observeur.*;


// Définition de la classe Piece, héritant de AbstractModeleEcoutable
public class Piece extends AbstractModeleEcoutable {
    private String id; // Identifiant de la pièce
    private Position posInit; // Position en y de la pièce
    private Position posFinal; // Position finale en x de la pièce
    private boolean estTrou = false ; 

    // Constructeur de la classe Piece
    public Piece(String id, Position posInit ,Position posFinal) {
        this.id = id; // Initialise l'identifiant de la pièce
        this.posInit = posInit; // Initialise la position de la pièce
        this.posFinal = posFinal; // Initialise la position finale de la pièce
    }

    // Méthode pour récupérer l'identifiant de la pièce
    public String getId() {
        return id;
    }

    // Méthode pour récupérer la position en x de la pièce
    public Position getPos() {
        return this.posInit;
    }

    // Méthode pour récupérer la position finale de la pièce
    public Position getPosFinal() {
        return this.posFinal;
    }
    
    
    public boolean getEstTrou(){
        return estTrou ;
    }
    

    // Méthode pour définir la position de la pièce
    public void setPos(Position pos) {
        this.posInit = pos;
    }

    public void setPos(int x, int y){
        this.posInit.setX(x);
        this.posInit.setY(y);
    }

    // Méthode pour définir la position en y de la pièce
    public void setPosFinal(Position posFinal) {
        this.posFinal = posFinal;
    }

    public void setPosFinal(int x, int y){
        this.posFinal.setX(x);
        this.posFinal.setY(y);
    }
   
    public void setEst_trou(boolean est_trou) {
        this.estTrou = est_trou;
    }

    @Override
    public String toString() {
        return "Piece [id=" + id + ", posX=" + posInit.getX()  + ", posY=" + posInit.getY() + ", posFinalX=" + posFinal.getX()  +  ", posFinalY=" + posFinal.getY() +", est_trou=" + estTrou + "]";
}

}