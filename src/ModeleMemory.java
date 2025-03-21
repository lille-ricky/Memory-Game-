import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModeleMemory {
    private List<Carte> cartes;
    private int pairesRestantes;
    private int nombreEssais;
    private List<String> valeurs;

    /**
     * Constructeur de la classe ModeleMemory.
     * @param valeurs La liste des valeurs (images) pour les cartes. Chaque valeur doit apparaître deux fois.
     */
    public ModeleMemory(List<String> valeurs) {
        this.valeurs = valeurs;
        initialiserJeu();
    }

    /**
     * Initialise ou réinitialise le jeu.
     */
    public void initialiserJeu() {
        this.cartes = new ArrayList<>();
        
        for (String valeur : valeurs) {
            cartes.add(new Carte(valeur));
            cartes.add(new Carte(valeur));
        }
        
        this.pairesRestantes = valeurs.size();
        this.nombreEssais = 0;
        
        melangerCartes();
    }

    /**
     * Mélange les cartes du jeu.
     */
    private void melangerCartes() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < cartes.size(); i++) {
            indices.add(i);
        }
        
        Collections.shuffle(indices);
        
        List<Carte> cartesMelangees = new ArrayList<>();
        for (int index : indices) {
            cartesMelangees.add(cartes.get(index));
        }
        
        cartes = cartesMelangees;
    }

    /**
     * Retourne une carte à l'index spécifié.
     * @param index L'index de la carte à retourner.
     */
    public void retournerCarte(int index) {
        if (index >= 0 && index < cartes.size()) {
            cartes.get(index).retourner();
        }
    }

    /**
     * Vérifie si deux cartes forment une paire.
     * @param index1 L'index de la première carte.
     * @param index2 L'index de la deuxième carte.
     * @return true si les cartes forment une paire, false sinon.
     */
    public boolean verifierPaire(int index1, int index2) {
        if (index1 >= 0 && index1 < cartes.size() && index2 >= 0 && index2 < cartes.size()) {
            Carte carte1 = cartes.get(index1);
            Carte carte2 = cartes.get(index2);
            if (carte1.getValeur().equals(carte2.getValeur())) {
                pairesRestantes--;
                return true;
            }
        }
        nombreEssais++;
        return false;
    }

    /**
     * Vérifie si le jeu est terminé.
     * @return true si toutes les paires ont été trouvées, false sinon.
     */
    public boolean estTermine() {
        return pairesRestantes == 0;
    }

    /**
     * Retourne le nombre d'essais effectués.
     * @return Le nombre d'essais.
     */
    public int getNombreEssais() {
        return nombreEssais;
    }

    /**
     * Retourne le nombre de paires restantes à trouver.
     * @return Le nombre de paires restantes.
     */
    public int getPairesRestantes() {
        return pairesRestantes;
    }

    /**
     * Retourne la liste des cartes.
     * @return La liste des cartes.
     */
    public List<Carte> getCartes() {
        return cartes;
    }
}