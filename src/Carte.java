public class Carte {
    private String valeur; // L'image représentée sur la carte
    private boolean estVisible; // true si la carte est face visible, false sinon

    /**
     * Constructeur de la classe Carte.
     * @param valeur La valeur (image) de la carte.
     */
    public Carte(String valeur) {
        this.valeur = valeur;
        this.estVisible = false; // Par défaut, la carte est face cachée
    }

    /**
     * Retourne la valeur de la carte.
     * @return La valeur de la carte.
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * Vérifie si la carte est face visible.
     * @return true si la carte est face visible, false sinon.
     */
    public boolean estVisible() {
        return estVisible;
    }

    /**
     * Retourne la carte (change son état).
     */
    public void retourner() {
        estVisible = !estVisible;
    }
}