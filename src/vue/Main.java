package vue;

import coeur.Grille;

public class Main {
    
    public static void main(String[] args) {
        Grille grille = new Grille(2, 2, "nombres");
        //new Menue();
        new Menue(grille);
        new Menue(grille);
    }
}
