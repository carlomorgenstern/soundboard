package omega.soundboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import omega.soundboard.App.Logic;
import omega.soundboard.customcomponents.NumberInputDialog;
import omega.soundboard.customcomponents.ShortcutInputDialog;
import omega.soundboard.customcomponents.SoundButton;

public class UIController {

	@FXML
	private GridPane buttonPane;

	@FXML
	private ToggleGroup buttonCountToggleGroup;

	@FXML
	private ToggleGroup columnCountToggleGroup;

	private final ToggleGroup soundButtonToggleGroup = new ToggleGroup();
	private final List<SoundButton> soundButtons = new ArrayList<SoundButton>();
	private MediaPlayer mediaPlayer;
	private FileChooser soundFileChooser = new FileChooser();

	private Logic logic;

	@FXML
	public void initialize() {
		buttonCountToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.getUserData() != null) {
				logic.setButtonCount(Integer.valueOf(newValue.getUserData().toString()));
				updateButtons();
			}
		});

		columnCountToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.getUserData() != null) {
				logic.setColumnCount(Integer.valueOf(newValue.getUserData().toString()));
				updateButtons();
			}
		});

		soundButtonToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			if (newValue != null && newValue.getUserData() instanceof Sound) {
				if (((Sound) newValue.getUserData()).getSoundFile() != null) {
					try {
						mediaPlayer = new MediaPlayer(
								new Media(((Sound) newValue.getUserData()).getSoundFile().toString()));
						mediaPlayer.play();
						mediaPlayer.setOnEndOfMedia(new ToggleDelecter(newValue));
					} catch (RuntimeException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Fehler");
						alert.setHeaderText("Es ist ein Fehler beim Abspielen des Sounds aufgetreten");
						alert.setContentText(e.getLocalizedMessage());
						alert.showAndWait();
						newValue.setSelected(false);

						e.printStackTrace();
					}
				} else {
					newValue.setSelected(false);
				}
			}
		});

		soundFileChooser.getExtensionFilters().add(
				new ExtensionFilter("Unterstütze Audioformate (.mp3 .wav .aif)", "*.mp3", "*.wav", "*.aif", "*.aiff"));
	}

	@FXML
	void openCustomCountDialog(ActionEvent event) {
		if (event.getSource() instanceof RadioMenuItem) {
			RadioMenuItem source = (RadioMenuItem) event.getSource();

			NumberInputDialog dialog = new NumberInputDialog(1);
			dialog.setHeaderText(null);
			if (source.getToggleGroup().equals(buttonCountToggleGroup)) {
				dialog.setValue(logic.getButtonCount());
				dialog.setTitle("Buttonzahl");
				dialog.setContentText("Buttonzahl:");
			} else if (source.getToggleGroup().equals(columnCountToggleGroup)) {
				dialog.setValue(logic.getColumnCount());
				dialog.setTitle("Spaltenzahl");
				dialog.setContentText("Spaltenzahl:");
			}

			Optional<Integer> result = dialog.showAndWait();
			result.ifPresent(newCount -> {
				source.setUserData(newCount);
				source.getToggleGroup().selectToggle(null);
				source.getToggleGroup().selectToggle(source);
			});
		}
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
		loadButtons();
	}

	public void loadButtons() {
		soundButtons.clear();
		try {
			List<Sound> sounds = logic.loadSounds();
			for (int i = 0; i < sounds.size(); i++) {
				soundButtons.add(new SoundButton(sounds.get(i), soundButtonToggleGroup, soundButtonContextMenu));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateButtons() {
		final int columnCount = logic.getColumnCount();
		final int rowCount = (logic.getButtonCount() % logic.getColumnCount() == 0
				? logic.getButtonCount() / logic.getColumnCount()
				: (logic.getButtonCount() / logic.getColumnCount()) + 1);
		buttonPane.getChildren().clear();
		buttonPane.getScene().getAccelerators().clear();

		buttonPane.getColumnConstraints().clear();
		for (int i = 0; i < columnCount; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(100 / columnCount);
			buttonPane.getColumnConstraints().add(column);
		}

		buttonPane.getRowConstraints().clear();
		for (int i = 0; i < rowCount; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100 / rowCount);
			buttonPane.getRowConstraints().add(row);
		}

		int column = 0, row = 0;
		for (int i = 0; i < logic.getButtonCount(); i++) {
			if (i < soundButtons.size()) {
				buttonPane.add(soundButtons.get(i), column++, row);
				if (soundButtons.get(i).getSound().getShortcut() != null) {
					logic.getShortcutHandler().registerShortcutListener(soundButtons.get(i).getSound().getShortcut(),
							soundButtons.get(i));
				}
			} else {
				buttonPane.add(new SoundButton(new Sound(), soundButtonToggleGroup, soundButtonContextMenu), column++,
						row);
			}

			if (column >= logic.getColumnCount()) {
				column = 0;
				row++;
			}
		}
	}

	public List<Sound> getButtonSounds() {
		List<Sound> result = new ArrayList<Sound>();
		for (int i = 0; i < soundButtons.size(); i++) {
			if (soundButtons.get(i).getSound().getSoundFile() != null) {
				result.add(soundButtons.get(i).getSound());
			}
		}
		return result;
	}

	private final ContextMenu soundButtonContextMenu = createSoundButtonContextMenu();

	private ContextMenu createSoundButtonContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();

		MenuItem editTitleItem = new MenuItem("Titel bearbeiten");
		editTitleItem.setOnAction(event -> {
			SoundButton ownerButton = (SoundButton) contextMenu.getOwnerNode();
			TextInputDialog dialog = new TextInputDialog(ownerButton.getText());
			dialog.setTitle("Titel bearbeiten");
			dialog.setHeaderText(null);
			dialog.setContentText("Titel:");

			Optional<String> result = dialog.showAndWait();
			result.ifPresent(newTitle -> {
				ownerButton.getSound().setTitle(newTitle);
				ownerButton.setText(newTitle);
			});
		});

		MenuItem editShortcutItem = new MenuItem("Shortcut bearbeiten");
		editShortcutItem.setOnAction(event -> {
			SoundButton ownerButton = (SoundButton) contextMenu.getOwnerNode();
			ShortcutInputDialog dialog;
			if (ownerButton.getSound().getShortcut() != null) {
				dialog = new ShortcutInputDialog(logic, ownerButton.getSound().getShortcut());
			} else {
				dialog = new ShortcutInputDialog(logic, null);
			}
			dialog.setTitle("Shortcut bearbeiten");
			dialog.setHeaderText(null);
			dialog.setContentText("Shortcut:");

			Optional<Shortcut> result = dialog.showAndWait();
			result.ifPresent(newShortcut -> {
				if (ownerButton.getSound().getShortcut() != null) {
					logic.getShortcutHandler().removeShortcutListener(ownerButton.getSound().getShortcut());
				}

				ownerButton.getSound().setShortcut(newShortcut);
				if (newShortcut != null) {
					logic.getShortcutHandler().registerShortcutListener(newShortcut, ownerButton);
				}
			});
		});

		MenuItem chooseSoundItem = new MenuItem("Sound zuweisen");
		chooseSoundItem.setOnAction(event -> {
			SoundButton ownerButton = (SoundButton) contextMenu.getOwnerNode();
			File file = soundFileChooser.showOpenDialog(ownerButton.getScene().getWindow());
			if (file != null) {
				if (ownerButton.getSound().getSoundFile() == null) {
					soundButtons.add(ownerButton);
				}
				Path fileInData = logic.copyToData(file.toPath());
				ownerButton.getSound().setSoundFile(fileInData.toUri());
				String newTitle = fileInData.getFileName().toString();
				if (newTitle.lastIndexOf('.') != -1) {
					newTitle = newTitle.substring(0, newTitle.lastIndexOf('.'));
				}
				ownerButton.getSound().setTitle(newTitle);
				ownerButton.setText(newTitle);
			}
		});

		MenuItem deleteSoundItem = new MenuItem("Sound löschen");
		deleteSoundItem.setOnAction(event -> {
			SoundButton ownerButton = (SoundButton) contextMenu.getOwnerNode();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Löschen bestätigen");
			alert.setHeaderText(null);
			alert.setContentText("Möchten Sie diesen Sound wirklich löschen?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				soundButtons.remove(ownerButton);
				updateButtons();
			}
		});
		contextMenu.getItems().addAll(editTitleItem, editShortcutItem, chooseSoundItem, deleteSoundItem);
		return contextMenu;
	};

	private class ToggleDelecter implements Runnable {

		private Toggle toggle;

		public ToggleDelecter(Toggle toggle) {
			this.toggle = toggle;
		}

		@Override
		public void run() {
			toggle.setSelected(false);
		}
	}
}
