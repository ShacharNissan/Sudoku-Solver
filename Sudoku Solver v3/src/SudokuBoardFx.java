
/**
 * This is a Program that can solve any level of Sudoku.
 *
 * @author Shachar Nissan
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SudokuBoardFx extends Application {

	private final double BOARD_WIDTH = 500.0, BOARD_HEIGHT = 550.0;
	private SudokuBoard sudokuBoard;
	private BorderPane boardPane;

	@Override
	public void start(Stage primaryStage) {
		try {
			sudokuBoard = new SudokuBoard();
			boardPane = getBoard();
			Scene scene = new Scene(boardPane, BOARD_WIDTH, BOARD_HEIGHT);

			scene.getStylesheets().add("NumbersFont.css");

			primaryStage.setTitle("Shachar's Sudoku Solver");
			primaryStage.setScene(scene);
			primaryStage.show();
			sudokuBoard.getBoardCells()[0][0].setFocus();
		} catch (Exception ex) {
			Error_Alert(ex);
		}
	}

	public static void main(String[] args) {
		launch(args);
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

	public static void Error_Alert(Exception ex) {
		Error_Alert(ex.getMessage());
	}

	public static void Error_Alert(String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(string);

		alert.showAndWait();
	}

	private BorderPane getBoard() throws SudokuException {
		BorderPane board = new BorderPane();
		Button goButton = new Button("Go!");
		Button resetButton = new Button("Restart");
		HBox bPane = new HBox();

		goButton.getStyleClass().add("Button");
		resetButton.getStyleClass().add("Button");
		goButton.setOnMouseClicked(e -> goClicked());
		resetButton.setOnMouseClicked(e -> resetClicked());
		bPane.getStyleClass().add("buttonPane");
		bPane.getChildren().addAll(resetButton, goButton);
		board.setBottom(bPane);
		board.setCenter(sudokuBoard.getBoardPane());

		return board;
	}

	private void resetClicked() {
		try {
			sudokuBoard = new SudokuBoard();
			boardPane.setCenter(sudokuBoard.getBoardPane());
		} catch (Exception ex) {
			Error_Alert(ex);
		}
	}

	private void goClicked() {
		try {
			sudokuBoard.run();
		} catch (Exception ex) {
			Error_Alert(ex);
		}
	}

}