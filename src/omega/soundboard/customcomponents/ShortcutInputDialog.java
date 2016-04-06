package omega.soundboard.customcomponents;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import omega.soundboard.App.Logic;
import omega.soundboard.Shortcut;
import omega.soundboard.ShortcutHandler.ShortcutRequestListener;

public class ShortcutInputDialog extends Dialog<Shortcut>implements EventHandler<KeyEvent>, ShortcutRequestListener {

	private final GridPane grid;
	private final Label label;
	private final Label shortcutLabel;
	private final ToggleButton recordButton;

	private ModifierValue shiftPressed = ModifierValue.UP;
	private ModifierValue controlPressed = ModifierValue.UP;
	private ModifierValue altPressed = ModifierValue.UP;
	private Shortcut shortcut;

	public ShortcutInputDialog(Logic logic, Shortcut inputShortcut) {
		this.shortcut = inputShortcut;

		final DialogPane dialogPane = getDialogPane();

		shortcutLabel = new Label();
		shortcutLabel.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(shortcutLabel, Priority.ALWAYS);
		GridPane.setFillWidth(shortcutLabel, true);
		if (shortcut != null) {
			if (shortcut.isKeyboardShortcut()) {
				shortcutLabel.setText(shortcut.getKeyboardShortcut().getDisplayText());
			} else {
				shortcutLabel.setText(shortcut.getControllerShortcutName());
			}
		}

		label = new Label(dialogPane.getContentText());
		label.setMaxWidth(Double.MAX_VALUE);
		label.setMaxHeight(Double.MAX_VALUE);
		label.getStyleClass().add("content");
		label.setWrapText(true);
		label.setPrefWidth(Region.USE_COMPUTED_SIZE);
		label.textProperty().bind(dialogPane.contentTextProperty());

		recordButton = new ToggleButton("Aufnehmen");
		recordButton.getStyleClass().add("content");
		recordButton.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					if (newValue) {
						recordButton.addEventHandler(KeyEvent.KEY_PRESSED, this);
						recordButton.addEventHandler(KeyEvent.KEY_RELEASED, this);
						logic.getShortcutHandler().requestControllerShortcut(this);
					} else {
						recordButton.removeEventHandler(KeyEvent.KEY_PRESSED, this);
						recordButton.removeEventHandler(KeyEvent.KEY_RELEASED, this);
						logic.getShortcutHandler().interruptControllerShortcutRequest();

						if (shortcut != null) {
							if (shortcut.isKeyboardShortcut()) {
								shortcutLabel.setText(shortcut.getKeyboardShortcut().getDisplayText());
							} else {
								shortcutLabel.setText(shortcut.getControllerShortcutName());
							}
						}
					}
				});

		grid = new GridPane();
		grid.setHgap(10);
		grid.setMaxWidth(Double.MAX_VALUE);
		grid.setAlignment(Pos.CENTER_LEFT);

		dialogPane.contentTextProperty().addListener(o -> updateGrid());

		setTitle("OK");
		dialogPane.setHeaderText("Abbrechen");
		dialogPane.getStyleClass().add("text-input-dialog");
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		updateGrid();

		setResultConverter((dialogButton) -> {
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return data == ButtonData.OK_DONE ? shortcut : null;
		});
	}

	@Override
	public void handle(KeyEvent event) {
		if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
			if (event.getCode().equals(KeyCode.SHIFT)) {
				shiftPressed = ModifierValue.DOWN;
			} else if (event.getCode().equals(KeyCode.CONTROL)) {
				controlPressed = ModifierValue.DOWN;
			} else if (event.getCode().equals(KeyCode.ALT)) {
				altPressed = ModifierValue.DOWN;
			} else if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				shortcut = new Shortcut(new KeyCodeCombination(event.getCode(), shiftPressed, controlPressed,
						altPressed, ModifierValue.ANY, ModifierValue.ANY));
				recordButton.setSelected(false);
				shiftPressed = ModifierValue.UP;
				controlPressed = ModifierValue.UP;
				altPressed = ModifierValue.UP;
			}
		} else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
			if (event.getCode().equals(KeyCode.SHIFT)) {
				shiftPressed = ModifierValue.UP;
			} else if (event.getCode().equals(KeyCode.CONTROL)) {
				controlPressed = ModifierValue.UP;
			} else if (event.getCode().equals(KeyCode.ALT)) {
				altPressed = ModifierValue.UP;
			}
		}
	}

	@Override
	public void controllerShortcutDetermined(String componentName) {
		shortcut = new Shortcut(componentName);
		Platform.runLater(() -> recordButton.setSelected(false));
	}

	private void updateGrid() {
		grid.getChildren().clear();

		grid.add(label, 0, 0);
		grid.add(shortcutLabel, 1, 0);
		grid.add(recordButton, 2, 0);
		getDialogPane().setContent(grid);

		Platform.runLater(() -> recordButton.requestFocus());
	}
}