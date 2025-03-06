package vue;

import javax.swing.*;

import coeur.Grille;

import java.awt.*;
import java.awt.event.*;


public class GrilleFrame extends JFrame implements KeyListener, ActionListener {
   private GrillePanel centerPanel;
   private JLabel nombreCoupLabel;
   private Grille grille;

    public GrilleFrame(Menue m,Grille grille){
        this.grille = grille;
  
        setTitle("Jeu de Puzzle à glissières (Taquin)");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(128, 0, 128)); // Fond violet
        setLayout(new BorderLayout());
        this.addKeyListener(this);
        setFocusable(true);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                m.activeBoutton(true);
            }
        });

        // Panneau supérieur avec le bouton "Restart"
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        topPanel.add(restartButton);

        // Panneau central avec une grille 3x3 de cercles
        centerPanel = new GrillePanel(this,this.grille);
       
        // Étiquette pour afficher le nombre de coups
        nombreCoupLabel = new JLabel("Nombre de Coups: "+centerPanel.getGrille().getNbCoup());
        nombreCoupLabel.setForeground(Color.WHITE);

        // Ajout des composants à la fenêtre principale
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(nombreCoupLabel, BorderLayout.SOUTH);

        setVisible(true); // Rendre la fenêtre visible
    }


    public void updateCoup(){
        nombreCoupLabel.setText("Nombre de Coups: "+centerPanel.getGrille().getNbCoup());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        centerPanel.pressed(e.getKeyCode());
      
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        centerPanel.restart();
        updateCoup();
        requestFocus();
       
    }
 

    
}
