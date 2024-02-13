import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons;
    private JLabel resultLabel;
    private Color highlightColor = new Color(255, 215, 0); // Golden yellow color

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setSize(500, 600); // Increased window size to accommodate the text area
        setResizable(false); // Fixing window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 3)); // Changed layout to accommodate the text area

        buttons = new JButton[3][3];
        initializeButtons();
        initializeResultLabel();
    }

    private void initializeButtons() {
        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 80); // Increased button font size
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(buttonFont); // Set font for each button
                buttons[i][j].setFocusPainted(false); // Disable default focus painting
                buttons[i][j].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2), // Outer black border
                        BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Empty border to separate buttons
                buttons[i][j].setBackground(Color.WHITE); // Set default background color
                buttons[i][j].setOpaque(true); // Make background color visible
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Button clicked at row " + row + ", column " + col);
                    }
                });
                buttons[i][j].addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        ((JButton) evt.getSource()).setBackground(highlightColor); // Set background color on focus
                    }

                    public void focusLost(java.awt.event.FocusEvent evt) {
                        ((JButton) evt.getSource()).setBackground(Color.WHITE); // Reset background color on focus lost
                    }
                });
                add(buttons[i][j]);
            }
        }
    }

    private void initializeResultLabel() {
        resultLabel = new JLabel("Result will be shown here", SwingConstants.CENTER);
        resultLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        resultLabel.setForeground(Color.GRAY);
        resultLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // Outer black border
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Empty border to separate label
        add(resultLabel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3; // Spanning three columns
        gbc.gridx = 0; // Starting at column 0
        gbc.gridy = 3; // Row 3
        add(resultLabel, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TicTacToeGUI game = new TicTacToeGUI();
                game.setVisible(true);
            }
        });
    }
}
