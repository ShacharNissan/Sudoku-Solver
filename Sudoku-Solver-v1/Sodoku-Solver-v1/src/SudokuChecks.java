
public class SudokuChecks {
	private Box[][] boxes;
	private Cell[][] sodukoCells;

	public SudokuChecks(Box[][] boxes) {
		this.boxes = boxes;
		this.sodukoCells = new Cell[9][9];
		for (int i = 0; i < 3; i++) {
			int n = 0;
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					this.sodukoCells[(i * 3) + k][n] = boxes[i][j].getCells()[k][n % 3];
					this.sodukoCells[(i * 3) + k][n + 1] = boxes[i][j].getCells()[k][(n % 3) + 1];
					this.sodukoCells[(i * 3) + k][n + 2] = boxes[i][j].getCells()[k][(n % 3) + 2];
				}
				n = n + 3;
			}
		}
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

	private void lockSudoku() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (this.sodukoCells[i][j].isFilled())
					this.sodukoCells[i][j].lock();
			}
		}
	}

	public void run() {
		lockSudoku();
		SudokuBoard.Message_Alert("Starting", "Solving Sudoku now.");

		boolean hasChanged;
		try {
			while (true) {
				hasChanged = this.setAllAvailable();
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
						}
					}
				}
				if (!hasChanged) {
					SudokuBoard.Message_Alert("Finish", "Sudoku has been Filled!");
					break;
				}
			}
		} catch (SudokuException ex) {
			SudokuBoard.Error_Alert(ex);
		}
	}
}
