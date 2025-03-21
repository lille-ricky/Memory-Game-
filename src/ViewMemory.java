import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class ViewMemory {
    private ModeleMemory modele;
    private JFrame frame;
    private JButton[] boutonsCartes;
    private int premiereCarteIndex = -1;
    private int deuxiemeCarteIndex = -1;
    private Timer timer;
    private JLabel essaisLabel;
    private JLabel imagesLabel;
    private JLabel timerLabel;
    private boolean enAttente = false;
    private long startTime;
    private Timer gameTimer;
    
    /**
     * Constructeur de la classe ViewMemory.
     * 
     * @param modele Le modèle du jeu.
     */
    public ViewMemory(ModeleMemory modele) {
        this.modele = modele;
        initialiserInterface();
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initialiserInterface() {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        frame = new JFrame("Jeu de Memory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a custom background panel
        Image bgImage = new ImageIcon("images/background.jpg").getImage();
        BackgroundPanel mainPanel = new BackgroundPanel(bgImage);
        frame.setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(35, 35, 45));
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenu jeuMenu = new JMenu("Jeu");
        jeuMenu.setForeground(Color.WHITE);
        JMenuItem nouvellePartieItem = new JMenuItem(new RecommencerAction("Nouvelle partie", null, this));
        nouvellePartieItem.setBackground(new Color(35, 35, 45));
        nouvellePartieItem.setForeground(Color.WHITE);
        
        JMenuItem quitterItem = new JMenuItem(new QuitterAction("Quitter", null));
        quitterItem.setBackground(new Color(35, 35, 45));
        quitterItem.setForeground(Color.WHITE);
        
        jeuMenu.add(nouvellePartieItem);
        jeuMenu.add(quitterItem);
        menuBar.add(jeuMenu);

        JMenu aideMenu = new JMenu("Aide");
        aideMenu.setForeground(Color.WHITE);
        JMenuItem aProposItem = new JMenuItem(new AProposAction("À propos...", null, frame));
        aProposItem.setBackground(new Color(35, 35, 45));
        aProposItem.setForeground(Color.WHITE);
        
        aideMenu.add(aProposItem);
        menuBar.add(aideMenu);

        frame.setJMenuBar(menuBar);

        // Add header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(35, 35, 45));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("League of Legends Memory");
        titleLabel.setForeground(new Color(236, 190, 55)); // Gold color
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create card panel with spacing
        JPanel cardPanel = new JPanel(new GridLayout(2, modele.getCartes().size() / 2, 10, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cardPanel.setOpaque(false);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Create card buttons
        boutonsCartes = new JButton[modele.getCartes().size()];
        for (int i = 0; i < boutonsCartes.length; i++) {
            boutonsCartes[i] = createCardButton(i);
            cardPanel.add(boutonsCartes[i]);
        }

        // Create info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(35, 35, 45));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setOpaque(false);

        // Create styled labels
        essaisLabel = new JLabel("Essais: 0");
        essaisLabel.setForeground(Color.WHITE);
        essaisLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        imagesLabel = new JLabel("Images gagnées: 0");
        imagesLabel.setForeground(Color.WHITE);
        imagesLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        statsPanel.add(essaisLabel);
        statsPanel.add(imagesLabel);
        infoPanel.add(statsPanel, BorderLayout.WEST);

        // Add a timer display
        timerLabel = new JLabel("Temps: 00:00");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        infoPanel.add(timerLabel, BorderLayout.EAST);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Set up card flip timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (premiereCarteIndex != -1 && deuxiemeCarteIndex != -1) {
                    // Flip cards back
                    animateCardFlip(premiereCarteIndex, false);
                    animateCardFlip(deuxiemeCarteIndex, false);
                    
                    modele.retournerCarte(premiereCarteIndex);
                    modele.retournerCarte(deuxiemeCarteIndex);
                    
                    premiereCarteIndex = -1;
                    deuxiemeCarteIndex = -1;
                    enAttente = false;
                }
            }
        });
        timer.setRepeats(false);

        // Set up game timer
        startTime = System.currentTimeMillis();
        gameTimer = new Timer(1000, e -> updateGameTimer());
        gameTimer.start();

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creates a stylized card button
     */
    private JButton createCardButton(int index) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon("images/twistedFate.png"));
        button.addActionListener(new CarteClickListener(index));
        
        // Modern styling
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(42, 42, 68));
        
        // Add rounded corners and border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(32, 32, 50), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!button.getActionCommand().equals("desactivee")) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(236, 190, 55), 2), // Gold highlight
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent evt) {
                if (!button.getActionCommand().equals("desactivee")) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(32, 32, 50), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                }
            }
        });
        
        return button;
    }

    /**
     * Updates the game timer display
     */
    private void updateGameTimer() {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        int minutes = (int) (elapsedSeconds / 60);
        int seconds = (int) (elapsedSeconds % 60);
        timerLabel.setText(String.format("Temps: %02d:%02d", minutes, seconds));
    }

    /**
     * Animates a card flip
     */
    private void animateCardFlip(int index, boolean revealing) {
        final JButton button = boutonsCartes[index];
        final Timer animTimer = new Timer(20, null);
        final int totalFrames = 10;
        final int[] currentFrame = {0};
        
        // Play flip sound
        if (revealing) {
            playSound("flip");
        }
        
        animTimer.addActionListener(e -> {
            currentFrame[0]++;
            
            if (currentFrame[0] <= totalFrames / 2) {
                // First half - shrink horizontally
                float scale = 1.0f - (currentFrame[0] / (float)(totalFrames / 2));
                button.setIcon(new ImageIcon(scaleImage("images/twistedFate.png", scale, 1.0f)));
            } else if (currentFrame[0] <= totalFrames) {
                // Second half - expand horizontally with new image
                float scale = (currentFrame[0] - (totalFrames / 2)) / (float)(totalFrames / 2);
                String imagePath = revealing ? 
                    "images/" + modele.getCartes().get(index).getValeur() + ".png" : 
                    "images/twistedFate.png";
                button.setIcon(new ImageIcon(scaleImage(imagePath, scale, 1.0f)));
            } else {
                // Animation complete
                animTimer.stop();
                if (!revealing) {
                    button.setIcon(new ImageIcon("images/twistedFate.png"));
                } else {
                    button.setIcon(new ImageIcon("images/" + modele.getCartes().get(index).getValeur() + ".png"));
                }
            }
        });
        
        animTimer.start();
    }

    /**
     * Helper method to scale an image
     */
    private Image scaleImage(String path, float xScale, float yScale) {
        ImageIcon icon = new ImageIcon(path);
        int width = Math.max(1, (int)(icon.getIconWidth() * xScale));
        int height = Math.max(1, (int)(icon.getIconHeight() * yScale));
        return icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * Met à jour l'affichage des cartes.
     */
    private void mettreAJourAffichage() {
        List<Carte> cartes = modele.getCartes();
        essaisLabel.setText("Essais : " + modele.getNombreEssais());
        imagesLabel.setText("Images gagnées : " + (modele.getCartes().size() / 2 - modele.getPairesRestantes()));

        if (modele.estTermine()) {
            gameTimer.stop();
            playSound("victory");
            showVictoryScreen();
        }
    }

    /**
     * Shows a custom victory screen
     */
    private void showVictoryScreen() {
        JDialog victoryDialog = new JDialog(frame, "Victory!", true);
        victoryDialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 35, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel congrats = new JLabel("Félicitations!", JLabel.CENTER);
        congrats.setFont(new Font("SansSerif", Font.BOLD, 28));
        congrats.setForeground(new Color(236, 190, 55)); // Gold
        
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        int minutes = (int) (elapsedSeconds / 60);
        int seconds = (int) (elapsedSeconds % 60);
        
        JLabel stats = new JLabel(String.format("Vous avez gagné en %d essais et %02d:%02d!", 
                                  modele.getNombreEssais(), minutes, seconds), JLabel.CENTER);
        stats.setFont(new Font("SansSerif", Font.PLAIN, 18));
        stats.setForeground(Color.WHITE);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);
        textPanel.add(congrats);
        textPanel.add(stats);
        panel.add(textPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        JButton newGameBtn = new JButton("Nouvelle partie");
        JButton closeBtn = new JButton("Fermer");
        
        // Style buttons
        for (JButton btn : new JButton[]{newGameBtn, closeBtn}) {
            btn.setBackground(new Color(45, 45, 55));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        }
        
        newGameBtn.addActionListener(e -> {
            recommencerJeu();
            victoryDialog.dispose();
        });
        
        closeBtn.addActionListener(e -> victoryDialog.dispose());
        
        buttonPanel.add(newGameBtn);
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        victoryDialog.add(panel);
        victoryDialog.pack();
        victoryDialog.setLocationRelativeTo(frame);
        victoryDialog.setVisible(true);
    }

    /**
     * Plays a sound effect
     */
    private void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                new File("sounds/" + soundName + ".wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound file not found: " + soundName);
        }
    }

    /**
     * Recommence une nouvelle partie.
     */
    public void recommencerJeu() {
        modele.initialiserJeu();
        for (JButton bouton : boutonsCartes) {
            bouton.setActionCommand("");
            bouton.setIcon(new ImageIcon("images/twistedFate.png"));
            
            // Reset the border style
            bouton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 32, 50), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }
        
        enAttente = false;
        premiereCarteIndex = -1;
        deuxiemeCarteIndex = -1;
        
        // Reset and restart timer
        startTime = System.currentTimeMillis();
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
        
        mettreAJourAffichage();
        playSound("newgame");
    }

    /**
     * Custom background panel class
     */
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel(Image image) {
            this.backgroundImage = image;
            setLayout(new BorderLayout());
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(29, 29, 38));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    /**
     * Classe interne pour gérer les clics sur les cartes.
     */
    private class CarteClickListener implements ActionListener {
        private int index;

        public CarteClickListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if ("desactivee".equals(((JButton) e.getSource()).getActionCommand())) {
                return;
            }

            if (modele.estTermine() || enAttente) {
                return;
            }

            if (premiereCarteIndex == -1) {
                premiereCarteIndex = index;
                modele.retournerCarte(index);
                animateCardFlip(index, true);
            } else if (deuxiemeCarteIndex == -1 && index != premiereCarteIndex) {
                deuxiemeCarteIndex = index;
                modele.retournerCarte(index);
                animateCardFlip(index, true);

                if (modele.verifierPaire(premiereCarteIndex, deuxiemeCarteIndex)) {
                    // Play match sound
                    playSound("match");
                    
                    boutonsCartes[premiereCarteIndex].setActionCommand("desactivee");
                    boutonsCartes[deuxiemeCarteIndex].setActionCommand("desactivee");
                    
                    // Dim matched cards
                    boutonsCartes[premiereCarteIndex].setBackground(new Color(30, 30, 45));
                    boutonsCartes[deuxiemeCarteIndex].setBackground(new Color(30, 30, 45));
                    
                    boutonsCartes[premiereCarteIndex].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(50, 205, 50), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    
                    boutonsCartes[deuxiemeCarteIndex].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(50, 205, 50), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));

                    premiereCarteIndex = -1;
                    deuxiemeCarteIndex = -1;
                } else {
                    // Play no match sound
                    playSound("nomatch");
                    enAttente = true;
                    timer.start();
                }
            }

            mettreAJourAffichage();
        }
    }
}