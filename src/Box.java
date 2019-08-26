import javafx.css.PseudoClass;
import javafx.scene.layout.GridPane;

public class Box extends GridPane {

	private boolean[] filledNumbers;
	private Cell[][] boardCells;

	public Box(SudokuBoard sudokuBoard, int boxNumber) {
		super();
		this.getStyleClass().add("cell");
		this.pseudoClassStateChanged(PseudoClass.getPseudoClass("border"), true);

		this.filledNumbers = new boolean[9];
		this.boardCells = new Cell[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.boardCells[i][j] = new Cell(sudokuBoard, boxNumber, i, j);
				this.add(this.boardCells[i][j], j, i);
			}
		}
	}

	public Cell[][] getCells() {
		return this.boardCells;
	}

	public Cell[] getCellsInVec() {
		Cell[] row = new Cell[9];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				row[(i * 3) + j] = this.boardCells[i][j];

		return row;
	}

	public int isInOnlyRow(int value) throws SudokuException {
		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return -1;

		int rowNum = -1;
		for (int i = 0; i < 3; i++) {
			if (isAvailableInRow(i, value)) {
				if (rowNum == -1)
					rowNum = i;
				else
					return -1;
			}
		}

		return rowNum;
	}

	public int isInOnlyColumn(int value) throws SudokuException {
		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return -1;

		int colNum = -1;
		for (int i = 0; i < 3; i++) {
			if (isAvailableInColumn(i, value)) {
				if (colNum == -1)
					colNum = i;
				else
					return -1;
			}
		}

		return colNum;
	}

	public boolean isFilled(int value) throws SudokuException {
		if (value < 1 || value > 9)
			throw new SudokuException("Box - value out of bound");

		return this.filledNumbers[value - 1];
	}

	public boolean isAvailableInRow(int row, int value) throws SudokuException {
		if (row < 0 || row > 2)
			throw new SudokuException("Box - row Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return false;

		for (int i = 0; i < 3; i++)
			if (this.boardCells[row][i].isAvailable(value))
				return true;

		return false;

	}

	public boolean isAvailableInColumn(int column, int value) throws SudokuException {
		if (column < 0 || column > 2)
			throw new SudokuException("Box - column Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return false;

		for (int i = 0; i < 3; i++)
			if (this.boardCells[i][column].isAvailable(value))
				return true;

		return false;

	}

	public void setUnAvailableInRow(int row, int value) throws SudokuException {
		if (row < 0 || row > 2)
			throw new SudokuException("Box - row Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return;

		for (int i = 0; i < 3; i++)
			this.boardCells[row][i].setUnAvailable(value);
	}

	public void setUnAvailableInColumn(int column, int value) throws SudokuException {
		if (column < 0 || column > 2)
			throw new SudokuException("Box - column Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return;

		for (int i = 0; i < 3; i++)
			this.boardCells[i][column].setUnAvailable(value);
	}

	public void setAvailableInOnlyRow(int row, int value) throws SudokuException {
		if (row < 0 || row > 2)
			throw new SudokuException("Box - row Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return;

		for (int i = 0; i < 3; i++)
			if (i != row)
				this.setUnAvailableInRow(i, value);
	}

	public void setAvailableInOnlyColumn(int column, int value) throws SudokuException {
		if (column < 0 || column > 2)
			throw new SudokuException("Box - column Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return;

		for (int i = 0; i < 3; i++)
			if (i != column)
				this.setUnAvailableInColumn(i, value);
	}

	public void duplicate(Box box) throws SudokuException {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				this.boardCells[i][j].duplicate(box.boardCells[i][j]);

	}

	public boolean isFilled() throws SudokuException {
		for (int i = 0; i < 9; i++)
			if (!this.filledNumbers[i])
				return false;

		SudokuValidation.check_Box_Validation(this);
		return true;
	}

	public void setTaken(int value) throws SudokuException {
		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		this.filledNumbers[value - 1] = true;
	}

	public void setAllFilled() throws SudokuException {
		Cell[] cells = this.getCellsInVec();
		for (int i = 0; i < 9; i++)
			if (cells[i].isFilled())
				this.setTaken(cells[i].getNumber());
	}

}
