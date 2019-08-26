import javafx.scene.layout.GridPane;

public class SudokuBoard {
	private Cell[][] boardCells;
	private Box[][] boardBoxes;
	private Focus focus;

	public SudokuBoard() throws SudokuException {
		boardBoxes = new Box[3][3];
		boardCells = new Cell[9][9];

		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
				boardBoxes[row][col] = new Box(this, (row * 3) + col);

		SyncBoxesToBoard(boardCells, boardBoxes);
	}

	public GridPane getBoardPane() throws SudokuException {
		GridPane board = new GridPane();
		board.getStyleClass().add("pane");

		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
				board.add(boardBoxes[row][col], col, row);

		focus = new Focus(boardCells);

		return board;
	}

	public static void SyncBoxesToBoard(Cell[][] boardCells, Box[][] boxes) {
		for (int i = 0; i < 3; i++) {
			int n = 0;
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					boardCells[(i * 3) + k][n] = boxes[i][j].getCells()[k][n % 3];
					boardCells[(i * 3) + k][n + 1] = boxes[i][j].getCells()[k][(n % 3) + 1];
					boardCells[(i * 3) + k][n + 2] = boxes[i][j].getCells()[k][(n % 3) + 2];
				}
				n = n + 3;
			}
		}
	}

	public void run() throws SudokuException, InterruptedException {
		lockSudoku();
		SudokuChecks.run(this);
	}

	public Cell[][] getBoardCells() {
		return this.boardCells;
	}

	public Box[][] getBoardBoxes() {
		return this.boardBoxes;
	}

	public Focus getFocus() {
		return this.focus;
	}

	public void textChanged(int boxNumber, int row, int column) throws SudokuException {

		// SudokuBoardFx.Message_Alert("check", String.format("box %d row %d col
		// %d", boxNumber, row, column));
		Box currentBox = boardBoxes[boxNumber / 3][boxNumber % 3];
		SudokuValidation.check_Row_Validation(boardCells, row);
		SudokuValidation.check_Column_Validation(boardCells, column);
		SudokuValidation.check_Box_Validation(currentBox);
	}

	public void valueAdded(int value, int boxNumber, int row, int column) throws SudokuException {
		Cell[] boxCells = this.boardBoxes[boxNumber / 3][boxNumber % 3].getCellsInVec();
		this.boardBoxes[boxNumber / 3][boxNumber % 3].setTaken(value);
		for (int i = 0; i < 9; i++) {
			boardCells[row][i].setUnAvailable(value);
			boardCells[i][column].setUnAvailable(value);
			boxCells[i].setUnAvailable(value);
		}
	}

	private void lockSudoku() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				boardCells[i][j].lock(); // locks only filled cells.

	}

	public void duplicate(SudokuBoard sudokuBoard) throws SudokuException {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.boardBoxes[i][j] = new Box(this, (i * 3) + j);
				this.boardBoxes[i][j].duplicate(sudokuBoard.getBoardBoxes()[i][j]);
			}
		}
	}
}
