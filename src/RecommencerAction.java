import javax.swing.*;
import java.awt.event.ActionEvent;

public class RecommencerAction extends AbstractAction {
    private ViewMemory viewMemory;

    public RecommencerAction(String name, Icon icon, ViewMemory viewMemory) {
        super(name, icon);
        this.viewMemory = viewMemory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        viewMemory.recommencerJeu(); // Réinitialiser la partie
    }
}