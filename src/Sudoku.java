import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sudoku {
    class Tile extends JButton {
        int r;
        int c;
        Tile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int boardWidth = 600;
    int boardHeight = 700;

    String[] puzzle = {
            "--74916-5",
            "2---6-3-9",
            "-----7-1-",
            "-586----4",
            "--3----9-",
            "--62--187",
            "9-4-7---2",
            "67-83----",
            "81--45---"
    };

    String[] solution = {
            "387491625",
            "241568379",
            "569327418",
            "758619234",
            "123784596",
            "496253187",
            "934176852",
            "675832941",
            "812945763"
    };

    JFrame frame = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JPanel numberPanel = new JPanel();
    JPanel actionPanel = new JPanel();

    JButton numSelected = null;

    Sudoku() {
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 28));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku Game");

        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));
        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(2, 1));
        numberPanel.setLayout(new GridLayout(1, 9));
        actionPanel.setLayout(new GridLayout(1, 2));

        buttonsPanel.add(numberPanel);
        buttonsPanel.add(actionPanel);
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        setupButtons();

        frame.setVisible(true);
    }

    void setupTiles() {
        boardPanel.removeAll();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c);
                char tileChar = puzzle[r].charAt(c);
                if (tileChar != '-') {
                    tile.setFont(new Font("Arial", Font.BOLD, 20));
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.lightGray);
                    tile.setForeground(Color.black);
                    tile.setFocusable(false);
                } else {
                    tile.setFont(new Font("Arial", Font.PLAIN, 20));
                    tile.setBackground(Color.white);
                    tile.setForeground(Color.red);
                }

                // Set thick borders between boxes
                if ((r == 2 && c == 2) || (r == 2 && c == 5) || (r == 5 && c == 2) || (r == 5 && c == 5)) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.black));
                } else if (r == 2 || r == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.black));
                } else if (c == 2 || c == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.black));
                } else {
                    tile.setBorder(BorderFactory.createLineBorder(Color.black));
                }

                tile.setFocusable(false);
                boardPanel.add(tile);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Tile tile = (Tile) e.getSource();
                        if (numSelected != null && tile.getBackground() == Color.white) { // ← solo permite editar blancas
                            tile.setText(numSelected.getText());
                            tile.setForeground(Color.red);
                        }
                    }
                });
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    void setupButtons() {
        // Number buttons 1-9
        for (int i = 1; i < 10; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusable(false);
            button.setBackground(Color.white);
            numberPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (numSelected != null) {
                        numSelected.setBackground(Color.white);
                    }
                    numSelected = button;
                    numSelected.setBackground(Color.lightGray);
                }
            });
        }

        // Check button
        JButton checkButton = new JButton("Check");
        checkButton.setFont(new Font("Arial", Font.BOLD, 20));
        checkButton.setFocusable(false);
        checkButton.setBackground(Color.cyan);
        actionPanel.add(checkButton);

        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Component[] components = boardPanel.getComponents();
                boolean allFilled = true;

                for (Component comp : components) {
                    if (comp instanceof Tile) {
                        Tile tile = (Tile) comp;
                        int r = tile.r;
                        int c = tile.c;
                        if (puzzle[r].charAt(c) == '-' && tile.getText().equals("")) {
                            allFilled = false;
                            break;
                        }
                    }
                }

                if (!allFilled) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all cells before checking.");
                    return;
                }

                int currentErrors = 0;
                for (Component comp : components) {
                    if (comp instanceof Tile) {
                        Tile tile = (Tile) comp;
                        int r = tile.r;
                        int c = tile.c;
                        if (puzzle[r].charAt(c) == '-') {
                            String current = tile.getText();
                            String correct = String.valueOf(solution[r].charAt(c));
                            if (!correct.equals(current)) {
                                currentErrors++;
                            }
                        }
                    }
                }

                if (currentErrors == 0) {
                    for (Component comp : components) {
                        if (comp instanceof Tile) {
                            Tile tile = (Tile) comp;
                            int r = tile.r;
                            int c = tile.c;
                            if (puzzle[r].charAt(c) == '-') {
                                tile.setForeground(Color.black);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Congratulations! You solved the Sudoku.");
                } else {
                    JOptionPane.showMessageDialog(frame, "There are " + currentErrors + " mistakes.");
                }
            }
        });

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 20));
        resetButton.setFocusable(false);
        resetButton.setBackground(Color.pink);
        actionPanel.add(resetButton);

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setupTiles();
            }
        });
    }
}