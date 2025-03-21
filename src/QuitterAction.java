import javax.swing.*;
import java.awt.event.ActionEvent;

public class QuitterAction extends AbstractAction {
    public QuitterAction(String name, Icon icon) {
        super(name, icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0); // Quitter le programme
    }
}