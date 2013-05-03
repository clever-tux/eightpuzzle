import java.awt.*;
import javax.swing.*;
import java.util.*;

public class EightDrawer {
    JButton[][] buttons;
    int blankRow;
    int blankCol;

    public EightDrawer(EightPuzzleState puzzle) {
	buttons = new JButton[3][3];
	blankRow = puzzle.blankRow;
	blankCol = puzzle.blankCol;

	Font font = new Font("Courier", Font.PLAIN, 36);

	for(int row = 0; row < 3; row++)
	    for(int col = 0; col < 3; col++) {
		buttons[row][col] = new JButton("" + puzzle.cells[row][col]);
		buttons[row][col].setFont(font);
		buttons[row][col].setSize(100, 100);
		buttons[row][col].setLocation(100 * col, 100 * row);
	    }
	buttons[blankRow][blankCol].setText("");
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("EightPuzzle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocation(400, 200);
	frame.setResizable(false);

	Container contentPane = frame.getContentPane();

	JPanel gamePane = new JPanel();
	gamePane.setLayout(null);
	gamePane.setPreferredSize(new Dimension(300, 300));

	for(int row = 0; row < 3; row++)
	    for(int col = 0; col < 3; col++) {
		gamePane.add(buttons[row][col]);
	    }

	contentPane.add(gamePane);

        frame.pack();
        frame.setVisible(true);
    }

    public void moveLeft() {
	int row = blankRow;
	int col = blankCol;
	buttons[row][col].setText(buttons[row][col-1].getText());
	buttons[row][col-1].setText("");
	blankCol--;
    }

    public void moveUp() {
	int row = blankRow;
	int col = blankCol;
	buttons[row][col].setText(buttons[row-1][col].getText());
	buttons[row-1][col].setText("");
	blankRow--;
    }

    public void moveRight() {
	int row = blankRow;
	int col = blankCol;
	buttons[row][col].setText(buttons[row][col+1].getText());
	buttons[row][col+1].setText("");
	blankCol++;
    }

    public void moveDown() {
	int row = blankRow;
	int col = blankCol;
	buttons[row][col].setText(buttons[row+1][col].getText());
	buttons[row+1][col].setText("");
	blankRow++;
    }
}
