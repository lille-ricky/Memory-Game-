import java.awt.EventQueue;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create directories for resources if they don't exist
        createResourceDirectories();
        
        // Créer le modèle avec les valeurs des cartes
        List<String> valeurs = List.of("kat", "mf", "morgana", "nasus", "olaf", "rammus");
        ModeleMemory modele = new ModeleMemory(valeurs);
        
        // Créer l'interface graphique
        EventQueue.invokeLater(() -> new ViewMemory(modele));
    }
    
    /**
     * Creates resource directories for the game
     */
    private static void createResourceDirectories() {
        try {
            java.io.File imagesDir = new java.io.File("images");
            if (!imagesDir.exists()) {
                imagesDir.mkdir();
                System.out.println("Created 'images' directory. Please add card images here.");
            }
            
            java.io.File soundsDir = new java.io.File("sounds");
            if (!soundsDir.exists()) {
                soundsDir.mkdir();
                System.out.println("Created 'sounds' directory. Please add sound files here.");
                System.out.println("Required sound files: flip.wav, match.wav, nomatch.wav, victory.wav, newgame.wav");
            }
        } catch (Exception e) {
            System.out.println("Error creating resource directories: " + e.getMessage());
        }
    }
}