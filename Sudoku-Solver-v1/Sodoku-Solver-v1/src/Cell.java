import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane {
	static final int FILLED = 2;
	static final int NONE_AVAILABLE = -1;
	static final int ONE_AVAILABLE = 0;
	static final int MANY_AVAILABLE = 1;

	private TextField tf;
	private boolean[] available;

	public Cell() {
		this.getStyleClass().add("cell");
		this.getChildren().add(createTextField());
		this.available = new boolean[9];
		for (int i = 0; i < 9; i++)
			available[i] = true;

	}

	public int getNumber() {
		if (!this.isFilled())
			return -1;

		return Integer.parseInt(tf.getText().trim());
	}

	public int getAvailable() {
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

	public void removeAvailable(int index) {
		if (index < 0 || index >= 9)
			return;
		available[index] = false;
	}

	public void lock() {
		this.tf.setDisable(true);
	}

	public void setUnAvailable(String nums) {
		if (this.isFilled())
			return;

		for (int i = 0; i < nums.length(); i++) {
			int index = nums.charAt(i) - '0';
			this.available[index - 1] = false;
		}
	}

	public void setValue(int value) {
		if (this.isFilled())
			return;

		tf.setText(String.format("%d", value));
		tf.setEditable(false);
	}

	public boolean isFilled() {
		return !this.tf.getText().isEmpty();
	}

	public boolean isAvailable(int number) {
		if (this.isFilled())
			return false;

		if (number <= 0 || number > 9)
			return false;

		return this.available[number - 1];
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
		String str = "";
		for (int i = 0; i < 9; i++)
			if (this.available[i])
				str += i + 1;
		return str;
	}

	private TextField createTextField() {
		tf = new TextField();
		// restrict input to integers:
		tf.setTextFormatter(new TextFormatter<Integer>(c -> {
			if (c.getControlNewText().matches("\\d?")) {
				return c;
			} else {
				return null;
			}
		}));

		return tf;
	}
	
	private void setOneAvailable() {
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
	
}
