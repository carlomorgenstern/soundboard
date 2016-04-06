package omega.soundboard.customcomponents;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import omega.soundboard.ShortcutHandler.ShortcutListener;
import omega.soundboard.Sound;

public class SoundButton extends ToggleButton implements ShortcutListener {
	private Sound sound;

	public SoundButton(Sound sound, ToggleGroup buttonGroup, ContextMenu contextMenu) {
		super(sound.getTitle());
		this.sound = sound;
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		setToggleGroup(buttonGroup);
		setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				if (this.getSound().getSoundFile() == null) {
					contextMenu.getItems().get(0).setVisible(false);
					contextMenu.getItems().get(1).setVisible(false);
					contextMenu.getItems().get(3).setVisible(false);
				} else {
					contextMenu.getItems().get(0).setVisible(true);
					contextMenu.getItems().get(1).setVisible(true);
					contextMenu.getItems().get(3).setVisible(true);
				}
				contextMenu.show(this, event.getScreenX(), event.getScreenY());
			}
		});
	}

	public Sound getSound() {
		return sound;
	}

	public Object getUserData() {
		return sound;
	}

	@Override
	public void onShortcutPressed() {
		if (!isSelected()) {
			fire();
		}
	}
}
