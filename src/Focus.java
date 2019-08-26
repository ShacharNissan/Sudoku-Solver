
public class Focus {
	private int row;
	private int column;
	private Cell[][] boardCells;

	public Focus(Cell[][] cells) throws SudokuException {
		this.boardCells = cells;
		this.row = 0;
		this.column = 0;
		setFocus();
	}

	private void setFocus() {
		fixRotation();
		for (int i = 0; i < 9; i++) {
			boardCells[i][this.column].getStyleClass().add("setfocus");
			boardCells[this.row][i].getStyleClass().add("setfocus");
		}

		boardCells[this.row][this.column].setFocus();
	}

	private void removeFocus() {
		for (int i = 0; i < 9; i++) {
			boardCells[i][this.column].getStyleClass().remove("setfocus");
			boardCells[this.row][i].getStyleClass().remove("setfocus");
			;
		}
	}

	private void fixRotation() {
		if (column > 8)
			column = 0;

		if (column < 0)
			column = 8;

		if (row > 8)
			row = 0;

		if (row < 0)
			row = 8;
	}

	public void focusRight() {
		removeFocus();
		column++;
		this.setFocus();
	}

	public void focusLeft() {
		removeFocus();
		column--;
		this.setFocus();
	}

	public void focusUp() {
		removeFocus();
		row--;
		this.setFocus();
	}

	public void focusDown() {
		removeFocus();
		row++;
		this.setFocus();
	}

	public void setFocus(Cell cell) throws SudokuException {
		removeFocus();
		if (cell == null)
			throw new SudokuException("Focus - Unvalid Cell.");

		boolean flag = false;
		for (int i = 0; i < 9 && !flag; i++) {
			for (int j = 0; j < 9 && !flag; j++) {
				if (this.boardCells[i][j] == cell) {
					this.row = i;
					this.column = j;
					flag = true;
				}
			}
		}

		if (!flag)
			throw new SudokuException("Focus - Cell not Found");

		setFocus();
	}
}
