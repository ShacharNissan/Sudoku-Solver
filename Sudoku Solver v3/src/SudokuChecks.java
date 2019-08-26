
public class SudokuChecks {

	/*
	 * Goes through all Cells sets the ones that have only one available value.
	 */
	private static boolean checkForSoloAvailableValue(Cell[][] boardCells) throws SudokuException {
		boolean hasChanged = false;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int result = boardCells[i][j].getNumberOfAvailableValues();
				if (result == Cell.NONE_AVAILABLE)
					throw new SudokuException("Error!\nCould not finish Solving Sudoku.");
				if (result == Cell.ONE_AVAILABLE)
					hasChanged = true;
			}
		}
		return hasChanged;
	}

	/*
	 * Goes through all Cells in a Row / Column / Box and checks for values that
	 * can only be on one Cell.
	 */
	private static boolean checkSoloAvailableCell(Cell[] cellsVec) throws SudokuException {
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

	/*
	 * Goes through all Cells and set available values for each unfilled cells.
	 */
	private static void readValuesAndSetAvailable(Cell[] cellsVec) throws SudokuException {
		String nums = "";
		for (int i = 0; i < 9; i++) {
			int n = 0;
			if ((n = cellsVec[i].getNumber()) != -1)
				nums += n;
		}
		for (int i = 0; i < 9; i++)
			cellsVec[i].setUnAvailable(nums);
	}

	/*
	 * Checks for similar cells that have the same available values and remove
	 * them from others.
	 */
	private static void checkForSimilarity(Cell[] cellsVec) throws SudokuException {
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

	private static void removeNotSimilar(Cell[] cellsVec, Cell cell) throws SudokuException {
		for (int i = 0; i < 9; i++) {

			if (cellsVec[i].isFilled())
				continue;

			if (!cell.equals(cellsVec[i]))
				cellsVec[i].setUnAvailable(cell.getAllAvailable());
		}
	}

	/*
	 * Checks for values that can only be in a single Row / Column and then
	 * remove it from the same Row / Column on near boxes.
	 */
	private static void checkForPatternInBoxes(Box[][] boardBoxes) throws SudokuException {
		for (int value = 1; value <= 9; value++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (boardBoxes[i][j].isFilled(value))
						continue;
					int row = boardBoxes[i][j].isInOnlyRow(value);
					int col = boardBoxes[i][j].isInOnlyColumn(value);

					if (row != -1)
						removeValueFromRow(boardBoxes, i, j, value, row);
					if (col != -1)
						removeValueFromColumn(boardBoxes, i, j, value, col);
				}
			}
		}
	}

	private static void removeValueFromRow(Box[][] boardBoxes, int row, int col, int value, int fixedRow)
			throws SudokuException {
		for (int i = 0; i < 3; i++)
			if (i != col)
				boardBoxes[row][i].setUnAvailableInRow(fixedRow, value);
	}

	private static void removeValueFromColumn(Box[][] boardBoxes, int row, int col, int value, int fixedCol)
			throws SudokuException {
		for (int i = 0; i < 3; i++)
			if (i != row)
				boardBoxes[i][col].setUnAvailableInColumn(fixedCol, value);
	}

	/*
	 * Checks for values that can NOT be in a single Row / Column and then sets
	 * it on the nearby Boxes.
	 */
	private static void checkSimilarAvailablePattern(Box[][] boardBoxes) throws SudokuException {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!boardBoxes[i][j].isFilled()) {
					for (int value = 1; value <= 9; value++) {
						for (int k = 0; k < 3; k++) {
							if (checkSoloRowAvailable(boardBoxes, i, j, k, value))
								boardBoxes[i][j].setAvailableInOnlyRow(k, value);

							if (checkSoloColumnAvailable(boardBoxes, i, j, k, value))
								boardBoxes[i][j].setAvailableInOnlyColumn(k, value);
						}
					}
				}
			}
		}
	}

	private static boolean checkSoloColumnAvailable(Box[][] boardBoxes, int boxRow, int boxColumn, int column,
			int value) throws SudokuException {
		if (!boardBoxes[boxRow][boxColumn].isAvailableInColumn(column, value))
			return false;

		for (int box = 0; box < 3; box++) {
			if (box == boxRow)
				continue;
			if (boardBoxes[box][boxColumn].isAvailableInColumn(column, value))
				return false;
		}
		return true;
	}

	private static boolean checkSoloRowAvailable(Box[][] boardBoxes, int boxRow, int boxColumn, int row, int value)
			throws SudokuException {
		if (!boardBoxes[boxRow][boxColumn].isAvailableInRow(row, value))
			return false;

		for (int box = 0; box < 3; box++) {
			if (box == boxColumn)
				continue;
			if (boardBoxes[boxRow][box].isAvailableInRow(row, value))
				return false;
		}
		return true;
	}

	public static void run(SudokuBoard sudokuBoard) throws SudokuException, InterruptedException {
		Box[][] boardBoxes = sudokuBoard.getBoardBoxes();
		Cell[][] boardCells = sudokuBoard.getBoardCells();
		boolean hasChanged = false;
		int unChanged = 0;

		SudokuBoardFx.Message_Alert("Starting", "Solving Sudoku now.");

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				readValuesAndSetAvailable(boardCells[(i * 3) + j]);
				readValuesAndSetAvailable(SudokuValidation.getColm(boardCells, (i * 3) + j));
				readValuesAndSetAvailable(boardBoxes[i][j].getCellsInVec());
				boardBoxes[i][j].setAllFilled();
			}
		}

		nextNumber: while (true) {
			if (hasChanged)
				unChanged = 0;
			hasChanged = false;
			if (checkForSoloAvailableValue(boardCells)) {
				hasChanged = true;
				continue nextNumber;
			}

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					checkForSimilarity(boardCells[(i * 3) + j]);
					checkForSimilarity(SudokuValidation.getColm(boardCells, (i * 3) + j));
					checkForSimilarity(boardBoxes[i][j].getCellsInVec());
				}
			}

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (checkSoloAvailableCell(boardCells[(i * 3) + j])) {
						hasChanged = true;
						continue nextNumber;
					} else if (checkSoloAvailableCell(SudokuValidation.getColm(boardCells, (i * 3) + j))) {
						hasChanged = true;
						continue nextNumber;
					} else if (checkSoloAvailableCell(boardBoxes[i][j].getCellsInVec())) {
						hasChanged = true;
						continue nextNumber;
					}
				}
			}

			checkForPatternInBoxes(boardBoxes);
			checkSimilarAvailablePattern(boardBoxes);

			if (!hasChanged) {
				try {
					if (SudokuValidation.check_Board_Validation(boardBoxes)) {
						SudokuBoardFx.Message_Alert("Done!", "Sudoku has been Filled!");
						break nextNumber;
					} else {
						unChanged++;
						if (unChanged >= 3) {
							Guess.guess(sudokuBoard);
						}
					}
				} catch (SudokuException ex) {
					if (Guess.hasGuessed())
						Guess.guessFailed();
					else {
						SudokuBoardFx.Error_Alert("Failed to Fill Sudoku.");
					}
				}
			}
		}
	}
}
