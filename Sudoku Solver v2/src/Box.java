import javafx.css.PseudoClass;
import javafx.scene.layout.GridPane;

public class Box extends GridPane {

	private boolean[] filledNumbers;
	private Cell[][] cells;

	public Box() {
		super();
		this.getStyleClass().add("cell");
		this.pseudoClassStateChanged(PseudoClass.getPseudoClass("border"), true);

		this.filledNumbers = new boolean[9];
		this.cells = new Cell[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				filledNumbers[i] = false;
				this.cells[i][j] = new Cell();
				this.add(this.cells[i][j], j, i);
			}
		}
	}

	public Cell[][] getCells() {
		return this.cells;
	}

	public Cell[] getCellsInVec() {
		Cell[] row = new Cell[9];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				row[(i * 3) + j] = this.cells[i][j];

		return row;
	}

	public int isInOneRow(int value) throws SudokuException {
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

	public int isInOneColumn(int value) throws SudokuException {
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
			if (this.cells[row][i].isAvailable(value))
				return true;

		return false;

	}

	public boolean isAvailableInColumn(int col, int value) throws SudokuException {
		if (col < 0 || col > 2)
			throw new SudokuException("Box - col Value out of bound.");

		if (value < 1 || value > 9)
			throw new SudokuException("Box - Value out of bound.");

		if (isFilled(value))
			return false;

		for (int i = 0; i < 3; i++)
			if (this.cells[i][col].isAvailable(value))
				return true;

		return false;

	}

	public void setUnAvailableInRow(int row, int value) throws SudokuException {
		if (row < 0 || row > 2)
			throw new SudokuException("Box - row Value out of bound.");

		if (isFilled(value))
			return;

		for (int i = 0; i < 3; i++)
			this.cells[row][i].setUnAvailable(value);
	}

	public void setUnAvailableInColumn(int col, int value) throws SudokuException {
		if (col < 0 || col > 2)
			throw new SudokuException("Box - column Value out of bound.");

		if (isFilled(value))
			return;

		for (int i = 0; i < 3; i++)
			this.cells[i][col].setUnAvailable(value);
	}

	public void checkFilledNumbers() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (this.cells[i][j].isFilled())
					this.filledNumbers[this.cells[i][j].getNumber() - 1] = true;
			}
		}
	}
}
