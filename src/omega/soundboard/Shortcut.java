package omega.soundboard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javafx.scene.input.KeyCodeCombination;

public class Shortcut implements Serializable {

	private static final long serialVersionUID = -4562036013141544357L;

	private boolean isKeyboardShortcut;
	private transient KeyCodeCombination keyboardShortcut;
	private String controllerShortcutName;

	public Shortcut(KeyCodeCombination keyboardShortcut) {
		isKeyboardShortcut = true;
		this.keyboardShortcut = keyboardShortcut;
	}

	public Shortcut(String controllerShortcutName) {
		isKeyboardShortcut = false;
		this.controllerShortcutName = controllerShortcutName;
	}

	public boolean isKeyboardShortcut() {
		return isKeyboardShortcut;
	}

	public KeyCodeCombination getKeyboardShortcut() {
		return keyboardShortcut;
	}

	public String getControllerShortcutName() {
		return controllerShortcutName;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		if (keyboardShortcut != null) {
			out.write(keyboardShortcut.getName().getBytes("UTF-8"));
		}
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(in, "UTF-8");
		scanner.useDelimiter("\\A");
		try {
			keyboardShortcut = (KeyCodeCombination) KeyCodeCombination.valueOf(scanner.next());
		} catch (NoSuchElementException e) {
		}
	}
}
