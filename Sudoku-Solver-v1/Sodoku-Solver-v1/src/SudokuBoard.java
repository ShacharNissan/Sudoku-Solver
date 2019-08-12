
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SudokuBoard extends Application {

	@Override
	public void start(Stage primaryStage) {
		GridPane board = getBoard();
		Scene scene = new Scene(board);

		scene.getStylesheets().add("NumbersFont.css");

		primaryStage.setTitle("Shachar's Sudoku Solver");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private GridPane getBoard() {
		GridPane board = new GridPane();
		Box[][] boxes = new Box[3][3];

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				boxes[row][col] = new Box();
				board.add(boxes[row][col], col, row);
			}
		}

		Button goButton = new Button("Go!");
		StackPane bPane = new StackPane();
		goButton.setOnMouseClicked(e -> goClicked(boxes));
		bPane.getChildren().add(goButton);
		bPane.setAlignment(Pos.CENTER);
		bPane.setPadding(new Insets(13, 0, 10, 0));
		board.add(bPane, 1, 4);
		return board;
	}

	private void goClicked(Box[][] boxes) {
		SudokuChecks sc = new SudokuChecks(boxes);
		sc.run();
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

	public static void Error_Alert(SudokuException ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(ex.getMessage());

		alert.showAndWait();
	}

}