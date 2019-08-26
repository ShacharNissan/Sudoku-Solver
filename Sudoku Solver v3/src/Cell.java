import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane {
	static final int FILLED = 2;
	static final int NONE_AVAILABLE = -1;
	static final int ONE_AVAILABLE = 0;
	static final int MANY_AVAILABLE = 1;

	private SudokuBoard sudokuBoard;
	private TextField tf;
	private boolean[] available;
	private int myRow;
	private int myColumn;
	private int myBox;

	public Cell(SudokuBoard sudokuBoard, int box, int row, int column) {
		super();
		this.getStyleClass().add("cell");
		this.getChildren().add(createTextField());

		this.myBox = box;
		this.myRow = (((int) (box / 3)) * 3) + row;
		this.myColumn = (box % 3) * 3 + column;
		this.sudokuBoard = sudokuBoard;
		this.available = new boolean[9];
		for (int i = 0; i < 9; i++)
			available[i] = true;

	}

	public int getNumber() {
		if (!this.isFilled())
			return -1;

		return Integer.parseInt(tf.getText().trim());
	}

	public int getNumberOfAvailableValues() throws SudokuException {
		if (this.isFilled())
			return FILLED;

		int counter = 0;
		for (int i = 0; i < 9; i++)
			if (available[i])
				counter++;

		if (counter == 0)
			return NONE_AVAILABLE;
		if (counter == 1) {
			this.setOneAvailable();
			return ONE_AVAILABLE;
		}
		return MANY_AVAILABLE;
	}

	public void setFocus() {
		tf.requestFocus();
	}

	public void lock() {
		if (!this.isFilled())
			return;

		this.tf.setDisable(true);
	}

	public void setUnAvailable(int value) throws SudokuException {
		if (this.isFilled())
			return;

		if (value < 1 || value > 9)
			throw new SudokuException("Cell - value out of bounds.");

		available[value - 1] = false;
	}

	public void setUnAvailable(String nums) throws SudokuException {
		if (this.isFilled())
			return;

		for (int i = 0; i < nums.length(); i++) {
			int index = nums.charAt(i) - '0';
			if (index < 1 || index > 9)
				throw new SudokuException("Cell - value out of bounds.");
			this.available[index - 1] = false;
		}
	}

	public void setValue(int value) throws SudokuException {
		if (this.isFilled())
			return;

		if (value < 1 || value > 9)
			throw new SudokuException("Cell - value out of bounds.");

		sudokuBoard.valueAdded(value, myBox, myRow, myColumn);
		tf.setText(String.format("%d", value));
	}

	public boolean isFilled() {
		return !this.tf.getText().isEmpty();
	}

	public boolean isAvailable(int value) throws SudokuException {
		if (this.isFilled())
			return false;

		if (value < 1 || value > 9)
			throw new SudokuException("Cell - value out of bounds.");

		return this.available[value - 1];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Cell))
			return false;

		Cell cell = (Cell) obj;
		for (int i = 0; i < 9; i++)
			if (cell.available[i] != this.available[i])
				return false;

		return true;
	}

	public String getAllAvailable() {
		if (this.isFilled())
			return "";

		String str = "";
		for (int i = 0; i < 9; i++)
			if (this.available[i])
				str += i + 1;
		return str;
	}

	private TextField createTextField() {
		tf = new TextField();

		/* restrict input to integers: */

		tf.setTextFormatter(new TextFormatter<Integer>(c -> {
			if (c.getControlNewText().matches("\\d?")) {
				if (c.getText().equals("0"))
					c.setText("");
				return c;

			} else
				return null;

		}));

		tf.textProperty().addListener(event -> {
			try {
				sudokuBoard.textChanged(myBox, myRow, myColumn);
			} catch (SudokuException ex) {
				tf.setText("");
				SudokuBoardFx.Error_Alert(ex);
			}
		});

		tf.setOnKeyPressed(event -> keyBoardEventHandler(event));

		tf.setOnMouseClicked(event -> {
			try {
				mouseClickedEventHandler(event);
			} catch (SudokuException ex) {
				SudokuBoardFx.Error_Alert(ex);
			}
		});

		return tf;
	}

	private void mouseClickedEventHandler(MouseEvent event) throws SudokuException {
		this.sudokuBoard.getFocus().setFocus(this);
	}

	private void keyBoardEventHandler(KeyEvent event) {

		switch (event.getCode()) {
		case TAB:
		case RIGHT:
			this.sudokuBoard.getFocus().focusRight();
			event.consume();
			break;
		case UP:
			this.sudokuBoard.getFocus().focusUp();
			event.consume();
			break;
		case DOWN:
			this.sudokuBoard.getFocus().focusDown();
			event.consume();
			break;
		case LEFT:
			this.sudokuBoard.getFocus().focusLeft();
			event.consume();
			break;
		default:
			break;
		}

	}

	private void setOneAvailable() throws SudokuException {
		if (this.isFilled())
			return;

		int sum = 0, n = 0;
		for (int i = 0; i < 9; i++) {
			if (this.available[i]) {
				n = i + 1;
				sum++;
			}
		}
		if (sum != 1)
			return;

		this.setValue(n);
	}

	public void duplicate(Cell cell) throws SudokuException {
		if (cell.isFilled())
			this.setValue(cell.getNumber());

		else
			for (int i = 0; i < 9; i++)
				this.available[i] = cell.available[i];
	}

}
