
public class SudokuChecks {
	private Box[][] boxes;
	private Cell[][] sodukoCells;

	public SudokuChecks(Cell[][] sodukoCells, Box[][] boxes) throws SudokuException {
		this.boxes = boxes;
		this.sodukoCells = sodukoCells;

		this.run();
	}

	private Cell[] getColm(int colm) {

		Cell[] colCells = new Cell[9];
		for (int i = 0; i < 9; i++)
			colCells[i] = this.sodukoCells[i][colm];
		return colCells;
	}

	private boolean setAllAvailable() throws SudokuException {
		boolean hasChanged = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int result = this.sodukoCells[i][j].getAvailable();
				if (result == Cell.NONE_AVAILABLE)
					throw new SudokuException("Error!\nCould not finish Solving Sudoku.");
				if (result == Cell.ONE_AVAILABLE)
					hasChanged = true;
			}
		}
		return hasChanged;
	}

	private boolean checkAvailableAllNumbers(Cell[] cellsVec) {
		int[] available = new int[9];
		Cell[] tempCells = new Cell[9];
		boolean hasChanged = false;

		for (Cell cell : cellsVec) {
			for (int i = 1; i <= 9; i++) {
				if (cell.isAvailable(i)) {
					available[i - 1]++;
					tempCells[i - 1] = cell;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			if (available[i] == 1) {
				tempCells[i].setValue(i + 1);
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	private void checkVec(Cell[] cellsVec) {
		String nums = "";
		for (int i = 0; i < 9; i++) {
			int n = 0;
			if ((n = cellsVec[i].getNumber()) != -1)
				nums += n;
		}
		for (int i = 0; i < 9; i++)
			cellsVec[i].setUnAvailable(nums);
	}

	private void checkForSimilarity(Cell[] cellsVec) {
		for (int i = 0; i < 8; i++) {
			if (cellsVec[i].isFilled())
				continue;

			int counter = 1;
			for (int j = i + 1; j < 9; j++) {
				if (cellsVec[j].isFilled())
					continue;
				if (cellsVec[i].equals(cellsVec[j]))
					counter++;
			}

			if (counter == cellsVec[i].getAllAvailable().length())
				removeNotSimilar(cellsVec, cellsVec[i]);
		}
	}

	private void removeNotSimilar(Cell[] cellsVec, Cell cell) {
		for (int i = 0; i < 9; i++) {

			if (cellsVec[i].isFilled())
				continue;

			if (!cell.equals(cellsVec[i]))
				cellsVec[i].setUnAvailable(cell.getAllAvailable());
		}
	}

	private void lockSudoku() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (this.sodukoCells[i][j].isFilled())
					this.sodukoCells[i][j].lock();
			}
		}
	}

	private void checkForPatternInBoxes() throws SudokuException {
		for (int value = 1; value <= 9; value++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (this.boxes[i][j].isFilled(value))
						continue;
					int row = this.boxes[i][j].isInOneRow(value);
					int col = this.boxes[i][j].isInOneColumn(value);

					if (row != -1)
						removeValueFromRow(i, j, value, row);
					if (col != -1)
						removeValueFromColumn(i, j, value, col);
				}
			}
		}
	}

	private void removeValueFromRow(int row, int col, int value, int fixedRow) throws SudokuException {
		for (int i = 0; i < 3; i++)
			if (i != col)
				this.boxes[row][i].setUnAvailableInRow(fixedRow, value);
	}

	private void removeValueFromColumn(int row, int col, int value, int fixedCol) throws SudokuException {
		for (int i = 0; i < 3; i++)
			if (i != row)
				this.boxes[i][col].setUnAvailableInColumn(fixedCol, value);
	}

	private void setupAllBoxes() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				this.boxes[i][j].checkFilledNumbers();
	}

	public void run() throws SudokuException {
		lockSudoku();
		SudokuBoard.Message_Alert("Starting", "Solving Sudoku now.");

		boolean hasChanged = false;
		;
		int unchanged = 0;
		nextNumber: while (true) {
			if (hasChanged)
				unchanged = 0;
			hasChanged = this.setAllAvailable();

			setupAllBoxes();

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					checkVec(this.sodukoCells[(i * 3) + j]);
					checkVec(this.getColm((i * 3) + j));
					checkVec(this.boxes[i][j].getCellsInVec());
					if (!hasChanged) {
						if (this.checkAvailableAllNumbers(this.sodukoCells[(i * 3) + j]))
							hasChanged = true;
						else if (this.checkAvailableAllNumbers(this.getColm((i * 3) + j)))
							hasChanged = true;
						else if (this.checkAvailableAllNumbers(this.boxes[i][j].getCellsInVec()))
							hasChanged = true;
					} else
						continue nextNumber;
				}
			}
			if (hasChanged)
				continue nextNumber;

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					checkForSimilarity(this.sodukoCells[(i * 3) + j]);
					checkForSimilarity(this.getColm((i * 3) + j));
					checkForSimilarity(this.boxes[i][j].getCellsInVec());
				}
			}

			checkForPatternInBoxes();

			if (!hasChanged) {
				unchanged++;
				if (unchanged >= 3) {
					SudokuBoard.Message_Alert("Finish", "Sudoku has been Filled!");
					break;
				}
			}
		}
	}
}
