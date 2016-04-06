package omega.soundboard.customcomponents;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class NumberInputDialog extends Dialog<Integer> {

	private final GridPane grid;
	private final Label label;
	private final TextField textField;

	public NumberInputDialog(Integer defaultValue) {
		final DialogPane dialogPane = getDialogPane();

		this.textField = new TextField(defaultValue.toString()) {
			@Override
			public void replaceText(int start, int end, String text) {
				if (text.matches("[0-9]*")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String text) {
				if (text.matches("[0-9]*")) {
					super.replaceSelection(text);
				}
			}
		};
		this.textField.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(textField, Priority.ALWAYS);
		GridPane.setFillWidth(textField, true);

		label = new Label(dialogPane.getContentText());
		label.setMaxWidth(Double.MAX_VALUE);
		label.setMaxHeight(Double.MAX_VALUE);
		label.getStyleClass().add("content");
		label.setWrapText(true);
		label.setPrefWidth(Region.USE_COMPUTED_SIZE);
		label.textProperty().bind(dialogPane.contentTextProperty());

		this.grid = new GridPane();
		this.grid.setHgap(10);
		this.grid.setMaxWidth(Double.MAX_VALUE);
		this.grid.setAlignment(Pos.CENTER_LEFT);

		dialogPane.contentTextProperty().addListener(o -> updateGrid());

		setTitle("OK");
		dialogPane.setHeaderText("Abbrechen");
		dialogPane.getStyleClass().add("text-input-dialog");
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		updateGrid();

		setResultConverter((dialogButton) -> {
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return data == ButtonData.OK_DONE ? Integer.valueOf(textField.getText()) : null;
		});
	}

	public void setValue(int value) {
		textField.setText(Integer.toString(value));
	}

	private void updateGrid() {
		grid.getChildren().clear();

		grid.add(label, 0, 0);
		grid.add(textField, 1, 0);
		getDialogPane().setContent(grid);

		Platform.runLater(() -> textField.requestFocus());
	}
}