package vue;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import coeur.Grille;
import coeur.Piece;
import util_observeur.EcouteurModele;


/**
 * GrillePanel
 */
public class GrillePanel extends JPanel implements MouseListener, MouseMotionListener , EcouteurModele{
    private int Hauteur;
    private int Largeur;
    private Grille grille ;
    private GrilleFrame frameParent;
    private List<Piece> pieceAdjacentes =  new ArrayList<>();
    private int largeurPiece;
    private int hauteurPiece;
    private int caseSurvoleeX = -1;
    private int caseSurvoleeY = -1;


    public GrillePanel(GrilleFrame frameParent,Grille grille){
        this.grille = grille;
        this.Hauteur = this.grille.getHauteur();
        this.Largeur = this.grille.getLargeur();
      
        this.frameParent = frameParent;
        grille.ajoutEcouteur(this);
        pieceAdjacentes = grille.getPieceAdjacente();

        addMouseListener(this);
        addMouseMotionListener(this);
    }
    


    @Override
    public void modeleMisAJour(Object source){
      repaint();
      frameParent.updateCoup();
      pieceAdjacentes = grille.getPieceAdjacente();
    }

    @Override
    protected void paintComponent(Graphics g) { 
        super.paintComponent(g);
        largeurPiece = getWidth() / Largeur;
        hauteurPiece = getHeight() / Hauteur;
        for (int i = 0; i < Largeur; i++) {
            for (int j = 0; j < Hauteur; j++) {
                Piece p = grille.getTab()[j][i];
                if (p.getEstTrou()) {
                    g.setColor(Color.BLACK);
                    g.fillRect(i * largeurPiece, j * hauteurPiece, largeurPiece, hauteurPiece); // Dessiner un Rect blanc
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(i * largeurPiece, j * hauteurPiece, largeurPiece, hauteurPiece); // Dessiner un Rect blanc
                    g.setColor(Color.BLACK); // Changer la couleur du texte en noir
                    
                    // Calcul de la taille de police en pourcentage de la largeur de la fenêtre
                    int taillePolice = getWidth() / 20; // Par exemple, 5% de la largeur de la fenêtre
                    g.setFont(new Font("Arial", Font.PLAIN, taillePolice));

                    g.drawString(p.getId(), i * largeurPiece + largeurPiece / 2, j * hauteurPiece + hauteurPiece / 2); // Dessiner l'ID au centre 
                }
               
                g.drawRect(i * largeurPiece, j * hauteurPiece, largeurPiece, hauteurPiece); // Dessiner le contour 
            }
        }

        
        if (grille.jeuTerminee()) {
            // Si le jeu est terminé, afficher un message
            String message = "Félicitations ! Vous avez terminé le jeu !  ";
            g.setColor(Color.RED); // Couleur du message
            g.setFont(new Font("Arial", Font.BOLD, 20)); // Police du message
          
            // Calculer les coordonnées pour centrer le message
            int messageLargeur = g.getFontMetrics().stringWidth(message);
            int messageHauteur = g.getFontMetrics().getHeight();
           // g.fillRect(messageLargeur, messageLargeur, messageLargeur, messageLargeur);
            int messageX = (getWidth() - messageLargeur) / 2;
            int messageY = (getHeight() - messageHauteur) / 2;
            // Dessiner le message
            g.drawString(message, messageX, messageY);
            
        }
        

        
    }
 

    public void restart(){
        if(grille.jeuTerminee()){
            grille.getTab()[grille.getLargeur()-1][grille.getHauteur()-1].setEst_trou(true);
            }
            grille.melangerGrille();
            grille.ajoutEcouteur(this);
           grille.setNbCoup(0);
            addMouseListener(this);
            grille.update();
            repaint();
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int posXDeLaCaseCliquee = mouseY / hauteurPiece;
        int posYDeLaCaseCliquee = mouseX / largeurPiece;
    
        // Obtenez les coordonnées de la case vide
        int posXVide = grille.getPosVide().getX();
        int posYVide = grille.getPosVide().getY();
    
        // Calculer la direction en fonction des coordonnées de la case cliquée et de la case vide
        int dx = posXDeLaCaseCliquee - posXVide;
        int dy = posYDeLaCaseCliquee - posYVide;
        int direction;
    
        if (dx == -1 && dy == 0 && posXVide > 0) {
            direction = 0; // Haut
        } else if (dx == 0 && dy == 1 && posYVide < Largeur - 1) {
            direction = 1; // Droite
        } else if (dx == 1 && dy == 0 && posXVide < Hauteur - 1) {
            direction = 2; // Bas
        } else if (dx == 0 && dy == -1 && posYVide > 0) {
            direction = 3; // Gauche
        } else {
            return; 
        }
    
        if(!grille.jeuTerminee()){
            grille.deplacerCase(direction, false); 
        }
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(!grille.jeuTerminee()){
            int taillePolice = getWidth() / 20;
            int mouseX = e.getX();
            int mouseY = e.getY();
            int posXDeLaCaseSurvolee = mouseY / hauteurPiece;
            int posYDeLaCaseSurvolee = mouseX / largeurPiece;
        
            // Obtenir les coordonnées de la case vide
            int posXVide = grille.getPosVide().getX();
            int posYVide = grille.getPosVide().getY();
            
           // Effacer la coloration de la case adjacente précédente en la repeignant en gris et en redessinant le contour
            if (caseSurvoleeX != -1 && caseSurvoleeY != -1) {
                Graphics g = getGraphics();
    
                // Dessiner la case en gris et redessiner le contour
                g.setColor(Color.GRAY);
                g.fillRect(caseSurvoleeY * largeurPiece, caseSurvoleeX * hauteurPiece, largeurPiece, hauteurPiece);
                g.setColor(Color.BLACK);
                g.drawRect(caseSurvoleeY * largeurPiece, caseSurvoleeX * hauteurPiece, largeurPiece, hauteurPiece);
            
                // Dessiner l'ID de la pièce au centre de la case non colorée
                Piece p = grille.getTab()[caseSurvoleeX][caseSurvoleeY];
                g.setFont(new Font("Arial", Font.PLAIN, taillePolice));
                g.drawString(p.getId(), caseSurvoleeY * largeurPiece + largeurPiece / 2 - taillePolice / 4, caseSurvoleeX * hauteurPiece + hauteurPiece / 2 + taillePolice / 4);
            
                if (p.getEstTrou()) {
                    g.setColor(Color.BLACK);
                    g.fillRect(caseSurvoleeY * largeurPiece, caseSurvoleeX * hauteurPiece, largeurPiece, hauteurPiece);
                }
            }
    
        
            // Vérifier si la case survolée est adjacente à la case vide
            if ((posXDeLaCaseSurvolee == posXVide - 1 && posYDeLaCaseSurvolee == posYVide) ||
                (posXDeLaCaseSurvolee == posXVide + 1 && posYDeLaCaseSurvolee == posYVide) ||
                (posYDeLaCaseSurvolee == posYVide - 1 && posXDeLaCaseSurvolee == posXVide) ||
                (posYDeLaCaseSurvolee == posYVide + 1 && posXDeLaCaseSurvolee == posXVide)) {
        
                // Dessiner la case survolée avec la couleur spécifique
                Graphics g = getGraphics();
                g.setColor(Color.GREEN);
                g.fillRect(posYDeLaCaseSurvolee * largeurPiece, posXDeLaCaseSurvolee * hauteurPiece, largeurPiece, hauteurPiece);
                
                // Dessiner l'ID de la pièce au centre de la case colorée
                Piece p = grille.getTab()[posXDeLaCaseSurvolee][posYDeLaCaseSurvolee];
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, taillePolice));
                g.drawString(p.getId(), posYDeLaCaseSurvolee * largeurPiece + largeurPiece / 2 - taillePolice / 4, posXDeLaCaseSurvolee * hauteurPiece + hauteurPiece / 2 + taillePolice / 4);
                
                // Mettre à jour les coordonnées de la case adjacente précédente
                caseSurvoleeX = posXDeLaCaseSurvolee;
                caseSurvoleeY = posYDeLaCaseSurvolee;
            } else {
                // Réinitialiser les coordonnées de la case adjacente précédente
                caseSurvoleeX = -1;
                caseSurvoleeY = -1;
            }
        }
        
    }
    
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(java.awt.event.MouseEvent e){}

    

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}

   
    //clavier
    public void pressed(int key) {
        
        int direction;

        // Déplacement du trou en fonction de la touche enfoncée
        if(key == 87){
            direction = 0; // Haut
        }else if(key == 68){
            direction = 1; // Droite
        }else if(key == 83){
            direction = 2; // Bas
        }else if(key == 65){
            direction = 3; // Gauche
        }else{
            direction = -1;
        }


        if(!grille.jeuTerminee()&& direction != -1){
            grille.deplacerCase(direction, false); 
        }
      
    }
    
    public Grille getGrille(){return grille;}



    @Override
    public void mouseDragged(MouseEvent e){}

}