package coeur;

import util_observeur.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Grille extends AbstractModeleEcoutable  {
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private Piece[][] tab; // Tableau représentant la grille
    private int largeur; // Largeur de la grille
    private int hauteur; // Hauteur de la grille
    private Position posVide; // Position en x et y de la case vide
    private String choix = "nombres"; // choix entre les lettres et les chiffres
    private List<Piece> pieceAdjacentes = new ArrayList<>();
    private int nbCoups = 0;
    
    // Constructeur de la grille avec la largeur et la hauteur
    public Grille(int largeur, int hauteur, String choix) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.choix = choix;
        posVide = new Position(0, 0);
        creerGrille();
        melangerGrille();
    }

    public Piece[][] getTab() {
        return tab;
    }
    public int getLargeur() {
        return largeur;
    }
    public int getHauteur() {
        return hauteur;
    }
    public Position getPosVide() {
        return posVide;
    }

    public void setTab(Piece[][] tab) {
        this.tab = tab;
    }
    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }
    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }
    public void setPosVide(Position posVide) {
        this.posVide = posVide;
    }

    
    // Méthode pour créer la grille
    public void creerGrille() {
        tab = new Piece[largeur][hauteur]; // Initialise le tableau représentant la grille
        int id = 0; // Calcule l'identifiant de la pièce

        // Affectation d'un chiffre à chaque case de la grille
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                // Crée une nouvelle pièce normale avec les positions initiale et finale
                Position posInit = new Position(x, y);
                Position posFinal = new Position(x, y);
                if(choix == "nombres"){
                    tab[x][y] = new Piece(Integer.toString(++id), posInit, posFinal);
                }else if(choix == "lettres"){
                    getTab()[x][y] = new Piece(Character.toString(ALPHABET[id]), posInit, posFinal);
                    id = (id + 1) % ALPHABET.length; // Passage à la lettre suivante dans l'alphabet
                }
               
               
            }
        }

    // Affecter la position finale pour la case vide
    posVide = new Position(largeur - 1, hauteur - 1); // Position en x et y de la case vide (trou)
    tab[posVide.getX()][posVide.getY()].setEst_trou(true);
}

    
        

    // Méthode pour mélanger aléatoirement les pièces dans la grille
    public void melangerGrille() {
        Random rand = new Random();
        for (int i = 0; i < largeur * hauteur * 100; i++) {
            int dir = rand.nextInt(4); // 0: haut, 1: droite, 2: bas, 3: gauche
            deplacerCase(dir,true);
        }
        casesAdjacentes(posVide.getX(), posVide.getY());
    }
    
    // Méthode pour déplacer la case vide dans une direction donnée
    public void deplacerCase(int direction,boolean estInit) {
        int dx = 0, dy = 0; // Variables pour stocker le décalage horizontal (dx) et vertical (dy)
    
        // Détermination du décalage en fonction de la direction spécifiée
        if (direction == 0 && posVide.getX() > 0) { // Si la direction est "haut" et la case vide n'est pas sur la première ligne
            dx = -1; // Décalage vertical vers le haut
        } else if (direction == 1 && posVide.getY() < largeur - 1) { // Si la direction est "droite" et la case vide n'est pas sur la dernière colonne
            dy = 1; // Décalage horizontal vers la droite
        } else if (direction == 2 && posVide.getX() < hauteur - 1) { // Si la direction est "bas" et la case vide n'est pas sur la dernière ligne
            dx = 1; // Décalage vertical vers le bas
        } else if (direction == 3 && posVide.getY() > 0) { // Si la direction est "gauche" et la case vide n'est pas sur la première colonne
            dy = -1; // Décalage horizontal vers la gauche
        }else{return;}

    
        // Stocke la pièce adjacente à la case vide
        Piece pieceAdjacente = tab[posVide.getX() + dx][posVide.getY() + dy];
        pieceAdjacente.setPos(posVide.getX(), posVide.getY());



        // Échange les positions de la case vide et de la pièce adjacente
        tab[posVide.getX() + dx][posVide.getY() + dy] = tab[posVide.getX()][posVide.getY()];

        tab[posVide.getX()][posVide.getY()] = pieceAdjacente;


        tab[posVide.getX() + dx][posVide.getY() + dy].setPos(posVide.getX() +dx,posVide.getY() +dy);

        // Met à jour les positions de la case vide après le déplacement
        posVide.ajouter(dx, dy);

        if(!estInit){
            nbCoups++;
            fireChangement();
           
            casesAdjacentes(posVide.getX(), posVide.getY());
            if(jeuTerminee()){
                tab[posVide.getX()][posVide.getY()].setEst_trou(false);
            }
        }

    }

     // Méthode pour déplacer la piece adjacente avec la piece trou
    public void deplacerPiece(Piece p){
        Piece pieceVide = tab[posVide.getX()][posVide.getY()];
        tab[posVide.getX()][posVide.getY()] = p;
        Position posAdjacente = p.getPos();
        p.setPos(posVide.getX(), posVide.getY());
        tab[posAdjacente.getX()][posAdjacente.getY()] = pieceVide;
        pieceVide.setPos(posAdjacente);
        posVide = posAdjacente;
        
        
    }
        // Méthode pour obtenir les positions des cases adjacentes à la case vide
        private void casesAdjacentes(int posXVide, int posYVide) {
            pieceAdjacentes.clear();
    
            // Vérifie les cases adjacentes dans les quatre directions : haut, droite, bas, gauche
            int[][] directions = { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };
            for (int[] dir : directions) {
                int x = posXVide + dir[0];
                int y = posYVide + dir[1];
    
                // Vérifie si la case adjacente est valide (dans les limites de la grille)
                if (x >= 0 && x < largeur && y >= 0 && y < hauteur) {
                    pieceAdjacentes.add(tab[x][y]);
                }
            }
    
         
        }


        public List<Piece> getPieceAdjacente(){
            return pieceAdjacentes;
        }

        public List<int[]> casesAdjacentes1(int posXVide, int posYVide) {
            List<int[]> casesAdjacentes = new ArrayList<>();
    
           int[] haut = {posXVide , posYVide -1 };
           if (tab[haut[0]][haut[1]] != null) {
            casesAdjacentes.add(haut);
           }
           int[] bas = {posXVide , posYVide +1 };
           if (tab[bas[0]][bas[1]]!= null) {
            casesAdjacentes.add(bas);
           }
           int[] droite = {posXVide + 1 , posYVide  };
           if (tab[droite[0]][droite[1]]!= null) {
            casesAdjacentes.add(droite);
           }
           int[] gauche = {posXVide -1 , posYVide  };
           if (tab[gauche[0]][gauche[1]]!= null) {
            casesAdjacentes.add(gauche);
           }
            
    
            return casesAdjacentes;
        }
    
    
    // Méthode pour récupérer la pièce à une position donnée dans la grille
    public Piece getPiece(int x, int y) {
        if (x < 0 || x >= largeur || y < 0 || y >= hauteur) {
            return null;
        }
        return tab[x][y];
    }

    //Méthode qui indique si le jeu est terminée
    public boolean jeuTerminee() {
    // Parcours de toutes les pièces de la grille
    for (int x = 0; x < largeur; x++) {
        for (int y = 0; y < hauteur; y++) {
            Piece piece = tab[x][y];
            if (!piece.getPos().equals(piece.getPosFinal())) {
                return false; // Si la pièce n'est pas à la bonne position, le jeu n'est pas terminé
            }
            
        }
    }
    return true; // Si toutes les pièces sont à leur position finale, le jeu est terminé
}


    // Autres méthodes
    public int getNbCoup(){return this.nbCoups;}
    public void setNbCoup(int n){
        this.nbCoups= n;
    }

    public void update(){
        fireChangement();
    }
}
