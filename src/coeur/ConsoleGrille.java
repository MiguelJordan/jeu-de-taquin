package coeur;

import util_observeur.*;

import java.util.Scanner;

public class ConsoleGrille implements EcouteurModele {
    
    private Grille grille;
    
    public ConsoleGrille(Grille grille) {
        this.grille = grille;
        this.grille.ajoutEcouteur(this);
    }
    
    public void nouveauJeu() {

        afficherGrille();
        
        Scanner scanner = new Scanner(System.in);
        
        while (!grille.jeuTerminee()) {
            System.out.println("0) haut\n1) droite\n2) bas\n3) gauche\n");
            System.out.print("Entrez le numéro du déplacement : ");
            int choix = scanner.nextInt();
            grille.deplacerCase(choix,false);
            System.out.println();
            //afficherGrille();
            
        }
        
        System.out.println("Félicitations, vous avez terminé le jeu !");
        scanner.close();
    }
    
    private void afficherGrille() {
        for (int x = 0; x < grille.getLargeur(); x++) {
            for (int y = 0; y < grille.getHauteur(); y++) {
                Piece piece = grille.getPiece(x, y);
                if (piece.getEstTrou()) {
                    System.out.print("    |"); // Case vide
                } else {
                    System.out.printf(" %2s |", piece.getId()); // Numéro de la pièce
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    @Override
    public void modeleMisAJour(Object source) {
            afficherGrille();
        
    }
}
