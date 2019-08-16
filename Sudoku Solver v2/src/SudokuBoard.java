
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SudokuBoard extends Application {
	private final double BOARD_WIDTH = 500.0, BOARD_HEIGHT = 550.0;

	private SudokuChecks sukudoChecks;
	private Cell[][] boardCells;
	private Box[][] boxes;
	private static Focus focus;

	Scene scene;

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane board = getBoard();
			scene = new Scene(board, BOARD_WIDTH, BOARD_HEIGHT);

			board.setAlignment(Pos.CENTER);
			scene.getStylesheets().add("NumbersFont.css");
			primaryStage.setTitle("Shachar's Sudoku Solver");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (SudokuException ex) {
			SudokuBoard.Error_Alert(ex);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void focusNext() {
		focus.focusNext();
	}

	public static void setFocus(Cell cell) {
		setFocus(cell, 0, 0);
	}

	public static void setFocus(Cell cell, int addRow, int AddColm) {
		try {
			focus.setFocus(cell, addRow, AddColm);
		} catch (SudokuException ex) {
			SudokuBoard.Error_Alert(ex);
		}
	}

	public static void Message_Alert(String title, String header, String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(msg);

		alert.showAndWait();
	}

	public static void Message_Alert(String title, String msg) {
		Message_Alert(title, null, msg);
	}

	public static void Error_Alert(SudokuException ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(ex.getMessage());

		alert.showAndWait();
	}

	private GridPane getBoard() throws SudokuException {
		GridPane board = new GridPane();
		boxes = new Box[3][3];

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				boxes[row][col] = new Box();
				board.add(boxes[row][col], col, row);
			}
		}
		SyncBoxesToBoard();
		focus = new Focus(boardCells);

		Button goButton = new Button("Go!");
		StackPane bPane = new StackPane();
		goButton.setOnMouseClicked(e -> {
			try {
				goClicked(boxes);
			} catch (SudokuException ex) {
				Error_Alert(ex);
			}
		});
		bPane.getChildren().add(goButton);
		bPane.setAlignment(Pos.CENTER);
		bPane.setPadding(new Insets(13, 0, 10, 0));
		board.add(bPane, 1, 4);
		return board;
	}

	private void SyncBoxesToBoard() {
		this.boardCells = new Cell[9][9];
		for (int i = 0; i < 3; i++) {

			int n = 0;
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					this.boardCells[(i * 3) + k][n] = boxes[i][j].getCells()[k][n % 3];
					this.boardCells[(i * 3) + k][n + 1] = boxes[i][j].getCells()[k][(n % 3) + 1];
					this.boardCells[(i * 3) + k][n + 2] = boxes[i][j].getCells()[k][(n % 3) + 2];
				}
				n = n + 3;
			}
		}
	}
	
	private void goClicked(Box[][] boxes) throws SudokuException {
		sukudoChecks = new SudokuChecks(boardCells, boxes);
	}
}