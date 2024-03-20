import javax.swing.*;
import java.awt.*;

public class FloatingPanel {
    private JFrame frame = new JFrame();
    private JLayeredPane layeredPane = new JLayeredPane();
    private JPanel mainPanel = new JPanel();
    private JPanel floatingPanel = new JPanel();

    public FloatingPanel() {
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        
        // Set up main panel
        mainPanel.setLayout(new GridLayout(3, 3)); // Set grid layout for main panel
        
        // Add buttons to the main panel with blue background
        for (int i = 0; i < 9; i++) {
            JButton button = new JButton("Button " + (i + 1));
            button.setBackground(Color.BLUE); // Set background color to blue
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border
            mainPanel.add(button);
        }

        // Set up floating panel
        floatingPanel.setBackground(new Color(0, 0, 0, 128)); // Semi-transparent black background
        floatingPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("GAME OVER");
        label.setForeground(Color.WHITE); // White text color
        label.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font
        floatingPanel.add(label);

        // Set bounds for main panel and floating panel
        mainPanel.setBounds(0, 0, 400, 400);
        floatingPanel.setBounds(100, 100, 200, 100);

        // Add components to layered pane with appropriate layers
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(floatingPanel, JLayeredPane.PALETTE_LAYER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new FloatingPanel();
    }
}
