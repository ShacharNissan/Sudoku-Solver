import java.util.Stack;

public class Guess {
	private static Stack<Guess> saves = new Stack<Guess>();

	private final SudokuBoard oldSudokuBoard;
	private SudokuBoard sudokuBoard;
	private int boxRow;
	private int boxCol;
	private int cellRow;
	private int cellCol;
	private int value;

	private Guess(SudokuBoard sudokuBoard) throws SudokuException {
		oldSudokuBoard = sudokuBoard;
		copySudoku();

		boxRow = 0;
		boxCol = 0;
		cellRow = 0;
		cellCol = 0;
		fill(1);

	}

	private void copySudoku() throws SudokuException {
		sudokuBoard = new SudokuBoard();
		sudokuBoard.duplicate(oldSudokuBoard);
	}

	private boolean fill(int value) throws SudokuException {
		Cell tempCell = null;
		Box[][] oldBoxes = oldSudokuBoard.getBoardBoxes();

		for (; boxRow < 3; boxRow++)
			for (; boxCol < 3; boxCol++)
				for (; cellRow < 3; cellRow++)
					for (; cellCol < 3; cellCol++)
						if (!(tempCell = oldBoxes[boxRow][boxCol].getCells()[cellRow][cellCol]).isFilled())
							if (fillValue(oldBoxes[boxRow][boxCol], tempCell, value))
								return true;

		return false;
	}

	private boolean fillValue(Box box, Cell tempCell, int value) throws SudokuException {
		if (tempCell == null)
			return false;

		for (int i = value; i <= 9; i++) {
			if (!box.isFilled(i)) {
				if (tempCell.isAvailable(i)) {
					tempCell.setValue(i);
					value = i;
					return true;
				}
			}
		}

		return false;
	}

	private boolean nextGuess() throws SudokuException {
		copySudoku();

		return fill(value + 1);
	}

	public static void guess(SudokuBoard sudokuBoard) throws SudokuException {
		saves.push(new Guess(sudokuBoard));
	}

	public static SudokuBoard guessFailed() throws SudokuException {
		while (!saves.isEmpty()) {
			if (saves.peek().nextGuess())
				return saves.peek().sudokuBoard;

			saves.pop();
		}
		return null;
	}

	public static boolean hasGuessed() {
		return !saves.isEmpty();
	}
}
