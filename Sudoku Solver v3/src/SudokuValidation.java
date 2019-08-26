
public abstract class SudokuValidation {

	public static void check_Row_Validation(Cell[][] board, int row) throws SudokuException {
		if (!check_vec_Validation(board[row]))
			throw new SudokuException("Row Validation Check Failed!");
	}

	public static void check_Column_Validation(Cell[][] board, int column) throws SudokuException {
		if (!check_vec_Validation(getColm(board, column)))
			throw new SudokuException("Column Validation Check Failed!");
	}

	public static void check_Box_Validation(Box box) throws SudokuException {
		if (!check_vec_Validation(box.getCellsInVec()))
			throw new SudokuException("Box Validation Check Failed!");
	}

	private static boolean check_vec_Validation(Cell[] boardVec) throws SudokuException {
		boolean[] isTaken = new boolean[9];
		for (int i = 0; i < 9; i++) {
			int index;
			if ((index = boardVec[i].getNumber()) != -1) {
				if (isTaken[index - 1])
					return false;
				else
					isTaken[index -1] = true;
			}
		}
		return true;
	}

	public static Cell[] getColm(Cell[][] board, int colm) {
		Cell[] colCells = new Cell[9];

		for (int i = 0; i < 9; i++)
			colCells[i] = board[i][colm];

		return colCells;
	}

	public static boolean check_Board_Validation(Box[][] boxes) throws SudokuException {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (!boxes[i][j].isFilled())
					return false;

		return true;
	}
}
