import javafx.css.PseudoClass;
import javafx.scene.layout.GridPane;

public class Box extends GridPane {

	private boolean[] available;
	private Cell[][] cells;

	public Box() {
		this.getStyleClass().add("cell");
		this.pseudoClassStateChanged(PseudoClass.getPseudoClass("border"), true);

		this.available = new boolean[9];
		this.cells = new Cell[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				available[i] = true;
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
}
