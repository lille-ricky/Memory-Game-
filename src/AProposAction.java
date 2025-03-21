import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AProposAction extends AbstractAction {
    private JFrame parent;
    
    public AProposAction(String name, Icon icon, JFrame parent) {
        super(name, icon);
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Create custom about dialog
        JDialog aboutDialog = new JDialog(parent, "À propos", true);
        aboutDialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 35, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Game title
        JLabel titleLabel = new JLabel("Jeu de Memory", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 190, 55)); // Gold
        
        // Author info
        JLabel authorLabel = new JLabel("Auteur : Enrik Pashaj", JLabel.CENTER);
        authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        authorLabel.setForeground(Color.WHITE);
        
        // Version info
        JLabel versionLabel = new JLabel("Version 1.0", JLabel.CENTER);
        versionLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        versionLabel.setForeground(Color.WHITE);
        
        // Add components to panel
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(titleLabel);
        contentPanel.add(authorLabel);
        contentPanel.add(versionLabel);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Add OK button
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(45, 45, 55));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        okButton.addActionListener(evt -> aboutDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.add(panel);
        aboutDialog.pack();
        aboutDialog.setLocationRelativeTo(parent);
        aboutDialog.setVisible(true);
    }
}