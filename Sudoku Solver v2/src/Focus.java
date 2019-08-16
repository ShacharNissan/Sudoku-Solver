
public class Focus {
	private int row;
	private int colm;
	private Cell[][] cells;

	public Focus(Cell[][] cells) throws SudokuException {
		this.cells = cells;
		this.row = 0;
		this.colm = 0;
		setCurrent(row, colm);
	}

	private void setRow(int newRow) throws SudokuException {
		if (newRow == -1)
			this.row = 8;
		else if (newRow == 9)
			this.row = 0;
		else if (row >= 0 || row <= 8)
			this.row = newRow;
		else
			throw new SudokuException("Focus Row input out of bound.");
	}

	private void setColm(int newColm) throws SudokuException {
		if (newColm == -1)
			this.colm = 8;
		else if (newColm == 9)
			this.colm = 0;
		else if (colm >= 0 || colm <= 8)
			this.colm = newColm;
		else
			throw new SudokuException("Focus Column input out of bound.");
	}

	private void setCurrent(int row, int colm) throws SudokuException {
		if (row > 8 || row < 0 || colm > 8 || colm < 0)
			throw new SudokuException("Focus out of bounds!");
		setFocus();
	}

	private void setFocus() {
		for (int i = 0; i < 9; i++) {
			cells[i][this.colm].getStyleClass().add("setfocus");
			cells[this.row][i].getStyleClass().add("setfocus");
		}

		cells[this.row][this.colm].setFocus();
	}

	private void removeFocus() {
		for (int i = 0; i < 9; i++) {
			cells[i][this.colm].getStyleClass().remove("setfocus");
			cells[this.row][i].getStyleClass().remove("setfocus");
			;
		}
	}

	public void focusNext() {
		this.removeFocus();
		if (colm < 8)
			colm++;
		else {
			colm = 0;
			if (row < 8)
				row++;
			else
				row = 0;
		}
		this.setFocus();
	}

	public void setFocus(Cell cell, int addRow, int AddColm) throws SudokuException {
		removeFocus();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (this.cells[i][j] == cell) {
					setRow(i + addRow);
					setColm(j + AddColm);
				}
			}
		}
		setFocus();
	}
}
